package ru.ps.geoloc.web;


import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.hibernate.type.IntegerType;
import org.junit.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.ps.geoloc.utils.LonLatToMetersConverter;
import ru.ps.geoloc.web.exceptions.LoadingGeoDataFailedException;
import ru.ps.geoloc.web.server.GeoLocHttpServer;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class TestCustomerService {
    private static final int HTTP_SERVER_PORT = 9088;

    @BeforeClass
    public static void startServer() throws IOException, LoadingGeoDataFailedException {
        GeoLocHttpServer.start(HTTP_SERVER_PORT);
        initDB();
    }

    public static void initDB() throws IOException, LoadingGeoDataFailedException {
        String geoNetFilename = "src/test/resources/geoNetSmall.csv";
        String geoUserLabelsFilename = "src/test/resources/geoUserLabelsSmall.csv";

        String url = new StringBuilder().append("http://localhost:").append(HTTP_SERVER_PORT).append("/geoloc/init").append("?").append("network=").append(geoNetFilename).append("&").append("label=").append(geoUserLabelsFilename).toString();
        HttpGet request = new HttpGet(url);
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        if (HttpStatus.SC_OK != httpResponse.getStatusLine().getStatusCode()) {
            throw new LoadingGeoDataFailedException("Loading of Geo data files failed");
        }
    }

    @Test
    public void testCreateUser() throws IOException {
        String url = "http://localhost:" + HTTP_SERVER_PORT + "/geoloc/user";

        HttpPut request = new HttpPut(url);
        request.addHeader("Content-Type", "application/json");

        String json = "{" +
                "\"user_id\": 124," +
                "\"lon\": 1.11," +
                "\"lat\": 2.22" +
                "}";

        StringEntity entity = new StringEntity(json);
        request.setEntity(entity);
        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        // Then
        assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());

        //Check if OK
        String json_string = EntityUtils.toString(httpResponse.getEntity());
        assertEquals(json_string, "\"OK\"");

    }

    @Test
    public void testCreateNonUniqueUser() throws IOException {
        String url = "http://localhost:" + HTTP_SERVER_PORT + "/geoloc/user";

        HttpPut request = new HttpPut(url);
        request.addHeader("Content-Type", "application/json");

        String json = "{" +
                "\"user_id\": 123," +
                "\"lon\": 10.11," +
                "\"lat\": 4.22" +
                "}";

        StringEntity entity = new StringEntity(json);
        request.setEntity(entity);
        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        // Then
        assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());

        //Check if OK
        String json_string = EntityUtils.toString(httpResponse.getEntity());
        assertEquals(json_string, "\"OK\"");

        //send same user send time
        httpResponse = HttpClientBuilder.create().build().execute(request);

        // Then
        assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());
        //Check if NOK
        json_string = EntityUtils.toString(httpResponse.getEntity());
        assertEquals("\"NOK\"", json_string);
    }

    @Test
    public void testDeleteUser() throws IOException {
        String createUserURL = "http://localhost:" + HTTP_SERVER_PORT + "/geoloc/user";

        HttpPut createNewUserRequest = new HttpPut(createUserURL);
        createNewUserRequest.addHeader("Content-Type", "application/json");

        String json = "{" +
                "\"user_id\": 123," +
                "\"lon\": 20.77," +
                "\"lat\": 10.99" +
                "}";

        StringEntity entity = new StringEntity(json);
        createNewUserRequest.setEntity(entity);
        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(createNewUserRequest);
        // Then
        assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());

        String deleteUserURL = "http://localhost:" + HTTP_SERVER_PORT + "/geoloc/user/123";
        HttpDelete deleteUserRequest = new HttpDelete(deleteUserURL);
        //When
        httpResponse = HttpClientBuilder.create().build().execute(deleteUserRequest);
        //Then
        String res_string = EntityUtils.toString(httpResponse.getEntity());
        assertEquals("\"OK\"", res_string);
    }

    @Test
    public void testDeleteNonExistingUser() throws IOException {
        String deleteUserURL = "http://localhost:" + HTTP_SERVER_PORT + "/geoloc/user/333";
        HttpDelete deleteUserRequest = new HttpDelete(deleteUserURL);
        //When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(deleteUserRequest);
        //Then
        String res_string = EntityUtils.toString(httpResponse.getEntity());
        assertEquals("\"NOK\"", res_string);
    }

    @Test
    public void testUpdateUser() throws IOException {
        String findNumOfUsersInCellURL = "http://localhost:" + HTTP_SERVER_PORT + "/geoloc/cell?x=10&y=10";

        HttpGet findNumberOfUsersInCellRequest = new HttpGet(findNumOfUsersInCellURL);

        //update user
        String updateUserURL = "http://localhost:" + HTTP_SERVER_PORT + "/geoloc/user";
        HttpPost updateUserRequest = new HttpPost(updateUserURL);
        updateUserRequest.addHeader("Content-Type", "application/json");
        String json = "{" +
                "\"user_id\": 1," +
                "\"lon\": 20.41," +
                "\"lat\": 20.11" +
                "}";
        StringEntity entity = new StringEntity(json);
        updateUserRequest.setEntity(entity);
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(updateUserRequest);
        String json_string = EntityUtils.toString(httpResponse.getEntity());
        //Then
        assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());
        assertEquals("\"OK\"", json_string);

        httpResponse = HttpClientBuilder.create().build().execute(findNumberOfUsersInCellRequest);
        json_string = EntityUtils.toString(httpResponse.getEntity());
        assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());
        assertEquals("\"1\"", json_string);
    }

    @Test
    public void testNumberOfUsersInCell() throws IOException, LoadingGeoDataFailedException {
        int cellX = LonLatToMetersConverter.lon2xCell(40.11);
        int cellY = LonLatToMetersConverter.lat2yCell(40.51);
        String json = "{" +
                "\"user_id\": 777," +
                "\"lon\": 40.41," +
                "\"lat\": 40.11" +
                "}";

        //create user
        String createUserURL = "http://localhost:" + HTTP_SERVER_PORT + "/geoloc/user";
        HttpPut createUserRequest = new HttpPut(createUserURL);
        createUserRequest.addHeader("Content-Type", "application/json");
        StringEntity entity = new StringEntity(json);
        createUserRequest.setEntity(entity);
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(createUserRequest);
        String json_string = EntityUtils.toString(httpResponse.getEntity());
        //Then
        assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());
        assertEquals("\"OK\"", json_string);

        //find num of users
        String findNumOfUsersInCellURL = new StringBuilder().append("http://localhost:").append(HTTP_SERVER_PORT)
                .append("/geoloc/cell").append("?").append("x=").append(cellX).append("&").append("y=").append(cellY).toString();

        HttpGet findNumberOfUsersInCellRequest = new HttpGet(findNumOfUsersInCellURL);

        //When
        httpResponse = HttpClientBuilder.create().build().execute(findNumberOfUsersInCellRequest);
        //Then
        String res_string = EntityUtils.toString(httpResponse.getEntity());
        assertEquals("\"1\"", res_string);
    }

    @Test
    public void testFindCustomerLocation() throws IOException, LoadingGeoDataFailedException {

        String url = "http://localhost:" + HTTP_SERVER_PORT + "/geoloc/user";

        HttpPut request = new HttpPut(url);
        request.addHeader("Content-Type", "application/json");

        String json = "{" +
                "\"user_id\": 555," +
                "\"lon\": 2.11," +
                "\"lat\": 2.22" +
                "}";

        StringEntity entity = new StringEntity(json);
        request.setEntity(entity);
        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        String findCustomerLocationURL = new StringBuilder().append("http://localhost:").append(HTTP_SERVER_PORT).append("/geoloc/user/").append("555").toString();
        HttpGet findCustomerLocationRequest = new HttpGet(findCustomerLocationURL);
        //When
        httpResponse = HttpClientBuilder.create().build().execute(findCustomerLocationRequest);
        //Then
        String res_string = EntityUtils.toString(httpResponse.getEntity());
        assertEquals("\"User location is inside cell. Cell coordinates x=1;y=1\"", res_string);
    }

    @AfterClass
    public static void stopServer() throws IOException {
        GeoLocHttpServer.stop();
    }
}

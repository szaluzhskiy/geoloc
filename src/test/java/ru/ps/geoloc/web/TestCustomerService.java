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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.ps.geoloc.web.exceptions.LoadingGeoDataFailedException;
import ru.ps.geoloc.web.server.GeoLocHttpServer;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class TestCustomerService {
    private static final int HTTP_SERVER_PORT = 9088;

    @Before
    public void startServer() throws IOException {
        GeoLocHttpServer.start(HTTP_SERVER_PORT);
    }

    @Test
    public void testCreateUser() throws IOException {
        String url = "http://localhost:"+HTTP_SERVER_PORT+ "/geoloc/user";

        HttpPut request = new HttpPut(url);
        request.addHeader("Content-Type", "application/json");

        String json = "{"+
                "\"user_id\": 123,"+
                "\"lon\": 1.11,"+
                "\"lat\": 2.22"+
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
        String url = "http://localhost:"+HTTP_SERVER_PORT+ "/geoloc/user";

        HttpPut request = new HttpPut(url);
        request.addHeader("Content-Type", "application/json");

        String json = "{"+
                "\"user_id\": 123,"+
                "\"lon\": 1.11,"+
                "\"lat\": 2.22"+
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
        String createUserURL = "http://localhost:"+HTTP_SERVER_PORT+ "/geoloc/user";

        HttpPut createNewUserRequest = new HttpPut(createUserURL);
        createNewUserRequest.addHeader("Content-Type", "application/json");

        String json = "{"+
                "\"user_id\": 123,"+
                "\"lon\": 1.77,"+
                "\"lat\": 3.99"+
                "}";

        StringEntity entity = new StringEntity(json);
        createNewUserRequest.setEntity(entity);
        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(createNewUserRequest);
        // Then
        assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());

        String deleteUserURL = "http://localhost:"+HTTP_SERVER_PORT+ "/geoloc/user/123";
        HttpDelete deleteUserRequest = new HttpDelete(deleteUserURL);
        //When
        httpResponse = HttpClientBuilder.create().build().execute(deleteUserRequest);
        //Then
        String res_string = EntityUtils.toString(httpResponse.getEntity());
        assertEquals("\"OK\"", res_string);
    }

    @Test
    public void testDeleteNonExistingUser() throws IOException {
        String createUserURL = "http://localhost:"+HTTP_SERVER_PORT+ "/geoloc/user";

        HttpPut createNewUserRequest = new HttpPut(createUserURL);
        createNewUserRequest.addHeader("Content-Type", "application/json");

        String json = "{"+
                "\"user_id\": 123,"+
                "\"lon\": 1.77,"+
                "\"lat\": 3.99"+
                "}";

        StringEntity entity = new StringEntity(json);
        createNewUserRequest.setEntity(entity);
        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(createNewUserRequest);
        // Then
        assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());

        String deleteUserURL = "http://localhost:"+HTTP_SERVER_PORT+ "/geoloc/user/124";
        HttpDelete deleteUserRequest = new HttpDelete(deleteUserURL);
        //When
        httpResponse = HttpClientBuilder.create().build().execute(deleteUserRequest);
        //Then
        String res_string = EntityUtils.toString(httpResponse.getEntity());
        assertEquals("\"NOK\"", res_string);
    }
    @Test
    public void testNumberOfUsersInCell() throws IOException, LoadingGeoDataFailedException {
        String geoNetFilename = "geoNetSmall.csv";
        String geoUserLabelsFilename = "geoUserLabelsSmall.csv";

        String url = new StringBuilder().append("http://localhost:").append(HTTP_SERVER_PORT).append("/geoloc/init").append("?").append("network=").append(geoNetFilename).append("&").append("label=").append(geoUserLabelsFilename).toString();
        HttpGet request = new HttpGet(url);
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        if (HttpStatus.SC_OK != httpResponse.getStatusLine().getStatusCode()) {
            throw new LoadingGeoDataFailedException("Loading of Geo data files failed");
        }

        String findNumberOfUsersURL = new StringBuilder().append("http://localhost:").append(HTTP_SERVER_PORT).append("/geoloc/users").toString();
        HttpGet findNumberOfUsersRequest = new HttpGet(findNumberOfUsersURL);
        //When
        httpResponse = HttpClientBuilder.create().build().execute(findNumberOfUsersRequest);
        //Then
        String res_string = EntityUtils.toString(httpResponse.getEntity());
        assertEquals("\"5\"", res_string);
    }


    @After
    public void stopServer() throws IOException {
        GeoLocHttpServer.stop();
    }
}

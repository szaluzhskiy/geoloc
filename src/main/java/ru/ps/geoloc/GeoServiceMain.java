package ru.ps.geoloc;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import ru.ps.geoloc.generator.GenerateGeoNetwork;
import ru.ps.geoloc.generator.GenerateUserData;
import ru.ps.geoloc.web.exceptions.LoadingGeoDataFailedException;
import ru.ps.geoloc.web.server.GeoLocHttpServer;

import java.io.IOException;


public class GeoServiceMain {

    private static final int HTTP_PORT = 8088;

    public static void main(String[] args) throws IOException, LoadingGeoDataFailedException {
        if (args.length == 1 && "gen".equalsIgnoreCase(args[0])) {
            GenerateGeoNetwork.generateGeograficNet();
            GenerateUserData.generateUsersData();
        } else if (args.length == 2) {
            GeoLocHttpServer.start(HTTP_PORT);
            //load data from input files
            String url = new StringBuilder().append("http://localhost:").append(HTTP_PORT).append("/geoloc/init").append("?").append("network=").append(args[0]).append("&").append("label=").append(args[1]).toString();
            HttpGet request = new HttpGet(url);
            HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
            if (HttpStatus.SC_OK != httpResponse.getStatusLine().getStatusCode()) {
                throw new LoadingGeoDataFailedException("Loading of Geo data files failed");
            }
            System.in.read();
        } else {
            System.out.println("Choose one of two usage options:");
            System.out.println("Option 1: GeoServiceMain gen");
            System.out.println("Used for generation of input files");
            System.out.println("Option 2: GeoServiceMain geoCell.filename geoLabels.filename");
            System.out.println("Used for init service with input files and start of serving");
        }
    }
}


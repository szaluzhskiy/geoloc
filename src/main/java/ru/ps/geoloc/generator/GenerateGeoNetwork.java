package ru.ps.geoloc.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GenerateGeoNetwork {

    private static final String DESCREPANCY_ERROR = "0.3";
    private static final double NETWORK_X = 50;
    private static final double NETWORK_Y = 1120;
    private static final String GEOGRAFICAL_NET_PATH = "geoNet.csv";

    public static void generateGeograficNet() throws IOException {
        File file = new File(GEOGRAFICAL_NET_PATH);
        FileWriter writer = new FileWriter(file);
        System.out.println("Starting generate geografical net...");
        //writer.append("title_x;title_y;discrepancy_error\n");
        //Network size
        for (int i = 0; i <= NETWORK_X; i++) {
            for (int j = 0; j <= NETWORK_Y; j++) {
                writer.append(Integer.toString(i));
                writer.append(';');
                writer.append(Integer.toString(j));
                writer.append(';');
                writer.append(DESCREPANCY_ERROR);
                writer.append('\r');
                writer.append('\n');
            }
        }
        writer.flush();
        writer.close();
        System.out.println("Geografical net was succesfully generated");
    }
}

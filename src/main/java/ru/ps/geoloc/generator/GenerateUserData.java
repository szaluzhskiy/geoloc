package ru.ps.geoloc.generator;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class GenerateUserData {

    private static final double RANGE_MIN = 0.0;
    private static final double RANGE_MAX = 90.0;
    private static final String USERS_DATA_PATH = "geoUserLabelsSmall.csv";

    public static void generateUsersData() throws IOException {
        File file = new File(USERS_DATA_PATH);
        FileWriter writer = new FileWriter(file);
        Random r = new Random();
        System.out.println("Starting generate users data...");
        //writer.append("user_id;lon;lat\n");
        for (int i = 1; i <= 1000000; i++) {
            writer.append(Integer.toString(i));
            writer.append(';');
            double value;
            value = RANGE_MIN + (RANGE_MAX-RANGE_MIN)*r.nextDouble();
            writer.append(Double.toString(value));
            writer.append(';');
            value = RANGE_MIN + (RANGE_MAX-RANGE_MIN)*r.nextDouble();
            writer.append(Double.toString(value));
            writer.append('\r');
            writer.append('\n');
        }
        writer.flush();
        writer.close();
        System.out.println("User data was succesfully generated");
    }
}


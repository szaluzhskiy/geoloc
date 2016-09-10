package ru.ps.geoloc.processors;

import org.springframework.beans.factory.annotation.Autowired;
import ru.ps.geoloc.loader.DataLoader;

import java.io.IOException;

public class InitProcessor {
    private final static String SUCCESS_OPERATION_RESPONSE = "OK";
    private final static String FAILED_OPERATION_RESPONSE = "NOK";

    private DataLoader dataLoader;

    public String init(String networkFileName, String fileGeoLabelsName) throws IOException {
        dataLoader.load(networkFileName, fileGeoLabelsName);
        return SUCCESS_OPERATION_RESPONSE;
    }

    public DataLoader getDataLoader() {
        return dataLoader;
    }

    public void setDataLoader(DataLoader dataLoader) {
        this.dataLoader = dataLoader;
    }
}

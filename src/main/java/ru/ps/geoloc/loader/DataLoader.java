package ru.ps.geoloc.loader;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.ps.geoloc.dao.InitDBDao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class DataLoader {

    private InitDBDao initDBDao;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void load(String networkFileName, String geoLabelsFileName) throws IOException {
        if (networkFileName != null && geoLabelsFileName != null) {
            try (Stream<String> stream = Files.lines(Paths.get(networkFileName))) {
                stream.forEach(line -> initDBDao.createGeoCellFromCSV(line));
            }
            try (Stream<String> stream = Files.lines(Paths.get(geoLabelsFileName))) {
                stream.forEach(line -> initDBDao.createUserFromCSV(line));
            }
        } else {
            throw new IllegalArgumentException("Both source files must be presented");
        }
    }

    public InitDBDao getInitDBDao() {
        return initDBDao;
    }

    public void setInitDBDao(InitDBDao initDBDao) {
        this.initDBDao = initDBDao;
    }
}

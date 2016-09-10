package ru.ps.geoloc.dao;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;
import ru.ps.geoloc.model.domain.GeoCell;
import ru.ps.geoloc.model.domain.GeoLabel;

@Transactional
public class InitDBDao implements InitializingBean {

    private HibernateTemplate hibernateTemplate;

    public InitDBDao() {
    }

    public void createUserFromCSV(String userRecord) {
        // userRecord is CSV separated
        String[] values = userRecord.split(";");
        GeoLabel geoLabel = new GeoLabel();
        geoLabel.setUserId(Integer.valueOf(values[0]));
        Double lon = Double.parseDouble(values[1]);
        Double lat = Double.parseDouble(values[2]);
        geoLabel.setLon(lon);
        geoLabel.setLat(lat);
        geoLabel.setCellX(lon.intValue());
        geoLabel.setCellY(lat.intValue());
        this.hibernateTemplate.persist(geoLabel);
        this.hibernateTemplate.flush();
        this.hibernateTemplate.clear();
    }

    public void createGeoCellFromCSV(String cell) {
        // userRecord is CSV separated
        String[] values = cell.split(";");
        GeoCell geoCell = new GeoCell();
        geoCell.setTile_x(Integer.valueOf(values[0]));
        geoCell.setTile_y(Integer.valueOf(values[1]));
        geoCell.setDistance_error(Double.parseDouble(values[2]));
        this.hibernateTemplate.persist(geoCell);
        this.hibernateTemplate.flush();
        this.hibernateTemplate.clear();
    }

    public HibernateTemplate getHibernateTemplate() {
        return hibernateTemplate;
    }

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if(this.getHibernateTemplate() == null) {
            throw new IllegalArgumentException("Property \'hibernateTemplate\' is required");
        }
    }
}

package ru.ps.geoloc.model.domain;

import org.hibernate.annotations.Index;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name ="geo_user_labels")
@org.hibernate.annotations.Table(appliesTo = "geo_user_labels",
        indexes = {
                @Index(name = "XIF_USRLAB_1", columnNames  = {"title_x" , "title_y"})
        }
)
public class GeoLabel {
    @Id
    @Column(name = "user_id")
    public Integer userId;
    @Column(name = "lon")
    public Double lon;
    @Column(name = "lat")
    public Double lat;
    @Column(name = "title_x")
    public Integer cellX;
    @Column(name = "title_y")
    public Integer cellY;


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Integer getCellX() {
        return cellX;
    }

    public void setCellX(Integer cellX) {
        this.cellX = cellX;
    }

    public Integer getCellY() {
        return cellY;
    }

    public void setCellY(Integer cellY) {
        this.cellY = cellY;
    }
}

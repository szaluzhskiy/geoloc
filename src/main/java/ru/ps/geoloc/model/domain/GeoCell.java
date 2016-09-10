package ru.ps.geoloc.model.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;

@Entity(name = "geo_network")
@IdClass(GeoCell.CellPK.class)
public class GeoCell {
    @Id
    @Column(name = "tile_x")
    private Integer tile_x;
    @Id
    @Column(name = "tile_")
    private Integer tile_y;

    private Double distance_error;

    public Integer getTile_x() {
        return tile_x;
    }

    public void setTile_x(Integer tile_x) {
        this.tile_x = tile_x;
    }

    public Integer getTile_y() {
        return tile_y;
    }

    public void setTile_y(Integer tile_y) {
        this.tile_y = tile_y;
    }

    public Double getDistance_error() {
        return distance_error;
    }

    public void setDistance_error(Double distance_error) {
        this.distance_error = distance_error;
    }

    public static class CellPK implements Serializable {
        private Integer tile_x;
        private Integer tile_y;

        public CellPK() {
        }

        public CellPK(Integer tile_x, Integer tile_y) {
            this.tile_x = tile_x;
            this.tile_y = tile_y;
        }
    }
}

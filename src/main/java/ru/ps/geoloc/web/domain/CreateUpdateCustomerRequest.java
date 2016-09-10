package ru.ps.geoloc.web.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({
        "user_id",
        "lon",
        "lat"
        })
public class CreateUpdateCustomerRequest {
    @JsonProperty("user_id")
    private Integer userID;
    @JsonProperty("lon")
    private Double lon;
    @JsonProperty("lat")
    private Double lat;

    public CreateUpdateCustomerRequest() {
    }

    public Integer getUserID() {
        return userID;
    }

    public Double getLon() {
        return lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    @Override
    public String toString() {
        return "ID: " + userID + "\n"
                + "     Lon: " + lon + "\n"
                + "     Lat: " + lat;
    }
}


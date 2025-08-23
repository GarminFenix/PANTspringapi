package com.example.springapi.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * Data Transfer Object for deserializing static site metadata
 * received from external Flask web service.
 * Maps JSON fields to java Site fields
 * @author Ross Cochrane
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class SiteMetadataDTO {
    private String systemCodeNumber;
    private double lat;
    private double lon;

    // Getters and setters
    public String getSystemCodeNumber() {
        return systemCodeNumber;
    }
    public void setSystemCodeNumber(String systemCodeNumber) {
        this.systemCodeNumber = systemCodeNumber;
    }

    public double getLat() {
        return lat;
    }
    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }
    public void setLon(double lon) {
        this.lon = lon;
    }

}

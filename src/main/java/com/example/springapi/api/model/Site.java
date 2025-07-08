package com.example.springapi.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;


/**
 * A class to represent air monitoring sites.
 * Each site has a unique system code number and
 * map coordinates
 */

@Entity
@Table(name = "sites")
public class Site {

    @Id
    private String systemCodeNumber;

    private double latitude;
    private double longitude;

    /**
     * No argument constructor
     */
    public Site(){}

    /**
     * Constructor for uni testing and manual instantiation
     * @param systemCodeNumber unique indentifier for each site
     */
    public Site(String systemCodeNumber, double latitude, double longitude) {
        this.systemCodeNumber = systemCodeNumber;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters and setters
    public String getSystemCodeNumber() {
        return systemCodeNumber;
    }
    public void setSystemCodeNumber(String systemCodeNumber) {
        this.systemCodeNumber = systemCodeNumber;
    }

    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


}

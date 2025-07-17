package com.example.springapi.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.PrePersist;
import org.locationtech.jts.geom.Point;

import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.PrecisionModel;




/**
 * A class to represent static air monitoring site metadata.
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


    // A column to store PostGIS geometry, converted from lat/long
    @Column(columnDefinition = "geometry(Point,4326)")
    private Point location;

    /**
     * No argument constructor
     */
    public Site(){}

    /**
     * Constructor for uni testing and manual instantiation
     * @param systemCodeNumber unique identifier for each site
     */
    public Site(String systemCodeNumber, double latitude, double longitude) {
        this.systemCodeNumber = systemCodeNumber;
        this.latitude = latitude;
        this.longitude = longitude;
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        this.location = geometryFactory.createPoint(new Coordinate(longitude, latitude));
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

    public Point getLocation() {
        return location;
    }
    public void setLocation(Point location) {
        this.location = location;
    }


}

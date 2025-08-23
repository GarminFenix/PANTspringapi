package com.example.springapi.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

/**
 * A class to represent dynamic site data.
 * Includes pollution and environmental metrics, timestamp
 * and is joined to the site meta data (Site model class) (ie map coordinates)
 * via the system code number
 * @author Ross Cochrane
 */

@Entity
@Table(name ="dynamic_readings")
public class DynamicReading {
    @Id
    @GeneratedValue
    private Long id;

    private Double co;
    private Double no;
    private Double no2;
    private Double temperature;
    private Double rh;
    private Double noise;
    private Double battery;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

    @ManyToOne
    @JoinColumn(name = "system_code_number", referencedColumnName = "systemCodeNumber")
    private Site site;

    // Getter and Setter to join dynamic readings to site
    public Site getSite() {
        return site;
    }
    public void setSite(Site site) {
        this.site = site;
    }

    // Getters and Setters
    public Double getCo() {
        return co;
    }
    public void setCo(Double co) {
        this.co = co;
    }

    public Double getNo() {
        return no;
    }
    public void setNo(Double no) {
        this.no = no;
    }

    public Double getNo2() {
        return no2;
    }
    public void setNo2(Double no2) {
        this.no2 = no2;
    }

    public Double getTemperature() {
        return temperature;
    }
    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getRh() {
        return rh;
    }
    public void setRh(Double rh) {
        this.rh = rh;
    }

    public Double getNoise() {
        return noise;
    }
    public void setNoise(Double noise) {
        this.noise = noise;
    }

    public Double getBattery() {
        return battery;
    }
    public void setBattery(Double battery) {
        this.battery = battery;
    }

    public OffsetDateTime getLastUpdated() {
        return lastUpdated;
    }
    public void setLastUpdated(OffsetDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}



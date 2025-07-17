package com.example.springapi.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Data Transfer Object for deserialsing each site's actual environmental metrics (ie
 * pollution data and timestamp) json data received from Flask web service
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class DynamicReadingDTO {
    private Double battery;
    private Double co;
    private Double no;
    private Double no2;
    private Double noise;
    private Double rh;
    private Double temperature;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime lastUpdated;

    // Getters and Setters
    public Double getBattery(){
        return battery;
    }
    public void setBattery(Double battery){
        this.battery = battery;
    }

    public Double getCo(){
        return co;
    }
    public void setCo(Double co){
        this.co = co;
    }

    public Double getNo(){
        return no;
    }
    public void setNo(Double no){
        this.no = no;
    }

    public Double getNo2(){
        return no2;
    }
    public void setNo2(Double no2){
        this.no2 = no2;
    }

    public Double getNoise(){
        return noise;
    }
    public void setNoise(Double noise){
        this.noise = noise;
    }

    public Double getRh(){
        return rh;
    }
    public void setRh(Double rh){
        this.rh = rh;
    }

    public Double getTemperature(){
        return temperature;
    }
    public void setTemperature(Double temperature){
        this.temperature = temperature;
    }

    public OffsetDateTime getLastUpdated() {
        return lastUpdated;
    }
    public void setLastUpdated(OffsetDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}


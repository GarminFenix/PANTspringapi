package com.example.springapi.api.dto;

import java.util.List;

/**
 * Data Transfer Object for deserialsing site level json data ie system code number
 * and list of environmental metrics received from Flask web service
 */

public class SiteReadingDTO {
    private String systemCodeNumber;
    private List<DynamicReadingDTO> dynamics;


    // Getters and Setters

    public String getSystemCodeNumber() {
        return systemCodeNumber;
    }
    public void setSystemCodeNumber(String systemCodeNumber) {
        this.systemCodeNumber = systemCodeNumber;
    }

    public List<DynamicReadingDTO> getDynamics() {
        return dynamics;
    }
    public void setDynamics(List<DynamicReadingDTO> dynamics) {
        this.dynamics = dynamics;
    }
}

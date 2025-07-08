package com.example.springapi.api.dto;

import java.util.List;

/**
 * Data Transfer Object for deserialsing incoming notification block
 * of json data received from Flask web service
 */

public class NotificationDTO {
    private String subscription;
    private String action;
    private List<SiteReadingDTO> notificationData;


    // Getters and Setters
    public String getSubscription() {
        return subscription;
    }
    public void setSubscription(String subscription) {
        this.subscription = subscription;
    }

    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }

    public List<SiteReadingDTO> getNotificationData() {
        return notificationData;
    }

    public void setNotificationData(List<SiteReadingDTO> notificationData) {
        this.notificationData = notificationData;
    }
}



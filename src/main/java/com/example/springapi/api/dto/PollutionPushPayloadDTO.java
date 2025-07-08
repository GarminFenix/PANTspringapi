package com.example.springapi.api.dto;

import javax.management.Notification;
import java.util.List;

/**
 * Data Transfer Object for deserialsing top level incoming json data
 * received from Flask web service
 */
public class PollutionPushPayloadDTO {
    private String subscriptionId;
    private List<NotificationDTO> notifications;


    // Getters and Setters
    public String getSubscriptionId(){
        return subscriptionId;
    }
    public void setSubscriptionId(String subscriptionId){
        this.subscriptionId=subscriptionId;
    }

    public List<NotificationDTO> getNotifications(){
        return notifications;
    }
    public void setNotifications(List<NotificationDTO> notifications) {
        this.notifications = notifications;
    }
}




package com.example.springapi.api.dto;


import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Data Transfer Object for deserialsing top level incoming json data
 * received from Flask web service
 * @author Ross Cochrane
 */

@JsonIgnoreProperties(ignoreUnknown = true)
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




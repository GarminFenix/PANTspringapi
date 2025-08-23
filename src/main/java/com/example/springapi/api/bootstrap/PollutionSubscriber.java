package com.example.springapi.api.bootstrap;

import com.example.springapi.api.service.DynamicReadingService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static com.example.springapi.api.bootstrap.NgrokConfig.NGROK_URL_SPRING_SUB;
import static com.example.springapi.api.bootstrap.NgrokConfig.NGROK_URL_WEB_SERVICE;

/**
 * Component that runs on start up to subscribe for pollution data
 * updates every 60 seconds
 */

@Component
public class PollutionSubscriber {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String SUBSCRIBE_URL = NGROK_URL_WEB_SERVICE + "/pollutiondata/subscribe";
    private final DynamicReadingService dynamicReadingService;

    public PollutionSubscriber(DynamicReadingService dynamicReadingService) {
        this.dynamicReadingService = dynamicReadingService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void subscribeToPollutionData(){
        // Clear previous readings
        try {
            dynamicReadingService.deleteAllReadings();
            System.out.println("Previous simulation readings deleted.");
        } catch (Exception e) {
            System.out.println("Error: failed to delete previous simulation readings.");
        }

        // Subscribe to pollution data
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("notificationUrl", NGROK_URL_SPRING_SUB);
        requestBody.put("subscriptions", List.of("AIR QUALITY DYNAMIC") );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(SUBSCRIBE_URL, request, String.class);
            System.out.println("Subscribed to pollution data: " + response.getStatusCode());
        } catch (Exception ex) {
            System.err.println("Failed to subscribe to pollution data: " + ex.getMessage());
        }
    }
}

package com.example.springapi.api.bootstrap;

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

/**
 * Component that runs on start up to subscribe for pollution data
 * updates every 60 seconds
 */

@Component
public class PollutionSubscriber {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String SUBSCRIBE_URL = "https://airdatageneration-fmhnbdh7cheee4ae.ukwest-01.azurewebsites.net/pollutiondata/subscribe";

    @EventListener(ApplicationReadyEvent.class)
    public void subscribeToPollutionData(){
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("notificationUrl", "https://05004b4d187f.ngrok-free.app/api/pollution/receive");
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

package com.example.springapi.api.controller;

import com.example.springapi.api.dto.PollutionPushPayloadDTO;
import com.example.springapi.api.dto.DynamicReadingDTO;
import com.example.springapi.api.dto.SiteReadingDTO;
import com.example.springapi.api.model.DynamicReading;
import com.example.springapi.api.model.Site;
import com.example.springapi.api.repository.SiteRepository;
import com.example.springapi.api.service.DynamicReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * REST controller for handling dynamic air quality data
 * notifications
 */
@RestController
@RequestMapping("/api/pollution")
public class DynamicController {private final DynamicReadingService dynamicReadingService;
    private final SiteRepository siteRepository;

    @Autowired
    public DynamicController(DynamicReadingService dynamicReadingService, SiteRepository siteRepository) {
        this.dynamicReadingService = dynamicReadingService;
        this.siteRepository = siteRepository;
    }

    /**
     * Endpoint to receive web service dynamic air quality push notifications.
     */
    @PostMapping("/receive")
    public ResponseEntity<Void> receiveDynamicData(@RequestBody PollutionPushPayloadDTO payload) {
        System.out.println("Received dynamic data push"); //debugging
        payload.getNotifications().forEach(notification -> {
            notification.getNotificationData().forEach(siteReadingDTO -> {
                String scn = siteReadingDTO.getSystemCodeNumber();
                Optional<Site> siteOpt = siteRepository.findBySystemCodeNumber(scn);

                if (siteOpt.isPresent()) {
                    Site site = siteOpt.get();
                    for (DynamicReadingDTO dto : siteReadingDTO.getDynamics()) {
                        DynamicReading reading = new DynamicReading();
                        reading.setSite(site);
                        reading.setCo(dto.getCo());
                        reading.setNo(dto.getNo());
                        reading.setNo2(dto.getNo2());
                        reading.setRh(dto.getRh());
                        reading.setTemperature(dto.getTemperature());
                        reading.setNoise(dto.getNoise());
                        reading.setBattery(dto.getBattery());
                        reading.setLastUpdated(dto.getLastUpdated());

                        dynamicReadingService.upsertLatestReading(scn, reading);
                    }
                }
            });
        });

        return ResponseEntity.ok().build();
    }

}

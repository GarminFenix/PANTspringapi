package com.example.springapi.api.service;


import com.example.springapi.api.model.DynamicReading;
import com.example.springapi.api.repository.DynamicReadingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service layer for managing dynamic reading entities
 */
@Service
public class DynamicReadingService {
    private final DynamicReadingRepository dynamicReadingRepository;

    /**
     * Constructs a DynamicReadingService with the given dynamicReadingRepository
     *
     * @param dynamicReadingRepository the repository used for data access
     */
    @Autowired
    public DynamicReadingService(DynamicReadingRepository dynamicReadingRepository) {
        this.dynamicReadingRepository = dynamicReadingRepository;
    }

    /**
     * Retrieves the latest pollution data for a given site
     *
     * @param systemCodeNumber unique identifier
     * @return optional containing the dynamic reading data as a list
     */
    public Optional<DynamicReading> getLatestReadingForSite(String systemCodeNumber){
        return dynamicReadingRepository.findTopBySite_SystemCodeNumberOrderByLastUpdatedDesc(systemCodeNumber);
    }

    /**
     * Creates a new dynamic reading for specified site, or updates it if more recent.
     *
     * @param systemCodeNumber unique identifier
     * @param newReading the reading to be inserted if newer
     * @return either saved or new reading depending if newer
     */
    public DynamicReading upsertLatestReading(String systemCodeNumber, DynamicReading newReading) {
        Optional<DynamicReading> existing = dynamicReadingRepository
                .findTopBySite_SystemCodeNumberOrderByLastUpdatedDesc(systemCodeNumber);

        if (existing.isPresent() && !newReading.getLastUpdated().isAfter(existing.get().getLastUpdated())) {
            return existing.get(); // Ignore if not newer
        }

        return dynamicReadingRepository.save(newReading);
    }



}

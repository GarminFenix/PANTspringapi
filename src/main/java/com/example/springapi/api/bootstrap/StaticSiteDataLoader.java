package com.example.springapi.api.bootstrap;

import com.example.springapi.api.dto.SiteMetadataDTO;
import com.example.springapi.api.model.Site;
import com.example.springapi.api.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Component that runs on application startup to fetch static site metadata
 * from the web service and synchronize it with the database.
 * Preserves existing dynamic_readings by avoiding deletions and instead
 * updating lat/lon and geometry where necessary.
 */
@Component
public class StaticSiteDataLoader {

    private final SiteService siteService;
    private final RestTemplate restTemplate = new RestTemplate();

    private static final String METADATA_URL =
            "https://airdatageneration-fmhnbdh7cheee4ae.ukwest-01.azurewebsites.net/pollutiondata/sitemetadata";

    @Autowired
    public StaticSiteDataLoader(SiteService siteService) {
        this.siteService = siteService;
    }

    /**
     * Fetches site metadata from the Flask web service and synchronizes it
     * with the database. Any missing geometry is backfilled without deleting
     * or overwriting dependent dynamic readings.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void fetchAndStoreSiteMetadata() {
        SiteMetadataDTO[] siteArray = restTemplate.getForObject(METADATA_URL, SiteMetadataDTO[].class);

        if (siteArray != null) {
            List<Site> incomingSites = Arrays.stream(siteArray)
                    .map(dto -> new Site(dto.getSystemCodeNumber(), dto.getLat(), dto.getLon()))
                    .toList();

            incomingSites.forEach(incoming -> {
                Optional<Site> existingOpt = siteService.getSiteBySystemCodeNumber(incoming.getSystemCodeNumber());

                if (existingOpt.isPresent()) {
                    Site existing = existingOpt.get();
                    existing.setLatitude(incoming.getLatitude());
                    existing.setLongitude(incoming.getLongitude());
                    siteService.createSite(existing); // updates with new coords
                } else {
                    siteService.createSite(incoming); // insert new site
                }
            });

            // Patch any geometry that wasn't set before. One off
            siteService.backfillMissingGeometry();
        }
    }
}

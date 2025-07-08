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
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Component that runs on application startup to fetch static site metadata
 * from the external Flask web service and synchronize it with the database.
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
     * Fetches site metadata from the web service on startup and synchronizes it with the database.
     * - Inserts new sites
     * - Skips existing ones
     * - Deletes any sites no longer present in the upstream source
     */
    @EventListener(ApplicationReadyEvent.class)
    public void fetchAndStoreSiteMetadata() {
        SiteMetadataDTO[] siteArray = restTemplate.getForObject(METADATA_URL, SiteMetadataDTO[].class);

        if (siteArray != null) {
            List<Site> incomingSites = Arrays.stream(siteArray)
                    .map(dto -> new Site(dto.getSystemCodeNumber(), dto.getLat(), dto.getLon()))
                    .toList();

            Set<String> incomingCodes = incomingSites.stream()
                    .map(Site::getSystemCodeNumber)
                    .collect(Collectors.toSet());

            List<String> existingCodes = siteService.getAllSystemCodeNumbers();

            // Delete stale sites
            existingCodes.stream()
                    .filter(code -> !incomingCodes.contains(code))
                    .forEach(siteService::deleteIfExists);

            // Insert new sites
            incomingSites.forEach(site ->
                    siteService.getSiteBySystemCodeNumber(site.getSystemCodeNumber())
                            .ifPresentOrElse(
                                    existing -> {}, // skip
                                    () -> siteService.createSite(site)
                            )
            );
        }
    }
}

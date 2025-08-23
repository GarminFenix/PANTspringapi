package com.example.springapi.api.controller;


import com.example.springapi.api.model.Site;
import com.example.springapi.api.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Site entities.
 * Handles HTTP requests for creating, retrieving and deleting
 * @author Ross Cochrane
 */

@RestController
@RequestMapping("/api/sites")
public class SiteController {

    @Autowired
    SiteService siteService;


    /**
     * Retrieves all sites from db
     * @return list of all sites
     */
    @GetMapping
    public List<Site> getAllSites() {
        return siteService.getAllSites();
    }

    /**
     * Retrieves a site
     * @param systemCodeNumber unique identifier
     * @return the site if found
     */
    @GetMapping("/{systemCodeNumber}")
    public ResponseEntity<Site> getSiteBySystemCodeNumber(@PathVariable String systemCodeNumber) {
        Optional<Site> site = siteService.getSiteBySystemCodeNumber(systemCodeNumber);
        return site.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Creates a new site and saves to database
     * @param site the Site object to be created
     * @return the created Site entity
     */
    @PostMapping
    public Site createSite(@RequestBody Site site) {
        return siteService.createSite(site);
    }

    /**
     * Deletes a site
     * @param systemCodeNumber unique identifier
     * @return 204 no content if deletion is successful
     */
    @DeleteMapping("/{systemCodeNumber}")
    public ResponseEntity<Void> deleteSite(@PathVariable String systemCodeNumber) {
        siteService.deleteSite(systemCodeNumber);
        return ResponseEntity.noContent().build();
    }
}
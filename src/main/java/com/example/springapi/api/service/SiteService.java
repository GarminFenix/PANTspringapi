package com.example.springapi.api.service;

import com.example.springapi.api.model.Site;
import com.example.springapi.api.repository.SiteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for managing Site entities.
 */
@Service
public class SiteService {

    private final SiteRepository siteRepository;

    /**
     * Constructs a SiteService with the given SiteRepository.
     *
     * @param siteRepository the repository used for data access
     */
    @Autowired
    public SiteService(SiteRepository siteRepository) {
        this.siteRepository = siteRepository;
    }

    /**
     * Retrieves all sites from the database.
     *
     * @return a list of all Site entities
     */
    public List<Site> getAllSites() {
        return siteRepository.findAll();
    }

    /**
     * Retrieves a site by its system code number.
     *
     * @param systemCodeNumber unique identifier
     * @return optional containing the site if found
     */
    public Optional<Site> getSiteBySystemCodeNumber(String systemCodeNumber) {
        return siteRepository.findBySystemCodeNumber(systemCodeNumber);
    }

    /**
     * Creates a new site.
     *
     * @param site the Site entity to be created
     * @return the saved Site entity
     */
    public Site createSite(Site site) {
        return siteRepository.save(site);
    }

    /**
     * Deletes a site by system code number if it exists.
     *
     * @param systemCodeNumber unique identifier
     */
    @Transactional
    public void deleteIfExists(String systemCodeNumber) {
        if (siteRepository.existsBySystemCodeNumber(systemCodeNumber)) {
            siteRepository.deleteBySystemCodeNumber(systemCodeNumber);
        }
    }

    /**
     * Deletes a site
     * @param systemCodeNumber unique identifier
     */
    public void deleteSite(String systemCodeNumber) {
        siteRepository.deleteBySystemCodeNumber(systemCodeNumber);
    }

    /**
     * Retrieves all system code numbers from the database.
     *
     * @return a list of system code numbers
     */
    public List<String> getAllSystemCodeNumbers() {
        return siteRepository.findAll()
                .stream()
                .map(Site::getSystemCodeNumber)
                .toList();
    }
}

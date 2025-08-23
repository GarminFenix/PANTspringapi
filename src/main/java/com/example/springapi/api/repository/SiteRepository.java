package com.example.springapi.api.repository;

import com.example.springapi.api.model.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for accessing and managing Site entities.
 * @author Ross Cochrane
 */

@Repository
public interface SiteRepository extends JpaRepository<Site, String> {

    /**
     * Method to find a site by its system code number
     * @param systemCodeNumber unique identifier
     * @return optional that returns the site if found
     */
    Optional<Site> findBySystemCodeNumber(String systemCodeNumber);

    /**
     * Method to check if a site exists
     * @param systemCodeNumber unique identifier
     * @return true if exists
     */
    boolean existsBySystemCodeNumber(String systemCodeNumber);

    /**
     * Method to delete a site
     * @param systemCodeNumber unique identifier
     */
    void deleteBySystemCodeNumber(String systemCodeNumber);
}

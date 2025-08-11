package com.example.springapi.api.repository;

import com.example.springapi.api.model.Site;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link SiteRepository}.
 * Uses {@code @DataJpaTest} to test repository behavior with an in-memory database.
 * Verifies custom query methods and basic CRUD operations.
 */
@DataJpaTest
public class SiteRepositoryTest {

    @Autowired
    private SiteRepository siteRepository;

    /**
     * Tests that {@link SiteRepository#findBySystemCodeNumber(String)} returns a site
     * when a matching system code number exists in the database.
     */
    @Test
    public void testFindBySystemCodeNumberReturnsSite() {
        Site site = new Site();
        site.setSystemCodeNumber("ABC123");
        site.setLatitude(53.0);
        site.setLongitude(0.43);

        siteRepository.save(site);

        Optional<Site> result = siteRepository.findBySystemCodeNumber("ABC123");

        assertThat(result).isPresent();
        assertThat(result.get().getLatitude()).isEqualTo(53.0);
    }

    /**
     * Tests that {@link SiteRepository#existsBySystemCodeNumber(String)} returns true
     * when a site with the given system code number exists.
     */
    @Test
    public void testExistsBySystemCodeNumberReturnsTrue() {
        Site site = new Site();
        site.setSystemCodeNumber("XYZ789");
        site.setLatitude(51.5);
        site.setLongitude(-0.1);

        siteRepository.save(site);

        boolean exists = siteRepository.existsBySystemCodeNumber("XYZ789");

        assertThat(exists).isTrue();
    }

    /**
     * Tests that {@link SiteRepository#deleteBySystemCodeNumber(String)} successfully
     * removes a site from the database.
     */
    @Test
    public void testDeleteBySystemCodeNumberRemovesSite() {
        Site site = new Site();
        site.setSystemCodeNumber("DEL456");
        site.setLatitude(60.0);
        site.setLongitude(5.0);

        siteRepository.save(site);

        siteRepository.deleteBySystemCodeNumber("DEL456");

        Optional<Site> result = siteRepository.findBySystemCodeNumber("DEL456");

        assertThat(result).isNotPresent();
    }
}
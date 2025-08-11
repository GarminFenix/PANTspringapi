package com.example.springapi.api.repository;

import com.example.springapi.api.model.DynamicReading;
import com.example.springapi.api.model.Site;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link DynamicReadingRepository}.
 * Verifies custom query methods using an in-memory H2 database.
 */
@DataJpaTest
public class DynamicReadingRepositoryTest {

    @Autowired
    private DynamicReadingRepository dynamicReadingRepository;

    @Autowired
    private SiteRepository siteRepository;

    /**
     * Tests that {@link DynamicReadingRepository#findTopBySite_SystemCodeNumberOrderByLastUpdatedDesc(String)}
     * returns the most recent reading for a given site.
     */
    @Test
    public void testFindTopBySiteSystemCodeNumberOrderByLastUpdatedDescReturnsLatestReading() {
        // Create and save a site
        Site site = new Site();
        site.setSystemCodeNumber("SITE001");
        site.setLatitude(59.9);
        site.setLongitude(10.8);
        siteRepository.saveAndFlush(site); // ✅ Flush to persist immediately

        // Create and save older reading
        DynamicReading olderReading = new DynamicReading();
        olderReading.setSite(site);
        olderReading.setCo(0.3);
        olderReading.setLastUpdated(OffsetDateTime.now().minusHours(2));
        dynamicReadingRepository.save(olderReading);

        // Create and save newer reading
        DynamicReading newerReading = new DynamicReading();
        newerReading.setSite(site);
        newerReading.setCo(0.7);
        newerReading.setLastUpdated(OffsetDateTime.now());
        dynamicReadingRepository.save(newerReading);

        // Fetch the most recent reading
        Optional<DynamicReading> result = dynamicReadingRepository
                .findTopBySite_SystemCodeNumberOrderByLastUpdatedDesc("SITE001");

        // Assert that the latest reading is returned
        assertThat(result).isPresent();
        assertThat(result.get().getCo()).isEqualTo(0.7);
    }
}
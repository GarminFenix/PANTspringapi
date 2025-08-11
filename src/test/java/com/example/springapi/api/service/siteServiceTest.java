package com.example.springapi.api.service;

import com.example.springapi.api.model.Site;
import com.example.springapi.api.repository.SiteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.PrecisionModel;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link SiteService}.
 * Uses Mockito to mock dependencies and verify service behavior.
 */
class SiteServiceTest {

    private SiteRepository siteRepository;
    private SiteService siteService;

    /**
     * Initializes the test environment before each test.
     * Creates a mock {@link SiteRepository} and injects it into {@link SiteService}.
     */
    @BeforeEach
    void setUp() {
        siteRepository = mock(SiteRepository.class);
        siteService = new SiteService(siteRepository);
    }

    /**
     * Tests retrieval of all sites.
     * Verifies that {@link SiteRepository#findAll()} is called and returns expected results.
     */
    @Test
    void testGetAllSites() {
        Site site1 = new Site();
        Site site2 = new Site();
        when(siteRepository.findAll()).thenReturn(List.of(site1, site2));

        List<Site> result = siteService.getAllSites();

        assertEquals(2, result.size());
        verify(siteRepository).findAll();
    }

    /**
     * Tests retrieval of a site by system code number.
     * Verifies that the correct site is returned when present.
     */
    @Test
    void testGetSiteBySystemCodeNumber() {
        String code = "ABC123";
        Site site = new Site();
        when(siteRepository.findBySystemCodeNumber(code)).thenReturn(Optional.of(site));

        Optional<Site> result = siteService.getSiteBySystemCodeNumber(code);

        assertTrue(result.isPresent());
        verify(siteRepository).findBySystemCodeNumber(code);
    }

    /**
     * Tests creation of a new site.
     * Verifies that the site is saved using {@link SiteRepository#save(Site)}.
     */
    @Test
    void testCreateSite() {
        Site site = new Site();
        when(siteRepository.save(site)).thenReturn(site);

        Site result = siteService.createSite(site);

        assertNotNull(result);
        verify(siteRepository).save(site);
    }

    /**
     * Tests conditional deletion of a site when it exists.
     * Verifies that {@link SiteRepository#deleteBySystemCodeNumber(String)} is called.
     */
    @Test
    void testDeleteIfExists_whenExists() {
        String code = "XYZ789";
        when(siteRepository.existsBySystemCodeNumber(code)).thenReturn(true);

        siteService.deleteIfExists(code);

        verify(siteRepository).deleteBySystemCodeNumber(code);
    }

    /**
     * Tests conditional deletion when the site does not exist.
     * Verifies that no deletion occurs.
     */
    @Test
    void testDeleteIfExists_whenNotExists() {
        String code = "XYZ789";
        when(siteRepository.existsBySystemCodeNumber(code)).thenReturn(false);

        siteService.deleteIfExists(code);

        verify(siteRepository, never()).deleteBySystemCodeNumber(code);
    }

    /**
     * Tests unconditional deletion of a site.
     * Verifies that the site is deleted regardless of existence.
     */
    @Test
    void testDeleteSite() {
        String code = "DEL123";

        siteService.deleteSite(code);

        verify(siteRepository).deleteBySystemCodeNumber(code);
    }

    /**
     * Tests retrieval of all system code numbers.
     * Verifies that the correct list of identifiers is returned.
     */
    @Test
    void testGetAllSystemCodeNumbers() {
        Site site1 = new Site();
        site1.setSystemCodeNumber("A1");
        Site site2 = new Site();
        site2.setSystemCodeNumber("B2");
        when(siteRepository.findAll()).thenReturn(List.of(site1, site2));

        List<String> result = siteService.getAllSystemCodeNumbers();

        assertEquals(List.of("A1", "B2"), result);
    }

    /**
     * Tests backfilling of missing geometry for sites.
     * Verifies that sites with null location are updated and saved.
     */
    @Test
    void testBackfillMissingGeometry() {
        Site siteWithNullLocation = new Site();
        siteWithNullLocation.setLatitude(59.91);
        siteWithNullLocation.setLongitude(10.75);
        siteWithNullLocation.setLocation(null);

        Site siteWithLocation = new Site();
        GeometryFactory gf = new GeometryFactory(new PrecisionModel(), 4326);
        Point point = gf.createPoint(new Coordinate(10.75, 59.91));
        siteWithLocation.setLocation(point);

        when(siteRepository.findAll()).thenReturn(List.of(siteWithNullLocation, siteWithLocation));
        when(siteRepository.save(siteWithNullLocation)).thenReturn(siteWithNullLocation);

        siteService.backfillMissingGeometry();

        assertNotNull(siteWithNullLocation.getLocation());
        verify(siteRepository).save(siteWithNullLocation);
        verify(siteRepository, never()).save(siteWithLocation);
    }
}
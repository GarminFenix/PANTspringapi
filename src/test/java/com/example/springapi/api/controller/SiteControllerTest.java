package com.example.springapi.api.controller;

import com.example.springapi.api.model.Site;
import com.example.springapi.api.controller.SiteController;
import com.example.springapi.api.service.SiteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link SiteController}.
 * Verifies the behavior of REST endpoints using mocked service layer.
 */
class SiteControllerTest {

    private SiteService siteService;
    private SiteController siteController;

    @BeforeEach
    void setUp() {
        siteService = mock(SiteService.class);
        siteController = new SiteController();
        siteController.siteService = siteService; // Direct field injection for simplicity
    }

    /**
     * Tests retrieval of all sites.
     * Ensures the controller returns the list provided by the service.
     */
    @Test
    void testGetAllSites() {
        Site site1 = new Site("001", 53.0, 0.43);
        Site site2 = new Site("002", 53.29, 0.562);
        List<Site> mockSites = Arrays.asList(site1, site2);

        when(siteService.getAllSites()).thenReturn(mockSites);

        List<Site> result = siteController.getAllSites();

        assertEquals(2, result.size());
        assertEquals("001", result.get(0).getSystemCodeNumber());
        verify(siteService, times(1)).getAllSites();
    }

    /**
     * Tests retrieval of a site by system code number when found.
     * Ensures the controller returns HTTP 200 with the site.
     */
    @Test
    void testGetSiteBySystemCodeNumber_Found() {
        Site site = new Site("001", 53.34, 0.54);
        when(siteService.getSiteBySystemCodeNumber("001")).thenReturn(Optional.of(site));

        ResponseEntity<Site> response = siteController.getSiteBySystemCodeNumber("001");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("001", response.getBody().getSystemCodeNumber());
        verify(siteService).getSiteBySystemCodeNumber("001");
    }

    /**
     * Tests retrieval of a site by system code number when not found.
     * Ensures the controller returns HTTP 404.
     */
    @Test
    void testGetSiteBySystemCodeNumber_NotFound() {
        when(siteService.getSiteBySystemCodeNumber("999")).thenReturn(Optional.empty());

        ResponseEntity<Site> response = siteController.getSiteBySystemCodeNumber("999");

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(siteService).getSiteBySystemCodeNumber("999");
    }

    /**
     * Tests creation of a new site.
     * Ensures the controller returns the created site.
     */
    @Test
    void testCreateSite() {
        Site site = new Site("003", 53.45, 0.56);
        when(siteService.createSite(site)).thenReturn(site);

        Site result = siteController.createSite(site);

        assertEquals("003", result.getSystemCodeNumber());
        verify(siteService).createSite(site);
    }

    /**
     * Tests deletion of a site.
     * Ensures the controller returns HTTP 204 No Content.
     */
    @Test
    void testDeleteSite() {
        doNothing().when(siteService).deleteSite("001");

        ResponseEntity<Void> response = siteController.deleteSite("001");

        assertEquals(204, response.getStatusCodeValue());
        verify(siteService).deleteSite("001");
    }
}
package com.example.springapi.api.controller;

import com.example.springapi.api.dto.*;
import com.example.springapi.api.model.Site;
import com.example.springapi.api.service.DynamicReadingService;
import com.example.springapi.api.repository.SiteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for {@link DynamicController}.
 * Uses constructor injection and custom configuration to avoid deprecated @MockBean.
 */
@WebMvcTest(controllers = DynamicController.class)
public class DynamicControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DynamicReadingService dynamicReadingService;

    @Autowired
    private SiteRepository siteRepository;

    private PollutionPushPayloadDTO payload;
    private ObjectMapper objectMapper;

    /**
     * Sets up test data before each test.
     */
    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());


        Site site = new Site();
        site.setSystemCodeNumber("ABC123");

        when(siteRepository.findBySystemCodeNumber("ABC123")).thenReturn(Optional.of(site));

        payload = buildMockPayload("ABC123");
    }

    /**
     * Tests that a valid payload returns HTTP 200 OK.
     */
    @Test
    public void testReceiveDynamicDataReturnsOk() throws Exception {
        String jsonPayload = objectMapper.writeValueAsString(payload);

        mockMvc.perform(post("/api/pollution/receive")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isOk());

        verify(dynamicReadingService, times(1)).upsertLatestReading(eq("ABC123"), any());
    }

    /**
     * Builds a mock PollutionPushPayloadDTO for testing.
     */
    private PollutionPushPayloadDTO buildMockPayload(String systemCodeNumber) {
        DynamicReadingDTO readingDTO = new DynamicReadingDTO();
        readingDTO.setCo(0.5);
        readingDTO.setNo(0.1);
        readingDTO.setNo2(0.2);
        readingDTO.setRh(45.0);
        readingDTO.setTemperature(22.0);
        readingDTO.setNoise(60.0);
        readingDTO.setBattery(3.7);
        readingDTO.setLastUpdated(OffsetDateTime.now());

        SiteReadingDTO siteReadingDTO = new SiteReadingDTO();
        siteReadingDTO.setSystemCodeNumber(systemCodeNumber);
        siteReadingDTO.setDynamics(List.of(readingDTO));

        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setSubscription("sub-001");
        notificationDTO.setAction("push");
        notificationDTO.setNotificationData(List.of(siteReadingDTO));

        PollutionPushPayloadDTO payload = new PollutionPushPayloadDTO();
        payload.setSubscriptionId("sub-001");
        payload.setNotifications(List.of(notificationDTO));

        return payload;
    }

    /**
     * Custom test configuration to inject mocked beans via constructor.
     */
    @TestConfiguration
    static class DynamicControllerTestConfig {

        @Bean
        public DynamicReadingService dynamicReadingService() {
            return mock(DynamicReadingService.class);
        }

        @Bean
        public SiteRepository siteRepository() {
            return mock(SiteRepository.class);
        }

        @Bean
        public DynamicController dynamicController(DynamicReadingService service, SiteRepository repo) {
            return new DynamicController(service, repo);
        }
    }
}
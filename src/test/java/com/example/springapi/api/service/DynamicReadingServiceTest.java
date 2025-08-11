package com.example.springapi.api.service;

import com.example.springapi.api.model.DynamicReading;
import com.example.springapi.api.repository.DynamicReadingRepository;
import com.example.springapi.api.service.DynamicReadingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link DynamicReadingService}.
 * Uses Mockito to mock dependencies and validate service logic.
 */
public class DynamicReadingServiceTest {

    private DynamicReadingRepository repository;
    private DynamicReadingService service;

    /**
     * Sets up the test environment before each test.
     * Mocks the repository and injects it into the service.
     */
    @BeforeEach
    public void setUp() {
        repository = mock(DynamicReadingRepository.class);
        service = new DynamicReadingService(repository);
    }

    /**
     * Tests that the service returns a reading when one is found for a given site.
     */
    @Test
    public void testGetLatestReadingForSite_found() {
        DynamicReading reading = new DynamicReading();
        when(repository.findTopBySite_SystemCodeNumberOrderByLastUpdatedDesc("ABC123"))
                .thenReturn(Optional.of(reading));

        Optional<DynamicReading> result = service.getLatestReadingForSite("ABC123");

        assertTrue(result.isPresent()); // Expect a result
        assertEquals(reading, result.get()); // Should match the mocked reading
    }

    /**
     * Tests that the service returns an empty Optional when no reading is found.
     */
    @Test
    public void testGetLatestReadingForSite_notFound() {
        when(repository.findTopBySite_SystemCodeNumberOrderByLastUpdatedDesc("XYZ789"))
                .thenReturn(Optional.empty());

        Optional<DynamicReading> result = service.getLatestReadingForSite("XYZ789");

        assertFalse(result.isPresent()); // Expect no result
    }

    /**
     * Tests that the service calls deleteAll on the repository.
     */
    @Test
    public void testDeleteAllReadings() {
        service.deleteAllReadings();
        verify(repository, times(1)).deleteAll(); // Ensure deleteAll was called once
    }

    /**
     * Tests that a new reading is saved when no existing reading is found.
     */
    @Test
    public void testUpsertLatestReading_noExisting_shouldSave() {
        DynamicReading newReading = new DynamicReading();
        newReading.setLastUpdated(OffsetDateTime.now()); // Current timestamp

        when(repository.findTopBySite_SystemCodeNumberOrderByLastUpdatedDesc("SITE1"))
                .thenReturn(Optional.empty()); // No existing reading
        when(repository.save(newReading)).thenReturn(newReading); // Simulate save

        DynamicReading result = service.upsertLatestReading("SITE1", newReading);

        assertEquals(newReading, result); // Should return the saved reading
        verify(repository).save(newReading); // Save should be called
    }

    /**
     * Tests that a newer reading replaces an older existing one.
     */
    @Test
    public void testUpsertLatestReading_existingOlder_shouldSave() {
        DynamicReading existing = new DynamicReading();
        existing.setLastUpdated(OffsetDateTime.now().minusMinutes(10)); // Older timestamp

        DynamicReading newReading = new DynamicReading();
        newReading.setLastUpdated(OffsetDateTime.now()); // Newer timestamp

        when(repository.findTopBySite_SystemCodeNumberOrderByLastUpdatedDesc("SITE2"))
                .thenReturn(Optional.of(existing)); // Existing reading found
        when(repository.save(newReading)).thenReturn(newReading); // Simulate save

        DynamicReading result = service.upsertLatestReading("SITE2", newReading);

        assertEquals(newReading, result); // Should return the new reading
        verify(repository).save(newReading); // Save should be called
    }

    /**
     * Tests that an older reading does not replace a newer existing one.
     */
    @Test
    public void testUpsertLatestReading_existingNewer_shouldReturnExisting() {
        DynamicReading existing = new DynamicReading();
        existing.setLastUpdated(OffsetDateTime.now()); // Newer timestamp

        DynamicReading newReading = new DynamicReading();
        newReading.setLastUpdated(OffsetDateTime.now().minusMinutes(5)); // Older timestamp

        when(repository.findTopBySite_SystemCodeNumberOrderByLastUpdatedDesc("SITE3"))
                .thenReturn(Optional.of(existing)); // Existing reading found

        DynamicReading result = service.upsertLatestReading("SITE3", newReading);

        assertEquals(existing, result); // Should return the existing reading
        verify(repository, never()).save(any()); // Save should NOT be called
    }
}
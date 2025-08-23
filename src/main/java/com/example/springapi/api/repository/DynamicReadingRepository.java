package com.example.springapi.api.repository;

import com.example.springapi.api.model.DynamicReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for accessing and managing dynamic data
 * entities
 * @author Ross Cochrane
 */

@Repository
public interface DynamicReadingRepository extends JpaRepository<DynamicReading,Long> {

    /**
     * Method to find the most recent pollution data readings
     * for a given site
     */
    Optional<DynamicReading> findTopBySite_SystemCodeNumberOrderByLastUpdatedDesc(String systemCodeNumber);

}

package com.repositories;

import com.linkDatabase.GeoAreas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeoAreasRepository extends JpaRepository<GeoAreas, Long> {
}

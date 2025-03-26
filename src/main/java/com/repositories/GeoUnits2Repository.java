package com.repositories;

import com.linkDatabase.GeoUnits2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeoUnits2Repository extends JpaRepository<GeoUnits2, Long> {
}

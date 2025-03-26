package com.repositories;

import com.linkDatabase.HistRegions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistRegionsRepository extends JpaRepository<HistRegions, Long> {
}

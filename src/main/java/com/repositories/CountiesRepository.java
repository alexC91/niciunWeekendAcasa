package com.repositories;

import com.linkDatabase.Counties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountiesRepository extends JpaRepository<Counties, Long> {
}

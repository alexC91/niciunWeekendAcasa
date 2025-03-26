package com.repositories;

import com.linkDatabase.Origins;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OriginsRepository extends JpaRepository<Origins, Long> {
}

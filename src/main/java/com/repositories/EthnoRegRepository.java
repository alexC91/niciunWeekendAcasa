package com.repositories;

import com.linkDatabase.EthnoReg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EthnoRegRepository extends JpaRepository<EthnoReg, Long> {
}

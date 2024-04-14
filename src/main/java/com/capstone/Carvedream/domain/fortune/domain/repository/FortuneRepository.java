package com.capstone.Carvedream.domain.fortune.domain.repository;

import com.capstone.Carvedream.domain.fortune.domain.Fortune;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FortuneRepository extends JpaRepository<Fortune, Long> {
}

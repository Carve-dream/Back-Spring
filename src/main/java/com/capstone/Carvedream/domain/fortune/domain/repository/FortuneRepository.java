package com.capstone.Carvedream.domain.fortune.domain.repository;

import com.capstone.Carvedream.domain.fortune.domain.Fortune;
import com.capstone.Carvedream.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FortuneRepository extends JpaRepository<Fortune, Long> {

    void deleteAllByUser(User user);

    List<Fortune> findAllByUserAndCreatedDateBetween(User user, LocalDateTime start, LocalDateTime end);
}

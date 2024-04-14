package com.capstone.Carvedream.domain.fortune.domain.repository;

import com.capstone.Carvedream.domain.fortune.domain.Fortune;
import com.capstone.Carvedream.domain.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FortuneRepository extends JpaRepository<Fortune, Long> {

    Page<Fortune> findAllByUser(User user, PageRequest pageRequest);
}

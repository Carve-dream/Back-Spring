package com.capstone.Carvedream.domain.GPT.domain.repository;

import com.capstone.Carvedream.domain.GPT.domain.GPTAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GPTAnswerRepository extends JpaRepository<GPTAnswer, Long> {
}

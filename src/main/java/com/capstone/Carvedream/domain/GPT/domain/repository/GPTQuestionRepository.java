package com.capstone.Carvedream.domain.GPT.domain.repository;

import com.capstone.Carvedream.domain.GPT.domain.GPTQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GPTQuestionRepository extends JpaRepository<GPTQuestion, Long> {
}

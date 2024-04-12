package com.capstone.Carvedream.domain.diary.domain.repository;

import com.capstone.Carvedream.domain.diary.domain.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {
}

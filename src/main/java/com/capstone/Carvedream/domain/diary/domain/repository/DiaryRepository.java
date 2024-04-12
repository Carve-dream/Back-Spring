package com.capstone.Carvedream.domain.diary.domain.repository;

import com.capstone.Carvedream.domain.diary.domain.Diary;
import com.capstone.Carvedream.domain.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {

    Page<Diary> findAllByUser(User user, PageRequest pageRequest);
}

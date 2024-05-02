package com.capstone.Carvedream.domain.diary.domain.repository;

import com.capstone.Carvedream.domain.diary.domain.Diary;
import com.capstone.Carvedream.domain.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import java.util.Optional;
import java.util.Set;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {

    Page<Diary> findAllByUser(User user, PageRequest pageRequest);

    List<Diary> findAllByUserAndDateBetween(User user, LocalDate start, LocalDate end);

    Optional<Diary> findByIdAndUser(Long id, User user);

    List<Diary> findAllByUserAndTagsContaining(User user, String tags);

}

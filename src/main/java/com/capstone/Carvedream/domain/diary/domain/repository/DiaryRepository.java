package com.capstone.Carvedream.domain.diary.domain.repository;

import com.capstone.Carvedream.domain.diary.domain.Diary;
import com.capstone.Carvedream.domain.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import java.util.Optional;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {

    Page<Diary> findAllByUser(User user, PageRequest pageRequest);

    List<Diary> findAllByUserAndDateBetween(User user, LocalDate start, LocalDate end);

    Optional<Diary> findByIdAndUser(Long id, User user);

    List<Diary> findAllByUserAndTagsContaining(User user, String tags);

    void deleteAllByUser(User user);

    List<Diary> findTop5ByUserOrderByIdDesc(User user);

    @Query("SELECT d.interpretation FROM Diary d WHERE d.user = :user AND d.interpretation IS NOT NULL ORDER BY d.id DESC LIMIT 5")
    List<String> findTop5InterpretationsByUserOrderByIdDesc(@Param("user") User user);
}

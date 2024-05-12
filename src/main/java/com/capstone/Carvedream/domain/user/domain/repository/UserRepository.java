package com.capstone.Carvedream.domain.user.domain.repository;

import com.capstone.Carvedream.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    
    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);
}

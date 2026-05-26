package com.pbo.backend.repository;

import com.pbo.backend.model.Trainer;
import com.pbo.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    Optional<Trainer> findByUser(User user);
    Optional<Trainer> findByUserUsername(String username);
}
package com.pbo.backend.repository;

import com.pbo.backend.model.Schedule;
import com.pbo.backend.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByTrainer(Trainer trainer);
    List<Schedule> findByTrainerAndStatus(Trainer trainer, Schedule.Status status);
}
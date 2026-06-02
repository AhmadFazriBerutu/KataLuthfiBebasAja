package com.pbo.backend.service;

import com.pbo.backend.model.Schedule;
import com.pbo.backend.model.Trainer;
import com.pbo.backend.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    public List<Schedule> getSchedulesByTrainer(Trainer trainer) {
        return scheduleRepository.findByTrainer(trainer);
    }

    public List<Schedule> getAvailableSchedules(Trainer trainer) {
        return scheduleRepository.findByTrainerAndStatus(trainer, Schedule.Status.AVAILABLE);
    }

    public Schedule saveSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public Schedule getScheduleById(Long id) {
        return scheduleRepository.findById(id).orElse(null);
    }

    /**
     * PERBAIKAN: Menghapus jadwal dengan validasi status.
     * Hanya jadwal berstatus AVAILABLE yang boleh dihapus oleh Trainer.
     */
    public void deleteSchedule(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Jadwal tidak ditemukan dengan ID: " + id));

        // Memeriksa status menggunakan enum Schedule.Status yang ada di modelmu
        if (schedule.getStatus() != Schedule.Status.AVAILABLE) {
            throw new IllegalStateException("Jadwal tidak dapat dihapus karena sudah dipesan atau tidak aktif!");
        }

        scheduleRepository.delete(schedule);
    }
}
package com.pbo.backend.service;

import com.pbo.backend.model.Trainer;
import com.pbo.backend.model.User;
import com.pbo.backend.repository.TrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainerService {

    @Autowired
    private TrainerRepository trainerRepository;

    public List<Trainer> getAllTrainers() {
        return trainerRepository.findAll();
    }

    public Optional<Trainer> getTrainerById(Long id) {
        return trainerRepository.findById(id);
    }

    public Trainer getTrainerByUser(User user) {
        return trainerRepository.findByUser(user).orElse(null);
    }

    public Trainer saveTrainer(Trainer trainer) {
        return trainerRepository.save(trainer);
    }

    public Trainer getTrainerByUsername(String username) {
        return trainerRepository.findAll().stream()
                .filter(t -> t.getUser() != null && t.getUser().getUsername().equals(username))
                .findFirst().orElse(null);
    }

    public void deleteTrainer(Long id) {
        trainerRepository.deleteById(id);
    }
}
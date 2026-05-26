package com.pbo.backend.service;

import com.pbo.backend.model.Trainer;
import com.pbo.backend.model.User;
import com.pbo.backend.repository.TrainerRepository;
import com.pbo.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TrainerService {

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private UserRepository userRepository;

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
        return trainerRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Trainer tidak ditemukan dengan username: " + username));
    }

    @Transactional
    public void deleteTrainer(Long id) {
        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trainer tidak ditemukan dengan id: " + id));
        User user = trainer.getUser();
        trainerRepository.delete(trainer);
        if (user != null) {
            userRepository.delete(user);
        }
    }
}
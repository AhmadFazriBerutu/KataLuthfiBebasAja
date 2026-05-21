package com.pbo.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "schedules")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    private String hari; // "Senin", "Selasa", dst

    private LocalTime jamMulai;
    private LocalTime jamSelesai;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        AVAILABLE, BOOKED
    }
}
package com.pbo.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "kunjungan")
public class Kunjungan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String namaKunjungan;
    private Double tarifHarian;
    private LocalDate tanggal;
    private LocalTime jamMasuk;
    private String keterangan;

    @PrePersist
    protected void onCreate() {
        if (tanggal == null) tanggal = LocalDate.now();
        if (jamMasuk == null) jamMasuk = LocalTime.now();
    }
}
package com.pbo.backend.repository;

import com.pbo.backend.model.Kunjungan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface KunjunganRepository extends JpaRepository<Kunjungan, Long> {
    List<Kunjungan> findByTanggalOrderByJamMasukDesc(LocalDate tanggal);
    List<Kunjungan> findAllByOrderByTanggalDescJamMasukDesc();
    long countByTanggal(LocalDate tanggal);
}
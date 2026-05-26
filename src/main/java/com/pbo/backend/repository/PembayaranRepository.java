package com.pbo.backend.repository;

import com.pbo.backend.model.Member;
import com.pbo.backend.model.Pembayaran;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PembayaranRepository extends JpaRepository<Pembayaran, Long> {
    Optional<Pembayaran> findByMember(Member member);
    List<Pembayaran> findAllByOrderByTanggalDesc();
}
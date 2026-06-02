package com.pbo.backend.repository;

import com.pbo.backend.model.Member;
import com.pbo.backend.model.Pembayaran;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PembayaranRepository extends JpaRepository<Pembayaran, Long> {
    // Diubah menjadi List menyesuaikan relasi ManyToOne
    List<Pembayaran> findByMember(Member member);

    List<Pembayaran> findAllByOrderByTanggalDesc();

    // Wajib ada untuk mencari transaksi via Webhook / Callback Payment Gateway
    Optional<Pembayaran> findByOrderId(String orderId);
}
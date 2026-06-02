package com.pbo.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "pembayaran")
public class Pembayaran {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Diubah menjadi ManyToOne agar member bisa melakukan pembayaran berkali-kali di masa depan
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    private MetodePembayaran metode;

    private Double nominal;

    @Enumerated(EnumType.STRING)
    private StatusPembayaran status;

    private String keterangan;

    private LocalDateTime tanggal;

    // Tambahan field untuk tracking integrasi payment gateway
    @Column(unique = true)
    private String orderId;

    @Column(length = 500)
    private String snapToken;

    public enum MetodePembayaran {
        CASH, TRANSFER_BANK, QRIS_EWALLET
    }

    public enum StatusPembayaran {
        LUNAS, PENDING, GAGAL
    }

    @PrePersist
    protected void onCreate() {
        tanggal = LocalDateTime.now();
        if (status == null) {
            status = StatusPembayaran.PENDING;
        }
    }
}
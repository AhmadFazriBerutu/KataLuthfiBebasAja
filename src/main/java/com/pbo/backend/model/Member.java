package com.pbo.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List; // 1. Tambahkan import ini di atas jika belum ada

@Getter
@Setter
@Entity
@Table(name = "members")
public class Member extends Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Paket paket;

    private boolean isActive = true;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    // 2. TAMBAHKAN KODE INI DI SINI
    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Pembayaran> pembayaranList;

    public enum Paket {
        BASIC, PREMIUM
    }

    @Override
    public String getRole() {
        return "MEMBER";
    }
}
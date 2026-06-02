package com.pbo.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

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

    // Relasi ke Pembayaran
    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Pembayaran> pembayaranList;

    // Relasi ke PTRequest (Ditambahkan untuk memperbaiki error)
    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<PTRequest> ptRequests;

    public enum Paket {
        BASIC, PREMIUM
    }

    @Override
    public String getRole() {
        return "MEMBER";
    }
}
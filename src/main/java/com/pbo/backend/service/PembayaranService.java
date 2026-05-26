package com.pbo.backend.service;

import com.pbo.backend.model.Member;
import com.pbo.backend.model.Pembayaran;
import com.pbo.backend.repository.PembayaranRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PembayaranService {

    @Autowired
    private PembayaranRepository pembayaranRepository;

    public Pembayaran simpan(Pembayaran pembayaran) {
        return pembayaranRepository.save(pembayaran);
    }

    public List<Pembayaran> getSemuaPembayaran() {
        return pembayaranRepository.findAllByOrderByTanggalDesc();
    }

    public Optional<Pembayaran> getByMember(Member member) {
        return pembayaranRepository.findByMember(member);
    }

    public Optional<Pembayaran> getById(Long id) {
        return pembayaranRepository.findById(id);
    }

    /**
     * Buat dan simpan data pembayaran baru untuk member Premium.
     * Dipanggil dari AdminController saat member Premium ditambah.
     */
    public Pembayaran buatPembayaran(Member member,
                                     String metodeStr,
                                     Double nominal) {
        Pembayaran p = new Pembayaran();
        p.setMember(member);
        p.setNominal(nominal != null ? nominal : 0.0);
        p.setStatus(Pembayaran.StatusPembayaran.LUNAS);

        // Mapping string dari form ke enum
        switch (metodeStr == null ? "" : metodeStr) {
            case "Transfer Bank" -> p.setMetode(Pembayaran.MetodePembayaran.TRANSFER_BANK);
            case "Qris/E-Wallet"  -> p.setMetode(Pembayaran.MetodePembayaran.QRIS_EWALLET);
            default               -> p.setMetode(Pembayaran.MetodePembayaran.CASH);
        }

        return pembayaranRepository.save(p);
    }
}
package com.pbo.backend.service;

import com.pbo.backend.model.Member;
import com.pbo.backend.model.Pembayaran;
import com.pbo.backend.repository.PembayaranRepository;
import com.pbo.backend.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PembayaranService {

    @Autowired
    private PembayaranRepository pembayaranRepository;

    @Autowired
    private MemberRepository memberRepository;

    public Pembayaran simpan(Pembayaran pembayaran) {
        return pembayaranRepository.save(pembayaran);
    }

    public List<Pembayaran> getSemuaPembayaran() {
        return pembayaranRepository.findAllByOrderByTanggalDesc();
    }

    public List<Pembayaran> getByMember(Member member) {
        return pembayaranRepository.findByMember(member);
    }

    public Optional<Pembayaran> getById(Long id) {
        return pembayaranRepository.findById(id);
    }

    /**
     * JALUR MANUAL: Pembayaran langsung LUNAS di kasir (Cash / Transfer Bank fiktif).
     */
    public Pembayaran buatPembayaran(Member member, String metodeStr, Double nominal) {
        Pembayaran p = new Pembayaran();
        p.setMember(member);
        p.setNominal(nominal != null ? nominal : 0.0);
        p.setStatus(Pembayaran.StatusPembayaran.LUNAS);

        switch (metodeStr == null ? "" : metodeStr) {
            case "Transfer Bank" -> p.setMetode(Pembayaran.MetodePembayaran.TRANSFER_BANK);
            case "Qris/E-Wallet"  -> p.setMetode(Pembayaran.MetodePembayaran.QRIS_EWALLET);
            default               -> p.setMetode(Pembayaran.MetodePembayaran.CASH);
        }

        return pembayaranRepository.save(p);
    }

    /**
     * JALUR OTOMATIS: Membuat tagihan online berstatus PENDING di sistem dan generate Token.
     */
    @Transactional
    public Pembayaran buatTagihanOtomatisGateway(Member member, Double nominal) {
        Pembayaran p = new Pembayaran();
        p.setMember(member);
        p.setNominal(nominal != null ? nominal : 0.0);
        p.setStatus(Pembayaran.StatusPembayaran.PENDING);
        p.setMetode(Pembayaran.MetodePembayaran.QRIS_EWALLET);

        String uniqueOrderId = "INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        p.setOrderId(uniqueOrderId);

        // Token dummy untuk kebutuhan demonstrasi awal / testing UI agar tidak null pointer
        p.setSnapToken("DUMMY_TOKEN_" + UUID.randomUUID().toString().substring(0, 5));

        return pembayaranRepository.save(p);
    }

    /**
     * WEBHOOK CALLBACK: Dipanggil otomatis jika sistem luar mendeteksi transaksi berhasil.
     */
    @Transactional
    public void prosesCallbackGateway(String orderId, String statusTransaksiLuar) {
        Pembayaran p = pembayaranRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice tidak ditemukan: " + orderId));

        if (statusTransaksiLuar.equalsIgnoreCase("settlement") || statusTransaksiLuar.equalsIgnoreCase("success")) {
            p.setStatus(Pembayaran.StatusPembayaran.LUNAS);
            pembayaranRepository.save(p);

            Member m = p.getMember();
            m.setPaket(Member.Paket.PREMIUM);
            m.setActive(true);
            memberRepository.save(m);

        } else if (statusTransaksiLuar.equalsIgnoreCase("expire") || statusTransaksiLuar.equalsIgnoreCase("cancel")) {
            p.setStatus(Pembayaran.StatusPembayaran.GAGAL);
            pembayaranRepository.save(p);
        }
    }
}
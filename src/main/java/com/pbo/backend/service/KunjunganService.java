package com.pbo.backend.service;

import com.pbo.backend.model.Kunjungan;
import com.pbo.backend.repository.KunjunganRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class KunjunganService {

    @Autowired
    private KunjunganRepository kunjunganRepository;

    private static final double TARIF_HARIAN = 50000.0;

    public List<Kunjungan> getKunjunganByTanggal(LocalDate tanggal) {
        return kunjunganRepository.findByTanggalOrderByJamMasukDesc(tanggal);
    }

    public List<Kunjungan> getSemuaKunjungan() {
        return kunjunganRepository.findAllByOrderByTanggalDescJamMasukDesc();
    }

    public long countHariIni() {
        return kunjunganRepository.countByTanggal(LocalDate.now());
    }

    public Kunjungan catat(String namaKunjungan, String keterangan) {
        Kunjungan k = new Kunjungan();
        k.setNamaKunjungan(namaKunjungan);
        k.setTarifHarian(TARIF_HARIAN);
        k.setKeterangan(keterangan);
        return kunjunganRepository.save(k);
    }

    public void hapus(Long id) {
        kunjunganRepository.deleteById(id);
    }
}
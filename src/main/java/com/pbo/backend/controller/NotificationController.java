package com.pbo.backend.controller;

import com.pbo.backend.model.Pembayaran;
import com.pbo.backend.repository.PembayaranRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class NotificationController {

    @Autowired
    private PembayaranRepository pembayaranRepository;

    @PostMapping("/notification")
    public ResponseEntity<String> handleNotification(@RequestBody Map<String, Object> body) {
        String orderId = (String) body.get("order_id");
        String transactionStatus = (String) body.get("transaction_status");

        pembayaranRepository.findByOrderId(orderId).ifPresent(p -> {
            if ("settlement".equals(transactionStatus) || "capture".equals(transactionStatus)) {
                p.setStatus(Pembayaran.StatusPembayaran.LUNAS); // Menggunakan Enum
            } else if (Arrays.asList("deny", "expire", "cancel").contains(transactionStatus)) {
                p.setStatus(Pembayaran.StatusPembayaran.GAGAL); // Menggunakan Enum
            }
            pembayaranRepository.save(p);
        });

        return ResponseEntity.ok("OK");
    }
}
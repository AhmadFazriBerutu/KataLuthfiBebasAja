package com.pbo.backend.controller;

import com.pbo.backend.model.Pembayaran;
import com.pbo.backend.repository.PembayaranRepository;
import io.github.cdimascio.dotenv.Dotenv; // Library untuk membaca .env
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    // Mengambil key dari file .env secara aman
    private final Dotenv dotenv = Dotenv.load();
    private final String SERVER_KEY = dotenv.get("MIDTRANS_SERVER_KEY");
    private final String CLIENT_KEY = dotenv.get("MIDTRANS_CLIENT_KEY");

    @Autowired
    private PembayaranRepository pembayaranRepository;

    @GetMapping("/checkout")
    public String checkoutPage(Model model) {
        String url = "https://app.sandbox.midtrans.com/snap/v1/transactions";
        String orderId = "FIT-" + System.currentTimeMillis();

        Map<String, Object> body = new HashMap<>();
        Map<String, Object> transDetails = new HashMap<>();
        transDetails.put("order_id", orderId);
        transDetails.put("gross_amount", 150000);
        body.put("transaction_details", transDetails);

        HttpHeaders headers = new HttpHeaders();
        // Menggunakan SERVER_KEY yang diambil dari .env
        String auth = Base64.getEncoder().encodeToString((SERVER_KEY + ":").getBytes());
        headers.set("Authorization", "Basic " + auth);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();

        try {
            Map<String, Object> response = restTemplate.postForObject(url, request, Map.class);

            // Simpan ke database
            Pembayaran p = new Pembayaran();
            p.setOrderId(orderId);
            p.setNominal(150000.0);
            p.setStatus(Pembayaran.StatusPembayaran.PENDING);
            pembayaranRepository.save(p);

            model.addAttribute("snapToken", response.get("token"));
            model.addAttribute("clientKey", CLIENT_KEY); // Menggunakan CLIENT_KEY dari .env
            model.addAttribute("orderId", orderId);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Gagal menghubungi server Midtrans");
        }
        return "payment/checkout";
    }
}
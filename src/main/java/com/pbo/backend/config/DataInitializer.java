package com.pbo.backend.config;

import com.pbo.backend.model.*;
import com.pbo.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initData(
            UserRepository userRepository,
            MemberRepository memberRepository,
            TrainerRepository trainerRepository,
            CSStaffRepository csStaffRepository,
            ScheduleRepository scheduleRepository
    ) {
        return args -> {
            // Cek kalau data sudah ada, skip
            if (userRepository.count() > 0) return;

            // === ADMIN / CS ===
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("admin123"));
            adminUser.setRole(User.Role.ADMIN);
            userRepository.save(adminUser);

            CSStaff cs = new CSStaff();
            cs.setNama("Admin CS");
            cs.setEmail("admin@gym.com");
            cs.setTelepon("081200000001");
            cs.setUser(adminUser);
            csStaffRepository.save(cs);

            // === TRAINER 1 ===
            User ptUser1 = new User();
            ptUser1.setUsername("budi");
            ptUser1.setPassword(passwordEncoder.encode("budi123"));
            ptUser1.setRole(User.Role.PT);
            userRepository.save(ptUser1);

            Trainer trainer1 = new Trainer();
            trainer1.setNama("Budi Santoso");
            trainer1.setEmail("budi@gym.com");
            trainer1.setTelepon("081200000002");
            trainer1.setSpesialisasi("Cardio & Weight Loss");
            trainer1.setUser(ptUser1);
            trainerRepository.save(trainer1);

            // === TRAINER 2 ===
            User ptUser2 = new User();
            ptUser2.setUsername("sari");
            ptUser2.setPassword(passwordEncoder.encode("sari123"));
            ptUser2.setRole(User.Role.PT);
            userRepository.save(ptUser2);

            Trainer trainer2 = new Trainer();
            trainer2.setNama("Sari Dewi");
            trainer2.setEmail("sari@gym.com");
            trainer2.setTelepon("081200000003");
            trainer2.setSpesialisasi("Strength & Muscle Building");
            trainer2.setUser(ptUser2);
            trainerRepository.save(trainer2);

            // === MEMBER 1 (PREMIUM) ===
            User memberUser1 = new User();
            memberUser1.setUsername("andi");
            memberUser1.setPassword(passwordEncoder.encode("andi123"));
            memberUser1.setRole(User.Role.MEMBER);
            userRepository.save(memberUser1);

            Member member1 = new Member();
            member1.setNama("Andi Pratama");
            member1.setEmail("andi@gmail.com");
            member1.setTelepon("081200000004");
            member1.setPaket(Member.Paket.PREMIUM);
            member1.setUser(memberUser1);
            memberRepository.save(member1);

            // === MEMBER 2 (BASIC) ===
            User memberUser2 = new User();
            memberUser2.setUsername("siti");
            memberUser2.setPassword(passwordEncoder.encode("siti123"));
            memberUser2.setRole(User.Role.MEMBER);
            userRepository.save(memberUser2);

            Member member2 = new Member();
            member2.setNama("Siti Rahma");
            member2.setEmail("siti@gmail.com");
            member2.setTelepon("081200000005");
            member2.setPaket(Member.Paket.BASIC);
            member2.setUser(memberUser2);
            memberRepository.save(member2);

            // === JADWAL TRAINER 1 ===
            String[] hari = {"Senin", "Selasa", "Rabu", "Kamis", "Jumat"};
            for (String h : hari) {
                Schedule s = new Schedule();
                s.setTrainer(trainer1);
                s.setHari(h);
                s.setJamMulai(java.time.LocalTime.of(8, 0));
                s.setJamSelesai(java.time.LocalTime.of(9, 0));
                s.setStatus(Schedule.Status.AVAILABLE);
                scheduleRepository.save(s);
            }

            // === JADWAL TRAINER 2 ===
            for (String h : hari) {
                Schedule s = new Schedule();
                s.setTrainer(trainer2);
                s.setHari(h);
                s.setJamMulai(java.time.LocalTime.of(10, 0));
                s.setJamSelesai(java.time.LocalTime.of(11, 0));
                s.setStatus(Schedule.Status.AVAILABLE);
                scheduleRepository.save(s);
            }

            System.out.println("=== Data dummy berhasil dibuat! ===");
            System.out.println("Login: admin/admin123 | budi/budi123 | sari/sari123 | andi/andi123 | siti/siti123");
        };
    }
}
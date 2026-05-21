package com.pbo.backend.repository;

import com.pbo.backend.model.CSStaff;
import com.pbo.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CSStaffRepository extends JpaRepository<CSStaff, Long> {
    Optional<CSStaff> findByUser(User user);
}
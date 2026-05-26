package com.pbo.backend.repository;

import com.pbo.backend.model.Member;
import com.pbo.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUser(User user);
    Optional<Member> findByUserUsername(String username);
}
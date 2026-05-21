package com.pbo.backend.repository;

import com.pbo.backend.model.Member;
import com.pbo.backend.model.PTRequest;
import com.pbo.backend.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PTRequestRepository extends JpaRepository<PTRequest, Long> {
    List<PTRequest> findByMember(Member member);
    List<PTRequest> findByTrainer(Trainer trainer);
    List<PTRequest> findByStatus(PTRequest.Status status);
}
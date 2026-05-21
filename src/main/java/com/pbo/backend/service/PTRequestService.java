package com.pbo.backend.service;

import com.pbo.backend.model.*;
import com.pbo.backend.repository.PTRequestRepository;
import com.pbo.backend.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PTRequestService {

    @Autowired
    private PTRequestRepository ptRequestRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    public List<PTRequest> getAllRequests() {
        return ptRequestRepository.findAll();
    }

    public List<PTRequest> getPendingRequests() {
        return ptRequestRepository.findByStatus(PTRequest.Status.PENDING);
    }

    public List<PTRequest> getRequestsByMember(Member member) {
        return ptRequestRepository.findByMember(member);
    }

    public List<PTRequest> getRequestsByTrainer(Trainer trainer) {
        return ptRequestRepository.findByTrainer(trainer);
    }

    public PTRequest saveRequest(PTRequest request) {
        return ptRequestRepository.save(request);
    }

    public PTRequest approveRequest(Long id, String catatan) {
        PTRequest request = ptRequestRepository.findById(id).orElseThrow();
        request.setStatus(PTRequest.Status.APPROVED);
        request.setCatatanCs(catatan);

        // Jadwal otomatis jadi BOOKED
        Schedule schedule = request.getSchedule();
        schedule.setStatus(Schedule.Status.BOOKED);
        scheduleRepository.save(schedule);

        return ptRequestRepository.save(request);
    }

    public PTRequest rejectRequest(Long id, String catatan) {
        PTRequest request = ptRequestRepository.findById(id).orElseThrow();
        request.setStatus(PTRequest.Status.REJECTED);
        request.setCatatanCs(catatan);
        return ptRequestRepository.save(request);
    }

    public List<PTRequest> getApprovedMembersByTrainer(Trainer trainer) {
        return ptRequestRepository.findByTrainer(trainer).stream()
                .filter(r -> r.getStatus() == PTRequest.Status.APPROVED)
                .collect(java.util.stream.Collectors.toList());
    }
}
package com.pbo.backend.service;

import com.pbo.backend.model.Member;
import com.pbo.backend.model.User;
import com.pbo.backend.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Optional<Member> getMemberById(Long id) {
        return memberRepository.findById(id);
    }

    public Member getMemberByUser(User user) {
        return memberRepository.findByUser(user).orElse(null);
    }

    public Member saveMember(Member member) {
        return memberRepository.save(member);
    }

    public Member getMemberByUsername(String username) {
        return memberRepository.findAll().stream()
                .filter(m -> m.getUser() != null && m.getUser().getUsername().equals(username))
                .findFirst().orElse(null);
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }
}
package com.pbo.backend.service;

import com.pbo.backend.model.Member;
import com.pbo.backend.model.User;
import com.pbo.backend.repository.MemberRepository;
import com.pbo.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private UserRepository userRepository;

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
        return memberRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Member tidak ditemukan dengan username: " + username));
    }

    @Transactional
    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member tidak ditemukan dengan id: " + id));
        User user = member.getUser();
        memberRepository.delete(member);
        if (user != null) {
            userRepository.delete(user);
        }
    }
}
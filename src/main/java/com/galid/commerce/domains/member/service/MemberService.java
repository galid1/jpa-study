package com.galid.commerce.domains.member.service;

import com.galid.commerce.common.value.Address;
import com.galid.commerce.domains.member.domain.MemberEntity;
import com.galid.commerce.domains.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;

    public Long signUp(SignUpRequest request) {
        validateDuplicateMember(request.getAuthId());

        MemberEntity newMember = MemberEntity.builder()
                .address(new Address(request.getCity(), request.getStreet()))
                .authId(request.getAuthId())
                .authPw(encoder.encode(request.getPassword()))
                .build();

        return memberRepository.save(newMember).getMemberId();
    }

    private void validateDuplicateMember(String authId) {
        Optional<MemberEntity> findMember = memberRepository.findFirstByAuthId(authId);

        if(findMember.isPresent())
            throw new IllegalStateException("이미 존재하는 회원입니다.");
    }

    private MemberEntity validateExistMember(Optional<MemberEntity> memberEntity) {
        if(!memberEntity.isPresent())
            throw new IllegalStateException("존재하지 않는 유저입니다.");
        return memberEntity.get();
    }

    public MemberEntity findMember(Long memberId) {
        MemberEntity memberEntity = validateExistMember(memberRepository.findById(memberId));

        return memberEntity;
    }

    public List<MemberEntity> findAllMember() {
        return memberRepository.findAll();
    }
}

//    public MemberDto findMember(Long memberId) {
//        MemberEntity memberEntity = validateExistMember(memberRepository.findById(memberId));
//
//        return toMemberDto(memberEntity);
//    }
//
//    public List<MemberDto> findAllMember() {
//        return memberRepository.findAll().stream()
//                .map(member -> toMemberDto(member))
//                .collect(Collectors.toList());
//    }
//    private MemberDto toMemberDto(MemberEntity memberEntity) {
//        return MemberDto.builder()
//                .id(memberEntity.getAuthId())
//                .address(toAddress(memberEntity.getAddress()))
//                .build();
//    }
//
//    private AddressDto toAddress(Address address) {
//        return AddressDto.builder()
//                .street(address.getStreet())
//                .city(address.getCity())
//                .build();
//    }

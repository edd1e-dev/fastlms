package com.zerobase.fastlms.member.service;

import com.zerobase.fastlms.admin.dto.MemberDto;
import com.zerobase.fastlms.admin.mapper.MemberMapper;
import com.zerobase.fastlms.admin.model.MemberParam;
import com.zerobase.fastlms.component.MailComponents;
import com.zerobase.fastlms.course.model.ServiceResult;
import com.zerobase.fastlms.admin.entity.LoginHistory;
import com.zerobase.fastlms.member.entity.Member;
import com.zerobase.fastlms.member.entity.MemberCode;
import com.zerobase.fastlms.member.exception.MemberBannedException;
import com.zerobase.fastlms.member.exception.MemberEmailVerifyFailException;
import com.zerobase.fastlms.member.model.MemberInput;
import com.zerobase.fastlms.member.model.ResetPasswordInput;
import com.zerobase.fastlms.admin.repository.LoginHistoryRepository;
import com.zerobase.fastlms.member.repository.MemberRepository;
import com.zerobase.fastlms.util.PasswordUtil;
import com.zerobase.fastlms.util.RequestUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MailComponents mailComponents;
    private final MemberMapper memberMapper;
    private final LoginHistoryRepository loginHistoryRepository;
    private final HttpServletRequest request;

    @Override
    public boolean register(MemberInput memberInput) {
        Optional<Member> optionalMember = memberRepository.findById(memberInput.getUserId());
        if (optionalMember.isPresent()) {
            return false;
        }

        String encryptedPassword =
                PasswordUtil.encryptPassword(memberInput.getPassword());

        String uuid = UUID.randomUUID().toString();

        Member member = Member.builder()
                .userId(memberInput.getUserId())
                .userName(memberInput.getUserName())
                .phone(memberInput.getPhone())
                .password(encryptedPassword)
                .regDt(LocalDateTime.now())
                .emailAuthYn(false)
                .emailAuthKey(uuid)
                .userStatus(MemberCode.MEMBER_STATUS_AUTH_WAIT)
                .build();
        memberRepository.save(member);

        String email = memberInput.getUserId();
        String subject = "fastlms ????????? ????????? ??????????????????.";
        String text = "<p>fastlms ????????? ????????? ??????????????????.</p>"
                + "<p>?????? ????????? ??????????????? ????????? ?????? ?????????.</p>"
                + "<div><a target='_blank' href='http://localhost:8080/member/email-auth?id=" + uuid + "'> ?????? ?????? </a></div>";
        mailComponents.sendMail(email, subject, text);

        return true;
    }

    @Override
    public boolean emailAuth(String uuid) {
        Optional<Member> optionalMember = memberRepository.findByEmailAuthKey(uuid);
        if (!optionalMember.isPresent()) {
            return false;
        }

        Member member = optionalMember.get();

        if (!MemberCode.MEMBER_STATUS_AUTH_WAIT.equals(member.getUserStatus())) {
            return false;
        }

        member.setEmailAuthYn(true);
        member.setEmailAuthDt(LocalDateTime.now());
        member.setUserStatus(MemberCode.MEMBER_STATUS_IN_USE);
        memberRepository.save(member);

        return true;
    }

    @Override
    public boolean sendResetPassword(ResetPasswordInput.Request resetPasswordDto) {
        Optional<Member> optionalMember = memberRepository.findByUserIdAndUserName(
                resetPasswordDto.getUserId(),
                resetPasswordDto.getUserName()
        );

        if (!optionalMember.isPresent()) {
            throw new UsernameNotFoundException("?????? ????????? ???????????? ????????????.");
        }

        Member member = optionalMember.get();
        String uuid = UUID.randomUUID().toString();

        member.setResetPasswordKey(uuid);
        member.setResetPasswordLimitDt(LocalDateTime.now().plusDays(1));
        memberRepository.save(member);

        String email = resetPasswordDto.getUserId();
        String subject = "[fastlms] ???????????? ????????? ?????? ?????????.";
        String text = "<p>fastlms ???????????? ????????? ?????? ?????????.</p>"
                + "<p>?????? ????????? ??????????????? ??????????????? ????????? ????????????.</p>"
                + "<div><a target='_blank' href='http://localhost:8080/member/reset/password?id=" + uuid + "'> ???????????? ????????? ?????? </a></div>";
        mailComponents.sendMail(email, subject, text);

        return true;
    }

    @Override
    public boolean resetPassword(String uuid, String password) {
        Optional<Member> optionalMember = memberRepository.findByResetPasswordKey(uuid);
        if (!optionalMember.isPresent()) {
            throw new UsernameNotFoundException("?????? ????????? ???????????? ????????????.");
        }

        Member member = optionalMember.get();

        if (member.getResetPasswordLimitDt() == null) {
            throw new RuntimeException("????????? ????????? ????????????.");
        }

        if (member.getResetPasswordLimitDt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("????????? ????????? ????????????.");
        }

        String encryptedPassword = PasswordUtil.encryptPassword(password);
        member.setPassword(encryptedPassword);
        member.setResetPasswordKey("");
        member.setResetPasswordLimitDt(null);
        memberRepository.save(member);

        return true;
    }

    @Override
    public boolean checkResetPassword(String uuid) {
        Optional<Member> optionalMember = memberRepository.findByResetPasswordKey(uuid);
        if (!optionalMember.isPresent()) {
            return false;
        }

        Member member = optionalMember.get();

        if (member.getResetPasswordLimitDt() == null) {
            throw new RuntimeException("????????? ????????? ????????????.");
        }

        if (member.getResetPasswordLimitDt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("????????? ????????? ????????????.");
        }

        return true;
    }

    @Override
    public List<MemberDto> getList(MemberParam memberParam) {
        long totalCount = memberMapper.selectListCount(memberParam);
        List<MemberDto> list = memberMapper.selectList(memberParam);

        if (!CollectionUtils.isEmpty(list)) {
            int i = 0;
            for (MemberDto x : list) {
                x.setTotalCount(totalCount);
                x.setSeq(totalCount - memberParam.getPageStart() - i);
                i++;
            }
        }

        return list;
    }

    @Override
    public MemberDto getDetail(String userId) {
        Optional<Member> optionalMember = memberRepository.findById(userId);

        if (!optionalMember.isPresent()) {
            return null;
        }

        Member member = optionalMember.get();

        return MemberDto.of(member);
    }

    @Override
    public boolean updateStatus(String userId, String userStatus) {
        Optional<Member> optionalMember = memberRepository.findById(userId);
        if (!optionalMember.isPresent()) {
            throw new UsernameNotFoundException("?????? ????????? ???????????? ????????????.");
        }

        Member member = optionalMember.get();
        member.setUserStatus(userStatus);
        memberRepository.save(member);

        return true;
    }

    @Override
    public boolean updatePassword(String userId, String password) {
        Optional<Member> optionalMember = memberRepository.findById(userId);
        if (!optionalMember.isPresent()) {
            throw new UsernameNotFoundException("?????? ????????? ???????????? ????????????.");
        }

        String encryptedPassword = PasswordUtil.encryptPassword(password);

        Member member = optionalMember.get();
        member.setPassword(encryptedPassword);
        memberRepository.save(member);

        return true;
    }

    @Override
    public ServiceResult updateMember(MemberInput memberInput) {
        String userId = memberInput.getUserId();
        Optional<Member> optionalMember = memberRepository.findById(userId);
        if (!optionalMember.isPresent()) {
            return new ServiceResult(false, "?????? ????????? ???????????? ????????????.");
        }

        Member member = optionalMember.get();
        member.setPhone(memberInput.getPhone());
        member.setZipcode(memberInput.getZipcode());
        member.setAddr(memberInput.getAddr());
        member.setAddrDetail(memberInput.getAddrDetail());
        member.setUdtDt(LocalDateTime.now());
        memberRepository.save(member);

        return new ServiceResult();
    }

    @Override
    public ServiceResult updateMemberPassword(MemberInput memberInput) {
        String userId = memberInput.getUserId();
        Optional<Member> optionalMember = memberRepository.findById(userId);
        if (!optionalMember.isPresent()) {
            return new ServiceResult(false, "?????? ????????? ???????????? ????????????.");
        }

        Member member = optionalMember.get();

        if (!PasswordUtil.equals(memberInput.getPassword(), member.getPassword())) {
            return new ServiceResult(false, "??????????????? ???????????? ????????????.");
        }

        String encryptedPassword =
                PasswordUtil.encryptPassword(memberInput.getNewPassword());

        member.setPassword(encryptedPassword);
        memberRepository.save(member);

        return new ServiceResult(true);
    }

    @Override
    public ServiceResult withdraw(String userId, String password) {
        Optional<Member> optionalMember = memberRepository.findById(userId);
        if (!optionalMember.isPresent()) {
            return new ServiceResult(false, "?????? ????????? ???????????? ????????????.");
        }

        Member member = optionalMember.get();

        if (!PasswordUtil.equals(password, member.getPassword())) {
            return new ServiceResult(false, "??????????????? ???????????? ????????????.");
        }

        member.setUserName("????????????");
        member.setPhone("");
        member.setPassword("");
        member.setRegDt(null);
        member.setUdtDt(null);
        member.setEmailAuthYn(false);
        member.setEmailAuthDt(null);
        member.setEmailAuthKey("");
        member.setResetPasswordKey("");
        member.setResetPasswordLimitDt(null);
        member.setUserStatus(MemberCode.MEMBER_STATUS_WITHDRAW);
        member.setZipcode("");
        member.setAddr("");
        member.setAddrDetail("");
        memberRepository.save(member);

        return new ServiceResult();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> optionalMember = memberRepository.findById(username);
        if (!optionalMember.isPresent()) {
            throw new UsernameNotFoundException("?????? ????????? ???????????? ????????????.");
        }

        Member member = optionalMember.get();

        if (MemberCode.MEMBER_STATUS_AUTH_WAIT.equals(member.getUserStatus())) {
            throw new MemberEmailVerifyFailException("???????????? ????????? ????????? ???????????? ????????????.");
        }

        if (MemberCode.MEMBER_STATUS_BANNED.equals(member.getUserStatus())) {
            throw new MemberBannedException("????????? ???????????????.");
        }

        if (MemberCode.MEMBER_STATUS_WITHDRAW.equals(member.getUserStatus())) {
            throw new MemberBannedException("????????? ???????????????.");
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>(
                Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));

        if (member.isAdminYn()) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        member.setLastLoginDt(LocalDateTime.now());
        memberRepository.save(member);

        LoginHistory loginHistory = LoginHistory.builder()
                .userId(member.getUserId())
                .loginDt(LocalDateTime.now())
                .ip(RequestUtil.getClientIpAddress())
                .userAgent(RequestUtil.getUserAgent())
                .build();
        loginHistoryRepository.save(loginHistory);

        return new User(member.getUserId(), member.getPassword(), grantedAuthorities);
    }


}

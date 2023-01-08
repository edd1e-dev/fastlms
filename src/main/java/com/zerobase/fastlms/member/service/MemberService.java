package com.zerobase.fastlms.member.service;

import com.zerobase.fastlms.admin.dto.MemberDto;
import com.zerobase.fastlms.admin.model.MemberParam;
import com.zerobase.fastlms.course.model.ServiceResult;
import com.zerobase.fastlms.member.model.MemberInput;
import com.zerobase.fastlms.member.model.ResetPasswordInput;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface MemberService extends UserDetailsService {
    boolean register(MemberInput memberDto);
    boolean emailAuth(String uuid);
    boolean sendResetPassword(ResetPasswordInput.Request resetPasswordDto);
    boolean resetPassword(String uuid, String password);

    boolean checkResetPassword(String uuid);
    List<MemberDto> getList(MemberParam memberParam);

    MemberDto getDetail(String userId);

    boolean updateStatus(String userId, String userStatus);

    boolean updatePassword(String userId, String password);
    ServiceResult updateMember(MemberInput memberInput);
    ServiceResult updateMemberPassword(MemberInput memberInput);
    ServiceResult withdraw(String userId, String password);
}

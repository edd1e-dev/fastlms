package com.zerobase.fastlms.member.controller;

import com.zerobase.fastlms.admin.dto.MemberDto;
import com.zerobase.fastlms.course.dto.RegisterCourseDto;
import com.zerobase.fastlms.course.model.RegisterCourseParam;
import com.zerobase.fastlms.course.model.ServiceResult;
import com.zerobase.fastlms.course.service.RegisterCourseService;
import com.zerobase.fastlms.member.model.MemberInput;
import com.zerobase.fastlms.member.model.ResetPasswordInput;
import com.zerobase.fastlms.member.service.MemberService;
import com.zerobase.fastlms.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final RegisterCourseService registerCourseService;

    @GetMapping("/member/register")
    public String register() {
        return "member/register";
    }

    @RequestMapping("/member/login")
    public String login() {
        return "member/login";
    }

    @GetMapping("/member/find/password")
    public String findPassword() {
        return "member/find_password";
    }

    @PostMapping("/member/find/password")
    public String findPasswordSubmit(Model model, ResetPasswordInput.Request resetPasswordDto) {
        boolean result = false;
        try {
            result = memberService.sendResetPassword(resetPasswordDto);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        model.addAttribute("result", result);

        return "member/find_password_result";
    }

    @GetMapping("/member/reset/password")
    public String resetPassword(Model model, HttpServletRequest request) {

        String uuid = request.getParameter("id");
        boolean result = memberService.checkResetPassword(uuid);
        model.addAttribute("result", result);

        return "member/reset_password";
    }

    @PostMapping("/member/reset/password")
    public String resetPasswordSubmit(Model model, ResetPasswordInput.Request resetPasswordDto) {
        boolean result = false;
        try {
            result = memberService.resetPassword(
                    resetPasswordDto.getId(),
                    resetPasswordDto.getPassword()
            );
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        model.addAttribute("result", result);

        return "member/reset_password_result";
    }

    @PostMapping("/member/register")
    public String registerSubmit(Model model, MemberInput memberInput) {

        boolean result = memberService.register(memberInput);
        model.addAttribute("result", result);

        return "member/register_complete";
    }

    @GetMapping("/member/email-auth")
    public String emailAuth(Model model, @RequestParam(value = "id") String id) {
        boolean result = memberService.emailAuth(id);
        model.addAttribute("result", result);
        return "member/email_auth";
    }

    @GetMapping("/member/info")
    public String memberInfo(Model model, Principal principal) {
        MemberDto memberDto = memberService.getDetail(principal.getName());
        model.addAttribute("detail", memberDto);
        return "member/info";
    }

    @PostMapping("/member/info")
    public String memberInfoSubmit(Model model, MemberInput memberInput, Principal principal) {

        String userId = principal.getName();
        memberInput.setUserId(userId);

        ServiceResult result = memberService.updateMember(memberInput);
        if (!result.isResult()) {
            model.addAttribute("message", result.getMessage());
            return "common/error";
        }

        return "redirect:/member/info";
    }

    @GetMapping("/member/password")
    public String memberPassword(Model model, Principal principal) {
        MemberDto memberDto = memberService.getDetail(principal.getName());
        model.addAttribute("detail", memberDto);
        return "member/password";
    }

    @PostMapping("/member/password")
    public String memberPasswordSubmit(Model model, MemberInput memberInput, Principal principal) {

        memberInput.setUserId(principal.getName());

        ServiceResult result = memberService.updateMemberPassword(memberInput);
        if (!result.isResult()) {
            model.addAttribute("message", result.getMessage());
            return "common/error";
        }

        return "redirect:/member/info";
    }

    @GetMapping("/member/register-course")
    public String memberRegisterCourse(Model model, Principal principal) {

        String userId = principal.getName();
        List<RegisterCourseDto> myCourse = registerCourseService.getMyCourse(userId);

        model.addAttribute("list", myCourse);
        return "member/register-course";
    }

    @GetMapping("/member/withdraw")
    public String memberWithDraw() {
        return "member/withdraw";
    }

    @PostMapping("/member/withdraw")
    public String memberWithDrawSubmit(Principal principal,
                                       Model model,
                                       MemberInput memberInput
    ) {
        String userId = principal.getName();
        memberInput.setUserId(userId);

        ServiceResult result = memberService.withdraw(userId, memberInput.getPassword());

        if (!result.isResult()) {
            model.addAttribute("message", result.getMessage());
            return "common/error";
        }

        return "redirect:/member/logout";
    }
}

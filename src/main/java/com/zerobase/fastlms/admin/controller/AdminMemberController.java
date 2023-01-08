package com.zerobase.fastlms.admin.controller;

import com.zerobase.fastlms.admin.dto.MemberDto;
import com.zerobase.fastlms.admin.model.MemberParam;
import com.zerobase.fastlms.admin.model.MemberInput;
import com.zerobase.fastlms.course.controller.BaseController;
import com.zerobase.fastlms.admin.dto.LoginHistoryDto;
import com.zerobase.fastlms.admin.model.LoginHistoryParam;
import com.zerobase.fastlms.admin.service.LoginHistoryService;
import com.zerobase.fastlms.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminMemberController extends BaseController {

    private final MemberService memberService;
    private final LoginHistoryService loginHistoryService;

    @GetMapping("/admin/member/list")
    public String list(Model model, MemberParam memberParam) {
        memberParam.init();

        List<MemberDto> memberList = memberService.getList(memberParam);
        model.addAttribute("list", memberList);

        long totalCount = 0;
        if (!CollectionUtils.isEmpty(memberList)) {
            totalCount = memberList.get(0).getTotalCount();
        }

        String queryString = memberParam.getQueryString();
        String pagerHtml = getPagerHtml(totalCount, memberParam.getPageSize(), memberParam.getPageIndex(), queryString, "");

        model.addAttribute("totalCount", totalCount);
        model.addAttribute("pager", pagerHtml);

        return "admin/member/list";
    }

    @GetMapping("/admin/member/detail")
    public String detail(Model model, MemberParam memberParam) {
        memberParam.init();

        String userId = memberParam.getUserId();

        MemberDto member = memberService.getDetail(userId);
        model.addAttribute("member", member);

        List<LoginHistoryDto> loginHistoryList = loginHistoryService.getMyHistory(memberParam);
        model.addAttribute("list", loginHistoryList);

        long totalCount = 0;
        if (!CollectionUtils.isEmpty(loginHistoryList)) {
            totalCount = loginHistoryList.get(0).getTotalCount();
        }

        String queryString = memberParam.getQueryString();
        String pagerHtml = getPagerHtml(totalCount, memberParam.getPageSize(), memberParam.getPageIndex(), queryString, "&userId=" + userId);

        model.addAttribute("pager", pagerHtml);

        return "admin/member/detail";
    }

    @PostMapping("/admin/member/status")
    public String status(Model model, MemberInput memberInput) {
        boolean result =
                memberService.updateStatus(memberInput.getUserId(), memberInput.getUserStatus());

        return "redirect:/admin/member/detail?userId=" + memberInput.getUserId();
    }

    @PostMapping("/admin/member/password")
    public String password(Model model, MemberInput memberInput) {
        boolean result =
                memberService.updatePassword(memberInput.getUserId(), memberInput.getPassword());

        return "redirect:/admin/member/detail?userId=" + memberInput.getUserId();
    }
}

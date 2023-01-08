package com.zerobase.fastlms.main.controller;

import com.zerobase.fastlms.banner.dto.BannerDto;
import com.zerobase.fastlms.banner.model.BannerParam;
import com.zerobase.fastlms.banner.service.BannerService;
import com.zerobase.fastlms.component.MailComponents;
import com.zerobase.fastlms.main.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final MailComponents mailComponents;
    private final BannerService bannerService;

    @RequestMapping("/")
    public String index(Model model, BannerParam bannerParam) {
        List<BannerDto> bannerList = bannerService.getAllList(bannerParam);
        model.addAttribute("list", bannerList);
        return "index";
    }

    @GetMapping("/error/denied")
    public String errorDenied() {
        return "error/denied";
    }
}

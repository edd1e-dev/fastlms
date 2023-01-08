package com.zerobase.fastlms.banner.controller;

import com.zerobase.fastlms.banner.type.BannerTargetType;
import com.zerobase.fastlms.banner.dto.BannerDto;
import com.zerobase.fastlms.banner.model.BannerInput;
import com.zerobase.fastlms.banner.model.BannerParam;
import com.zerobase.fastlms.banner.service.BannerService;
import com.zerobase.fastlms.course.controller.BaseController;
import com.zerobase.fastlms.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AdminBannerController extends BaseController {
    private final BannerService bannerService;

    @GetMapping("/admin/banner/list")
    public String list(Model model, BannerParam bannerParam) {
        bannerParam.init();

        List<BannerDto> bannerList = bannerService.getList(bannerParam);
        model.addAttribute("list", bannerList);

        long totalCount = 0;
        if (!CollectionUtils.isEmpty(bannerList)) {
            totalCount = bannerList.get(0).getTotalCount();
        }

        String queryString = bannerParam.getQueryString();
        String pagerHtml = getPagerHtml(totalCount, bannerParam.getPageSize(), bannerParam.getPageIndex(), queryString, "");

        model.addAttribute("totalCount", totalCount);
        model.addAttribute("pager", pagerHtml);

        return "admin/banner/list";
    }

    @GetMapping(value = {"/admin/banner/add", "/admin/banner/edit"})
    public String add(Model model,
                      HttpServletRequest request,
                      BannerInput bannerInput
    ) {

        List<String> targetTypeList = BannerTargetType.getList();
        model.addAttribute("targetType", targetTypeList);

        boolean editMode = request.getRequestURI().contains("/edit");

        BannerDto detail = new BannerDto();

        if (editMode) {
            long id = bannerInput.getId();
            BannerDto existBanner = bannerService.getById(id);
            if (existBanner == null) {
                // error 처리
                model.addAttribute("message", "배너 정보가 존재하지 않습니다.");
                return "common/error";
            }
            detail = existBanner;
        }

        model.addAttribute("editMode", editMode);
        model.addAttribute("detail", detail);

        return "admin/banner/add";
    }

    @PostMapping(value = {"/admin/banner/add", "/admin/banner/edit"})
    public String addSubmit(Model model,
                            HttpServletRequest request,
                            @RequestParam("file") MultipartFile multipartFile,
                            BannerInput bannerInput
    ) {
        if (bannerInput.getIsPublic().equals("true")) {
            bannerInput.set_isPublic(true);
        } else {
            bannerInput.set_isPublic(false);
        }

        String saveFilename = "";
        String urlFilename = "";

        if (multipartFile != null) {
            String originalFilename = multipartFile.getOriginalFilename();
            String baseLocalPath = "C:/Users/cudy_/Desktop/project1/fastlms/files";
            String baseUrlPath = "/files";

            String[] arrFilename = FileUtil.getNewSaveFile(baseLocalPath, baseUrlPath, originalFilename);

            saveFilename = arrFilename[0];
            urlFilename = arrFilename[1];

            try {
                File newFile = new File(saveFilename);
                FileCopyUtils.copy(multipartFile.getInputStream(), new FileOutputStream(newFile));
            } catch (IOException e) {
                log.info(e.getMessage());
            }
        }

        bannerInput.setFilename(saveFilename);
        bannerInput.setUrlFilename(urlFilename);

        boolean editMode = request.getRequestURI().contains("/edit");

        if (editMode) {
            long id = bannerInput.getId();
            BannerDto existBanner = bannerService.getById(id);
            if (existBanner == null) {
                // error 처리
                model.addAttribute("message", "배너 정보가 존재하지 않습니다.");
                return "common/error";
            }

            boolean result = bannerService.set(bannerInput);
        } else {
            boolean result = bannerService.add(bannerInput);
        }

        return "redirect:/admin/banner/list";
    }

    @PostMapping("/admin/banner/delete")
    public String del(BannerInput bannerInput) {

        boolean result = bannerService.delete(bannerInput.getIdList());

        return "redirect:/admin/banner/list";
    }
}

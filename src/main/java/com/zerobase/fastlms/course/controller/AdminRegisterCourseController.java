package com.zerobase.fastlms.course.controller;


import com.zerobase.fastlms.course.dto.CourseDto;
import com.zerobase.fastlms.course.dto.RegisterCourseDto;
import com.zerobase.fastlms.course.model.RegisterCourseParam;
import com.zerobase.fastlms.course.model.ServiceResult;
import com.zerobase.fastlms.course.service.CourseService;
import com.zerobase.fastlms.course.service.RegisterCourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class AdminRegisterCourseController extends BaseController {

    private final CourseService courseService;
    private final RegisterCourseService registerCourseService;

    @GetMapping("/admin/register-course/list")
    public String list(Model model,
                       RegisterCourseParam registerCourseParam,
                       BindingResult bindingResult
    ) {

        registerCourseParam.init();
        List<RegisterCourseDto> list = registerCourseService.getList(registerCourseParam);

        long totalCount = 0;
        if (!CollectionUtils.isEmpty(list)) {
            totalCount = list.get(0).getTotalCount();
        }
        String queryString = registerCourseParam.getQueryString();
        String pagerHtml = getPagerHtml(
                totalCount,
                registerCourseParam.getPageSize(),
                registerCourseParam.getPageIndex(),
                queryString,
                "");

        model.addAttribute("list", list);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("pager", pagerHtml);


        List<CourseDto> courseList = courseService.getAllList();
        model.addAttribute("courseList", courseList);

        return "admin/register-course/list";
    }

    @PostMapping("/admin/register-course/status")
    public String status(Model model, RegisterCourseParam registerCourseParam) {

        ServiceResult result = registerCourseService.updateStatus(
                registerCourseParam.getId(),
                registerCourseParam.getStatus());

        if (!result.isResult()) {
            model.addAttribute("message", result.getMessage());
            return "common/error";
        }

        return "redirect:/admin/register-course/list";
    }
}

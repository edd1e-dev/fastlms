package com.zerobase.fastlms.course.controller;

import com.zerobase.fastlms.admin.dto.CategoryDto;
import com.zerobase.fastlms.admin.service.CategoryService;
import com.zerobase.fastlms.course.dto.CourseDto;
import com.zerobase.fastlms.course.model.CourseParam;
import com.zerobase.fastlms.course.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;
    private final CategoryService categoryService;

    @GetMapping("/course")
    public String course(Model model,
                         CourseParam courseParam
    ) {

        List<CourseDto> list = courseService.getFrontList(courseParam);
        model.addAttribute("list", list);

        int courseTotalCount = 0;
        List<CategoryDto> categoryList =
                categoryService.getFrontList(CategoryDto.builder().build());
        if (categoryList != null) {
            for(CategoryDto x : categoryList) {
                courseTotalCount += x.getCourseCount();
            }
        }

        model.addAttribute("categoryList", categoryList);
        model.addAttribute("courseTotalCount", courseTotalCount);

        return "course/index";
    }

    @GetMapping("/course/{id}")
    public String courseDetail(Model model,
                               CourseParam courseParam
    ) {

        CourseDto detail = courseService.getFrontDetail(courseParam.getId());
        model.addAttribute("detail", detail);

        return "course/detail";
    }
}

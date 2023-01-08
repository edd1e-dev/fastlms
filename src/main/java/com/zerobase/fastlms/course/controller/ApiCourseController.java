package com.zerobase.fastlms.course.controller;

import com.zerobase.fastlms.common.model.ResponseResult;
import com.zerobase.fastlms.course.model.RegisterCourseInput;
import com.zerobase.fastlms.course.model.ServiceResult;
import com.zerobase.fastlms.course.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class ApiCourseController {
    private final CourseService courseService;

    @PostMapping("/api/course/requset.api")
    public ResponseEntity<?> courseRequest(Model model,
                                           @RequestBody RegisterCourseInput registerCourseInput,
                                           Principal principal
    ) {

        registerCourseInput.setUserId(principal.getName());

        ServiceResult result = courseService.request(registerCourseInput);
        if (!result.isResult()) {
            ResponseResult responseResult = new ResponseResult(false, result.getMessage());
            return ResponseEntity.ok().body(responseResult);
        }

        ResponseResult responseResult = new ResponseResult(true);
        return ResponseEntity.ok().body(responseResult);
    }
}

package com.zerobase.fastlms.member.controller;

import com.zerobase.fastlms.common.model.ResponseResult;
import com.zerobase.fastlms.course.dto.RegisterCourseDto;
import com.zerobase.fastlms.course.model.RegisterCourseInput;
import com.zerobase.fastlms.course.model.ServiceResult;
import com.zerobase.fastlms.course.service.RegisterCourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class ApiMemberController {
    private final RegisterCourseService registerCourseService;
    @PostMapping("/api/course/cancel.api")
    public ResponseEntity<?> courseCancel(Model model,
                                          @RequestBody RegisterCourseInput registerCourseInput,
                                          Principal principal
    ) {
        String userId = principal.getName();
        RegisterCourseDto detail = registerCourseService.detail(registerCourseInput.getRegisterCourseId());

        if (detail == null) {
            ResponseResult responseResult = new ResponseResult(false, "수강 정보가 존재하지 않습니다.");
            return ResponseEntity.ok().body(responseResult);
        }

        if (userId == null || userId.equals(detail.getUserId())) {
            ResponseResult responseResult = new ResponseResult(false, "본인의 수강 신청 정보만 취소할 수 있습니다.");
            return ResponseEntity.ok().body(responseResult);
        }

        ServiceResult cancel = registerCourseService.cancel(registerCourseInput.getRegisterCourseId());
        if (!cancel.isResult()) {
            ResponseResult responseResult = new ResponseResult(false, cancel.getMessage());
            return ResponseEntity.ok().body(responseResult);
        }

        ResponseResult responseResult = new ResponseResult(true);
        return ResponseEntity.ok().body(responseResult);
    }
}

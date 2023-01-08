package com.zerobase.fastlms.course.service;

import com.zerobase.fastlms.course.dto.RegisterCourseDto;
import com.zerobase.fastlms.course.model.RegisterCourseParam;
import com.zerobase.fastlms.course.model.ServiceResult;

import java.util.List;

public interface RegisterCourseService {
    List<RegisterCourseDto> getList(RegisterCourseParam parameter);
    RegisterCourseDto detail(long id);
    ServiceResult updateStatus(long id, String status);
    List<RegisterCourseDto> getMyCourse(String userId);
    ServiceResult cancel(long id);
}

package com.zerobase.fastlms.course.service;

import com.zerobase.fastlms.course.dto.CourseDto;
import com.zerobase.fastlms.course.model.CourseInput;
import com.zerobase.fastlms.course.model.CourseParam;
import com.zerobase.fastlms.course.model.RegisterCourseInput;
import com.zerobase.fastlms.course.model.ServiceResult;

import java.util.List;

public interface CourseService {
    boolean add(CourseInput courseInput);

    List<CourseDto> getList(CourseParam courseParam);

    CourseDto getById(long id);

    boolean set(CourseInput courseInput);

    boolean delete(String idList);

    List<CourseDto> getFrontList(CourseParam courseParam);
    CourseDto getFrontDetail(long id);

    ServiceResult request(RegisterCourseInput registerCourseInput);
    List<CourseDto> getAllList();
}

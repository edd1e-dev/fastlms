package com.zerobase.fastlms.course.mapper;

import com.zerobase.fastlms.course.dto.RegisterCourseDto;
import com.zerobase.fastlms.course.model.RegisterCourseParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RegisterCourseMapper {

    long selectListCount(RegisterCourseParam parameter);

    List<RegisterCourseDto> selectList(RegisterCourseParam parameter);

    List<RegisterCourseDto> selectListMyCourse(RegisterCourseParam parameter);
}

package com.zerobase.fastlms.course.model;

import lombok.Data;

@Data
public class RegisterCourseInput {
    long courseId;
    String userId;
    long registerCourseId;
}

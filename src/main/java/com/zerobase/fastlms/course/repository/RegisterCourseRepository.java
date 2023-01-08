package com.zerobase.fastlms.course.repository;

import com.zerobase.fastlms.course.entity.RegisterCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface RegisterCourseRepository extends JpaRepository<RegisterCourse, Long> {
    long countByCourseIdAndUserIdAndStatusIn(long courseId, String userId, Collection<String> statusList);
}

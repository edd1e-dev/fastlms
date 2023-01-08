package com.zerobase.fastlms.course.dto;

import com.zerobase.fastlms.course.entity.Course;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseDto {
    private Long id;
    private long categoryId;
    private String imagePath;
    private String keyword;
    private String subject;
    private String summary;
    private String contents;
    private long price;
    private long salePrice;
    private LocalDate saleEndDt;
    private LocalDateTime regDt;
    private LocalDateTime udtDt;
    private String filename;
    private String urlFilename;
    private long totalCount;
    private long seq;

    public static CourseDto of(Course course) {
        return CourseDto.builder()
                .id(course.getId())
                .categoryId(course.getCategoryId())
                .imagePath(course.getImagePath())
                .keyword(course.getKeyword())
                .subject(course.getSubject())
                .summary(course.getSummary())
                .contents(course.getContents())
                .price(course.getPrice())
                .salePrice(course.getSalePrice())
                .saleEndDt(course.getSaleEndDt())
                .regDt(course.getRegDt())
                .udtDt(course.getUdtDt())
                .build();
    }

    public static List<CourseDto> of(List<Course> courses) {
        if (courses == null) {
            return null;
        }

        List<CourseDto> courseList = new ArrayList<>();
        for (Course x : courses) {
            courseList.add(CourseDto.of(x));
        }
        return courseList;
    }
}

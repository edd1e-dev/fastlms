package com.zerobase.fastlms.course.model;

import lombok.Data;

@Data
public class CourseInput {
    private long id;
    private long categoryId;
    private String subject;
    private String keyword;
    private String summary;
    private String contents;
    private long price;
    private long salePrice;
    private String saleEndDtText;
    private String idList;
    private String filename;
    private String urlFilename;
}

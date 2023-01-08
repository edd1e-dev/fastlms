package com.zerobase.fastlms.course.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private long categoryId;
    private String imagePath;
    private String keyword;
    private String subject;
    @Column(length = 1000)
    private String summary;
    @Lob
    private String contents;
    private long price;
    private long salePrice;
    private LocalDate saleEndDt;
    private LocalDateTime regDt;
    private LocalDateTime udtDt;
    private String filename;
    private String urlFilename;
}

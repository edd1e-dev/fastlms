package com.zerobase.fastlms.banner.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Banner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String imagePath;
    private String link;
    private String targetType;
    private int descOrder;
    private boolean isPublic;
    LocalDateTime regDt;
    private String filename;
    private String urlFilename;
}

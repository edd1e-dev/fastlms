package com.zerobase.fastlms.banner.model;

import lombok.Data;

@Data
public class BannerInput {
    private Long id; // bannerId
    private String name;
    private String imagePath;
    private String link;
    private String targetType;
    private int descOrder;
    private String isPublic;
    private boolean _isPublic;

    private String idList;

    private String filename;
    private String urlFilename;
}

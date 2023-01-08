package com.zerobase.fastlms.admin.model;

import lombok.Data;

@Data
public class CategoryInput {
    private long id;
    private String categoryName;
    private int sortValue;
    private boolean usingYn;
}

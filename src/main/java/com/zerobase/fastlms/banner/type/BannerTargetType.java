package com.zerobase.fastlms.banner.type;

import java.util.ArrayList;
import java.util.List;

public enum BannerTargetType {
    SELF,
    BLANK,
    PARENT,
    TOP;

    public static List<String> getList() {
        List<String> bannerTargetTypeList = new ArrayList<>();
        for (BannerTargetType type : BannerTargetType.values()) {
            bannerTargetTypeList.add(type.name());
        }

        return bannerTargetTypeList;
    }
}

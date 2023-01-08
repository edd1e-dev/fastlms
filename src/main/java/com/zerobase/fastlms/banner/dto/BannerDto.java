package com.zerobase.fastlms.banner.dto;

import com.zerobase.fastlms.banner.entity.Banner;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class BannerDto {
    private Long id;
    private String name;
    private String imagePath;
    private String link;
    private String targetType;
    private int descOrder;
    private boolean isPublic;
    LocalDateTime regDt;
    private long totalCount;
    private long seq;
    private String filename;
    private String urlFilename;

    public static List<BannerDto> of(List<Banner> bannerList) {
        List<BannerDto> bannerDtoList = new ArrayList<>();
        for (Banner banner : bannerList) {
            BannerDto bannerDto = BannerDto.builder()
                    .id(banner.getId())
                    .name(banner.getName())
                    .imagePath(banner.getImagePath())
                    .link(banner.getLink())
                    .targetType(banner.getTargetType())
                    .descOrder(banner.getDescOrder())
                    .isPublic(banner.isPublic())
                    .regDt(banner.getRegDt())
                    .filename(banner.getFilename())
                    .urlFilename(banner.getUrlFilename())
                    .build();
            bannerDtoList.add(bannerDto);
        }
        return bannerDtoList;
    }

    public static BannerDto of(Banner banner) {
        return BannerDto.builder()
                .id(banner.getId())
                .name(banner.getName())
                .imagePath(banner.getImagePath())
                .link(banner.getLink())
                .targetType(banner.getTargetType())
                .descOrder(banner.getDescOrder())
                .isPublic(banner.isPublic())
                .regDt(banner.getRegDt())
                .filename(banner.getFilename())
                .urlFilename(banner.getUrlFilename())
                .build();
    }

    public String getRegDtText() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        return regDt != null ? regDt.format(formatter) : "";
    }
}

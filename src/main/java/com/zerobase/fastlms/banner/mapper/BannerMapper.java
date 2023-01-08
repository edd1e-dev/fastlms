package com.zerobase.fastlms.banner.mapper;

import com.zerobase.fastlms.banner.dto.BannerDto;
import com.zerobase.fastlms.banner.model.BannerParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BannerMapper {
    long selectListCount(BannerParam bannerParam);
    List<BannerDto> selectAllList(BannerParam bannerParam);

    List<BannerDto> selectList(BannerParam bannerParam);
}

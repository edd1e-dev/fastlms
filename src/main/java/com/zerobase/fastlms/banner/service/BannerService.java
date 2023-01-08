package com.zerobase.fastlms.banner.service;

import com.zerobase.fastlms.banner.dto.BannerDto;
import com.zerobase.fastlms.banner.model.BannerInput;
import com.zerobase.fastlms.banner.model.BannerParam;

import java.util.List;

public interface BannerService {

    List<BannerDto> getList(BannerParam bannerParam);

    BannerDto getById(long id);

    boolean set(BannerInput bannerInput);

    boolean add(BannerInput bannerInput);

    boolean delete(String idList);
    List<BannerDto> getAllList(BannerParam bannerParam);
}

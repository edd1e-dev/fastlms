package com.zerobase.fastlms.banner.service;

import com.zerobase.fastlms.banner.dto.BannerDto;
import com.zerobase.fastlms.banner.entity.Banner;
import com.zerobase.fastlms.banner.mapper.BannerMapper;
import com.zerobase.fastlms.banner.model.BannerInput;
import com.zerobase.fastlms.banner.model.BannerParam;
import com.zerobase.fastlms.banner.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BannerServiceImpl implements BannerService {
    private final BannerMapper bannerMapper;
    private final BannerRepository bannerRepository;

    @Override
    public List<BannerDto> getList(BannerParam bannerParam) {
        long totalCount = bannerMapper.selectListCount(bannerParam);
        List<BannerDto> list = bannerMapper.selectList(bannerParam);

        if (!CollectionUtils.isEmpty(list)) {
            int i = 0;
            for (BannerDto x : list) {
                x.setTotalCount(totalCount);
                x.setSeq(totalCount - bannerParam.getPageStart() - i);
                i++;
            }
        }

        return list;
    }

    @Override
    public BannerDto getById(long id) {
        return bannerRepository.findById(id).map(BannerDto::of).orElse(null);
    }

    @Override
    public boolean add(BannerInput bannerInput) {
        Banner banner = Banner.builder()
                .id(bannerInput.getId())
                .name(bannerInput.getName())
                .imagePath(bannerInput.getImagePath())
                .link(bannerInput.getLink())
                .targetType(bannerInput.getTargetType())
                .descOrder(bannerInput.getDescOrder())
                .isPublic(bannerInput.is_isPublic())
                .regDt(LocalDateTime.now())
                .urlFilename(bannerInput.getUrlFilename())
                .filename(bannerInput.getFilename())
                .build();
        bannerRepository.save(banner);

        return true;
    }

    @Override
    public boolean delete(String idList) {
        if (idList != null && idList.length() > 0) {
            String[] ids = idList.split(",");
            for (String x : ids) {
                long id = 0L;
                try {
                    id = Long.parseLong(x);
                } catch (Exception e) {
                }

                if (id > 0) {
                    bannerRepository.deleteById(id);
                }
            }
        }

        return true;
    }

    @Override
    public List<BannerDto> getAllList(BannerParam bannerParam) {
        return bannerMapper.selectAllList(bannerParam);
    }

    @Override
    public boolean set(BannerInput bannerInput) {
        Optional<Banner> optionalBanner = bannerRepository.findById(bannerInput.getId());
        if (!optionalBanner.isPresent()) {
            return false;
        }

        Banner banner = optionalBanner.get();
        banner.setName(bannerInput.getName());
        banner.setImagePath(bannerInput.getImagePath());
        banner.setLink(bannerInput.getLink());
        banner.setTargetType(bannerInput.getTargetType());
        banner.setDescOrder(bannerInput.getDescOrder());
        banner.setPublic(bannerInput.is_isPublic());
        banner.setFilename(bannerInput.getFilename());
        banner.setUrlFilename(bannerInput.getUrlFilename());
        bannerRepository.save(banner);

        return true;
    }
}

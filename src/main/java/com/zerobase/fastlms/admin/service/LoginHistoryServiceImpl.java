package com.zerobase.fastlms.admin.service;

import com.zerobase.fastlms.admin.dto.LoginHistoryDto;
import com.zerobase.fastlms.admin.entity.LoginHistory;
import com.zerobase.fastlms.admin.mapper.LoginHistoryMapper;
import com.zerobase.fastlms.admin.model.LoginHistoryParam;
import com.zerobase.fastlms.admin.model.MemberParam;
import com.zerobase.fastlms.admin.repository.LoginHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoginHistoryServiceImpl implements LoginHistoryService {
    private final LoginHistoryRepository loginHistoryRepository;
    private final LoginHistoryMapper loginHistoryMapper;

    @Override
    public List<LoginHistoryDto> getMyHistory(MemberParam memberParam) {
        String userId = memberParam.getUserId();
        int totalCount = loginHistoryRepository.countAllByUserId(userId);
        List<LoginHistoryDto> historyList = loginHistoryMapper.selectList(memberParam);

        if (!CollectionUtils.isEmpty(historyList)) {
            int i = 0;
            for (LoginHistoryDto x : historyList) {
                x.setTotalCount(totalCount);
                x.setSeq(totalCount - memberParam.getPageStart() - i);
                i++;
            }
        }

        return historyList;
    }
}

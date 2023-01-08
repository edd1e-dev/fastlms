package com.zerobase.fastlms.admin.service;

import com.zerobase.fastlms.admin.dto.LoginHistoryDto;
import com.zerobase.fastlms.admin.model.LoginHistoryParam;
import com.zerobase.fastlms.admin.model.MemberParam;

import java.util.List;

public interface LoginHistoryService {
    List<LoginHistoryDto> getMyHistory(MemberParam memberParam);
}

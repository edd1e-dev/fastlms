package com.zerobase.fastlms.admin.model;

import com.zerobase.fastlms.admin.model.CommonParam;
import lombok.Data;

@Data
public class LoginHistoryParam extends CommonParam {
    private String userId;
}

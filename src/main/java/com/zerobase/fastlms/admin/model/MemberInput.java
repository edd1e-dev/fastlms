package com.zerobase.fastlms.admin.model;

import lombok.Data;

@Data
public class MemberInput {
    private String userId;
    private String userStatus;
    private String password;
}

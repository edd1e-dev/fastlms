package com.zerobase.fastlms.member.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member implements MemberCode {
    @Id
    private String userId;

    private String userName;
    private String phone;
    private String password;
    private LocalDateTime regDt;
    private LocalDateTime udtDt;
    private LocalDateTime lastLoginDt;
    private boolean emailAuthYn;
    private String emailAuthKey;
    private LocalDateTime emailAuthDt;
    private String resetPasswordKey;
    private LocalDateTime resetPasswordLimitDt;
    private boolean adminYn;
    private String userStatus;
    private String zipcode;
    private String addr;
    private String addrDetail;
}

package com.zerobase.fastlms.member.model;

import lombok.*;

public class ResetPasswordInput {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class Request {
        private String userId;
        private String userName;
        private String id;
        private String password;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @ToString
    public static class Response {

    }
}

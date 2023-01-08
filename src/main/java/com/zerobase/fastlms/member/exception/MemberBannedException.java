package com.zerobase.fastlms.member.exception;

public class MemberBannedException extends RuntimeException {
    public MemberBannedException(String error) {
        super(error);
    }
}

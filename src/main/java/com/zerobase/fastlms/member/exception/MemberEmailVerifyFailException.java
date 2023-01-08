package com.zerobase.fastlms.member.exception;

public class MemberEmailVerifyFailException extends RuntimeException {
    public MemberEmailVerifyFailException(String error) {
        super(error);
    }
}

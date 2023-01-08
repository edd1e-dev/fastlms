package com.zerobase.fastlms.admin.dto;

import com.zerobase.fastlms.admin.entity.LoginHistory;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class LoginHistoryDto {
    private String userId;
    private LocalDateTime loginDt;
    private String ip;
    private String userAgent;

    private long totalCount;
    private long seq;

    public static List<LoginHistoryDto> of(List<LoginHistory> historyList) {
        List<LoginHistoryDto> historyDtoList = new ArrayList<>();

        for (LoginHistory history : historyList) {
            LoginHistoryDto loginHistoryDto = LoginHistoryDto.builder()
                    .userId(history.getUserId())
                    .loginDt(history.getLoginDt())
                    .ip(history.getIp())
                    .userAgent(history.getUserAgent())
                    .build();
            historyDtoList.add(loginHistoryDto);
        }

        return historyDtoList;
    }

    public String getLoginDtText() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        return loginDt != null ? loginDt.format(formatter) : "";
    }
}

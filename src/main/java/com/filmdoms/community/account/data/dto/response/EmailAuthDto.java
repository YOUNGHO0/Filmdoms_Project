package com.filmdoms.community.account.data.dto.response;

import lombok.Getter;


@Getter
public class EmailAuthDto {

    private String uuid;

    public EmailAuthDto(String uuid) {
        this.uuid = uuid;
    }

    public static EmailAuthDto from(String uuid) {
        return new EmailAuthDto(uuid);
    }
}

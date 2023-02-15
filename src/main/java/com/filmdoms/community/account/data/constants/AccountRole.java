package com.filmdoms.community.account.data.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccountRole {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private final String name;
}

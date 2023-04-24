package com.filmdoms.community.testentityprovider;

import com.filmdoms.community.account.data.constant.AccountRole;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.file.data.entity.File;

public class TestAccountProvider {

    public static int count;

    public static Account get() {
        count++;
        Account account = Account.builder()
                .nickname("test_nickname_" + count)
                .email("test_email_" + count + "@filmdoms.com")
                .password("test_password")
                .role(AccountRole.USER)
                .build();
        return account;
    }

    public static Account get(File profileImage) {
        count++;
        Account account = Account.builder()
                .nickname("test_nickname_" + count)
                .email("test_email_" + count + "@filmdoms.com")
                .password("test_password")
                .role(AccountRole.USER)
                .profileImage(profileImage)
                .build();
        return account;
    }
}

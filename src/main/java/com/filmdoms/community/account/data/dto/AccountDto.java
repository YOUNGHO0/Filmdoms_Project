package com.filmdoms.community.account.data.dto;

import com.filmdoms.community.account.data.constant.AccountRole;
import com.filmdoms.community.account.data.constant.AccountStatus;
import com.filmdoms.community.account.data.entity.Account;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@ToString(onlyExplicitlyIncluded = true)
public class AccountDto {

    @ToString.Include
    private Long id;
    private String nickname;
    private String password;
    @ToString.Include
    private String email;
    @ToString.Include
    private AccountRole accountRole;
    @ToString.Include
    private AccountStatus accountStatus;
    private LocalDateTime dateCreated;
    private Integer loginFailCount;
    private LocalDateTime dateLockedTill;
    private boolean isSocialLogin;

    private AccountDto(Account account) {
        this.id = account.getId();
        this.nickname = account.getNickname();
        this.password = account.getPassword();
        this.email = account.getEmail();
        this.accountRole = account.getAccountRole();
        this.accountStatus = account.getAccountStatus();
        this.dateCreated = account.getDateCreated();
        this.loginFailCount = account.getLoginFailCount();
        this.dateLockedTill = account.getDateLockedTill();
        this.isSocialLogin = account.isSocialLogin();
    }

    public static AccountDto from(Account account) {
        return new AccountDto(account);
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add((GrantedAuthority) () -> accountRole.getName());
        return authorities;
    }
}

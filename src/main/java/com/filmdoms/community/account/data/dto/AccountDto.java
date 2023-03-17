package com.filmdoms.community.account.data.dto;

import com.filmdoms.community.account.data.constants.AccountRole;
import com.filmdoms.community.account.data.constants.AccountStatus;
import com.filmdoms.community.account.data.entity.Account;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@AllArgsConstructor
@ToString
public class AccountDto implements UserDetails {

    private Long id;
    private String username;
    private String nickname;
    private String password;
    private String email;
    private AccountRole accountRole;
    private AccountStatus accountStatus;
    private LocalDateTime dateCreated;
    private Integer loginFailCount;
    private LocalDateTime dateLockedTill;
    private boolean isSocialLogin;

    public static AccountDto from(Account entity) {
        return new AccountDto(
                entity.getId(),
                entity.getUsername(),
                entity.getNickname(),
                entity.getPassword(),
                entity.getEmail(),
                entity.getAccountRole(),
                entity.getAccountStatus(),
                entity.getDateCreated(),
                entity.getLoginFailCount(),
                entity.getDateLockedTill(),
                entity.isSocialLogin()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add((GrantedAuthority) () -> accountRole.getName());
        return collect;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountStatus != AccountStatus.EXPIRED;
    }

    @Override
    public boolean isAccountNonLocked() {
        return dateLockedTill != null;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return accountStatus == AccountStatus.ACTIVE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AccountDto that = (AccountDto) o;
        return Objects.equals(id, that.id) && Objects.equals(username, that.username)
                && Objects.equals(email, that.email);
    }
}

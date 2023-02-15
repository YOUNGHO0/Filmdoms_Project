package com.filmdoms.community.account.data.dto;

import com.filmdoms.community.account.data.constants.AccountRole;
import com.filmdoms.community.account.data.constants.AccountStatus;
import com.filmdoms.community.account.data.entity.Account;
import java.sql.Timestamp;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@AllArgsConstructor
public class AccountDto implements UserDetails {


    private Long id;
    private String username;
    private String nickname;
    private String password;
    private String email;
    private AccountRole accountRole;
    private AccountStatus accountStatus;
    private Timestamp dateCreated;
    private Integer loginFailCount;
    private Timestamp dateLockedTill;

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
                entity.getDateLockedTill()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
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
}

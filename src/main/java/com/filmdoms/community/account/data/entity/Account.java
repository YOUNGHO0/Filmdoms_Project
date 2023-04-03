package com.filmdoms.community.account.data.entity;

import com.filmdoms.community.account.data.constants.AccountRole;
import com.filmdoms.community.account.data.constants.AccountStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "account", indexes = {
        @Index(columnList = "username"),
        @Index(columnList = "email")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "password")
    private String password;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private AccountRole accountRole;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus = AccountStatus.ACTIVE;

    @Column(name = "date_created")
    private LocalDateTime dateCreated;

    @Column(name = "login_fail_count")
    private int loginFailCount = 0;

    @Column(name = "date_locked_till", nullable = true)
    private LocalDateTime dateLockedTill;

    @Column(name = "social_login")
    private boolean isSocialLogin;

    @PrePersist
    void dateCreated() {
        this.dateCreated = LocalDateTime.now();
    }

    @Builder
    private Account(String username, String nickname, String password, AccountRole role, String email, boolean isSocialLogin) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.accountRole = Optional.ofNullable(role).orElse(AccountRole.USER);
        this.isSocialLogin = isSocialLogin;
    }
}

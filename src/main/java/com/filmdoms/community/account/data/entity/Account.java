package com.filmdoms.community.account.data.entity;

import com.filmdoms.community.account.data.constant.AccountRole;
import com.filmdoms.community.account.data.constant.AccountStatus;
import com.filmdoms.community.file.data.entity.File;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Table(name = "account", indexes = {
        @Index(columnList = "nickname"),
        @Index(columnList = "email")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nickname", unique = true)
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

    private LocalDateTime deleteRegisteredDate = null;

    @Column(name = "login_fail_count")
    private int loginFailCount = 0;

    @Column(name = "date_locked_till", nullable = true)
    private LocalDateTime dateLockedTill;

    @Column(name = "social_login")
    private boolean isSocialLogin;

    @JoinColumn(name = "file_id")
    @OneToOne(fetch = FetchType.LAZY)
    private File profileImage;

    @Builder
    private Account(String nickname, String password, AccountRole role, String email, boolean isSocialLogin, File profileImage) {
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.accountRole = Optional.ofNullable(role).orElse(AccountRole.USER);
        this.isSocialLogin = isSocialLogin;
        this.profileImage = profileImage;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateProfileImage(File profileImage) {
        this.profileImage = profileImage;
    }

    public void updateNicknameAndChangeRoleToUser(String nickname) {
        this.nickname = nickname;
        this.accountRole = AccountRole.USER;
    }

    public void updateStatusToDeleted(LocalDateTime localDateTime) {
        this.accountStatus = AccountStatus.DELETED;
        this.deleteRegisteredDate = localDateTime;
    }
}

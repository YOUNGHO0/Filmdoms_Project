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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "account", indexes = {
        @Index(columnList = "username"),
        @Index(columnList = "email")
})
@NoArgsConstructor
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
    private AccountStatus accountStatus;

    @Column(name = "date_created")
    private Timestamp dateCreated;

    @Column(name = "login_fail_count")
    private Integer loginFailCount;

    @Column(name = "date_locked_till", nullable = true)
    private Timestamp dateLockedTill;

    @PrePersist
    void dateCreated() {
        this.dateCreated = Timestamp.from(Instant.now());
    }

    private Account(Long id, String username, String password, AccountRole role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.accountRole = role;
    }

    public static Account of(String username, String password, AccountRole role) {
        return new Account(null, username, password, role);
    }

    public static Account of(Long id, String username, String password, AccountRole role) {
        return new Account(id, username, password, role);
    }
}

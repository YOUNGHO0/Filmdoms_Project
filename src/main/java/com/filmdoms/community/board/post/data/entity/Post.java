package com.filmdoms.community.board.post.data.entity;

import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.board.post.data.constants.PostCategory;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
@Table(name = "post", indexes = {
        @Index(columnList = "category")
})
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    private PostCategory postCategory;

    @Setter
    @Column(name = "title", nullable = false)
    private String title;

    @Setter
    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "view", nullable = false)
    private Integer view;

    @OrderBy("dateCreated DESC")
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final Set<PostComment> postComments = new LinkedHashSet<>();

    @Column(name = "date_created")
    private Timestamp dateCreated;

    @Column(name = "date_last_modified")
    private Timestamp dateLastModified;

    @PrePersist
    void dateCreated() {
        this.dateCreated = Timestamp.from(Instant.now());
    }

    @PreUpdate
    void dateLastModified() {
        this.dateLastModified = Timestamp.from(Instant.now());
    }
}

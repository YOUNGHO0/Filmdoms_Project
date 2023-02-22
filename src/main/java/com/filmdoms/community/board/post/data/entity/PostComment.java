package com.filmdoms.community.board.post.data.entity;

import com.filmdoms.community.account.data.entity.Account;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@Table(name = "post_comment")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "post_id")
    @ManyToOne(optional = false)
    private Post post;

    @JoinColumn(name = "user_id")
    @ManyToOne(optional = false)
    private Account account;

    @Setter
    @Column(name = "content", nullable = false)
    private String content;

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

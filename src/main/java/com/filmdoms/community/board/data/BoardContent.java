package com.filmdoms.community.board.data;


import com.filmdoms.community.imagefile.data.entitiy.ImageFile;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "board_content")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public final class BoardContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false, length = 10000)
    private String content;

    @OneToMany(mappedBy = "boardContent", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private final Set<ImageFile> imageFiles = new HashSet<>();

    @Builder
    private BoardContent(String content) {
        this.content = content;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}

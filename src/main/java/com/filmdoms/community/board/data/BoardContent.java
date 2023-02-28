package com.filmdoms.community.board.data;


import com.filmdoms.community.imagefile.data.entitiy.ImageFile;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
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

    @Column(name = "content", length = 10000)
    private String content;

    @OneToMany(mappedBy = "boardContent", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<ImageFile> imageFiles = new HashSet<>();

    @Builder
    private BoardContent(String content) {
        this.content = content;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}

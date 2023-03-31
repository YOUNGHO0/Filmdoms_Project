package com.filmdoms.community.file.data.entity;

import com.filmdoms.community.article.data.entity.Content;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
public class FileContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "file_id")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE) //File 자동 삭제(cascade) 여부 논의 필요, OneToOne이어야 cascade 옵션 적용 가능
    private File file;

    @JoinColumn(name = "content_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Content content;

    @Builder
    private FileContent(File file, Content content) {
        this.file = file;
        this.content = content;
    }
}

package com.filmdoms.community.imagefile.data.entitiy;


import com.filmdoms.community.account.data.entity.BaseTimeEntity;
import com.filmdoms.community.board.data.BoardContent;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageFile extends BaseTimeEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "original_file_name")
    String originalFileName;
    @Column(name = "uuid_file_name")
    String uuidFileName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_content_id")
    public BoardContent boardContent;


    @Builder
    private ImageFile(String uuidFileName, String originalFileName, BoardContent boardContent) {

        this.uuidFileName = uuidFileName;
        this.originalFileName = originalFileName;
        this.boardContent = boardContent;
    }

    public void updateBoardContent(BoardContent boardContent) {
        this.boardContent = boardContent;
    }

    public String getFileUrl(String domain) {
        return domain + "/" + uuidFileName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ImageFile imageFile = (ImageFile) o;
        return Objects.equals(id, imageFile.id) && Objects.equals(uuidFileName, imageFile.uuidFileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuidFileName);
    }
}
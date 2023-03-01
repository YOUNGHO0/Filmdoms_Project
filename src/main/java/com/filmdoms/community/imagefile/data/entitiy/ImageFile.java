package com.filmdoms.community.imagefile.data.entitiy;


import com.filmdoms.community.board.data.BaseTimeEntity;
import com.filmdoms.community.board.data.BoardContent;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
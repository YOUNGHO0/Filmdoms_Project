package com.filmdoms.community.file.data.entity;

import com.filmdoms.community.board.data.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class File extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalFileName;

    private String uuidFileName;

    @Builder
    private File(String uuidFileName, String originalFileName) {
        this.uuidFileName = uuidFileName;
        this.originalFileName = originalFileName;
    }

    public String getFileUrl(String domain) {
        return domain + "/" + uuidFileName;
    }
}

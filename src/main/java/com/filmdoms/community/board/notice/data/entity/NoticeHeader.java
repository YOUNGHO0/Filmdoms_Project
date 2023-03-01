package com.filmdoms.community.board.notice.data.entity;

import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.board.data.BoardContent;
import com.filmdoms.community.board.data.BoardHeadCore;
import com.filmdoms.community.imagefile.data.entitiy.ImageFile;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "notice_header")
@DiscriminatorValue("NoticeHeader")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class NoticeHeader extends BoardHeadCore {

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "image_file_id")
    private ImageFile mainImage;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @Builder
    private NoticeHeader(String title, Account author, BoardContent boardContent, ImageFile mainImage, LocalDateTime startDate, LocalDateTime endDate) {
        super(title, author, boardContent);
        this.mainImage = mainImage;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void update(String title, String content, ImageFile mainImageFile, LocalDateTime startDate, LocalDateTime endDate) {
        //업데이트 방법 정해지면 수정
        updateTitle(title);
        getBoardContent().updateContent(content);
        this.mainImage = mainImageFile;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}

package com.filmdoms.community.board.banner.data.entity;

import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.board.data.BoardContent;
import com.filmdoms.community.board.data.BoardHeadCore;
import com.filmdoms.community.imagefile.data.dto.ImageFileDto;
import com.filmdoms.community.imagefile.data.entitiy.ImageFile;
import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("BannerHeader")
@Table(name = "banner_header")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BannerHeader extends BoardHeadCore {


    @OneToOne
    @JoinColumn(name = "image_file_id")
    private ImageFile mainImage;

    @Builder
    private BannerHeader(String title, Account author, BoardContent content, ImageFile mainImage) {
        super(title, author, content);
        this.mainImage = mainImage;
    }

    public void update(String title, ImageFile mainImage) {
        updateBoardHeadCore(title, null);
    }

    public String getMainImageUrl(String domain) {
        return mainImage.getFileUrl(domain);
    }
}

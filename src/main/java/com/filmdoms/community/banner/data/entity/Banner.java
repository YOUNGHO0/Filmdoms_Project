package com.filmdoms.community.banner.data.entity;

import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.board.data.BoardHeadCore;
import com.filmdoms.community.imagefile.data.entitiy.ImageFile;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "banner")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Banner extends BoardHeadCore {

    @OneToMany(mappedBy = "header", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ImageFile> imageFiles = new ArrayList<>();

    @Builder
    private Banner(String title, Account author) {
        super(title, author, null);
    }

    public String getFirstImageUrl() {
        return imageFiles.get(0).getFileUrl();
    }

}

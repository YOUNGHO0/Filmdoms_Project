package com.filmdoms.community.banner.data.entity;

import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.board.data.BoardHeadCore;
import com.filmdoms.community.imagefile.data.entitiy.ImageFile;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "banner")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Banner extends BoardHeadCore {

    @OneToMany(mappedBy = "boardHeadCore", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ImageFile> imageFiles = new ArrayList<>();

    @Builder
    private Banner(String title, Account author) {
        super(title, author, null);
    }

    public String getFirstImageUrl() {
        return imageFiles.get(0).getFileUrl();
    }

}

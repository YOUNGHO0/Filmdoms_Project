package com.filmdoms.community.banner.data.entity;

import com.filmdoms.community.board.data.BoardHeadCore;
import com.filmdoms.community.imagefile.data.entitiy.ImageFile;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "\"banner\"")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class Banner extends BoardHeadCore {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_file_id")
    private ImageFile imageFile;

}

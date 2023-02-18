package com.filmdoms.community.account.data.entity;


import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;

@Entity
@RequiredArgsConstructor
public class ImageFile {


    @Id @GeneratedValue
    Long id;
    String originalFileName;
    String uuidFileName;
    String fileUrl;


    public ImageFile(String uuidFileName, String originalFileName, String fileUrl) {
        this.uuidFileName = uuidFileName;
        this.originalFileName = originalFileName;
        this.fileUrl = fileUrl;
    }
}

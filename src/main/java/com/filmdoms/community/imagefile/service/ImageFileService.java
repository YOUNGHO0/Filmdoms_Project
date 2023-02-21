package com.filmdoms.community.imagefile.service;

import com.filmdoms.community.board.data.BoardHeadCore;
import com.filmdoms.community.imagefile.data.entitiy.ImageFile;
import com.filmdoms.community.imagefile.repository.ImageFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageFileService {
    private final ImageFileRepository imageFileRepository;
    private final AmazonS3Upload amazonS3Upload;

    public Optional<ImageFile> saveImage(MultipartFile multipartFile, BoardHeadCore head) throws IOException {
        if(multipartFile.isEmpty()) {
            return Optional.empty();
        }
        String uuidFileName = UUID.randomUUID().toString();
        String originalFileName = multipartFile.getOriginalFilename();
        String url =  amazonS3Upload.upload(multipartFile, uuidFileName, originalFileName);
        ImageFile imageFile = new ImageFile(uuidFileName, originalFileName, url, head);
        imageFileRepository.save(imageFile);
        return Optional.of(imageFile);
    }

    public List<ImageFile> saveImages(List<MultipartFile> multipartFiles, BoardHeadCore head) throws IOException {
        List<ImageFile> imageFiles = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            Optional<ImageFile> imageFile = saveImage(multipartFile, head);
            if(imageFile.isPresent()) {
                imageFiles.add(imageFile.get());
            }
        }
        return imageFiles;
    }
}

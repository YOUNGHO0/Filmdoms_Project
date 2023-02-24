package com.filmdoms.community.imagefile.service;

import com.filmdoms.community.board.data.BoardHeadCore;
import com.filmdoms.community.imagefile.data.dto.ImageFileDto;
import com.filmdoms.community.imagefile.data.entitiy.ImageFile;
import com.filmdoms.community.imagefile.repository.ImageFileRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageFileService {

    @Value("${domain}")
    private String domain;
    private final ImageFileRepository imageFileRepository;
    private final AmazonS3UploadService amazonS3UploadService;

    public Optional<ImageFileDto> saveImage(MultipartFile multipartFile, BoardHeadCore head) {

        if (multipartFile == null || multipartFile.isEmpty()) {
            return Optional.empty();
        }

        // 업로드 하면 UploadedFileDto를 받는데, 이걸 toEntity로 ImageFile 엔티티로 바꿔줌
        ImageFile imageFile = amazonS3UploadService
                .upload(multipartFile, multipartFile.getOriginalFilename())
                .toEntity(head);

        // 저장 후 ImageFileDto로 바꿔서 반환
        return Optional.of(ImageFileDto.from(imageFileRepository.save(imageFile), domain));
    }

    public List<ImageFileDto> saveImages(List<MultipartFile> multipartFiles, BoardHeadCore head) {
        if (multipartFiles == null || multipartFiles.isEmpty()) {
            return new ArrayList<>();
        }
        return multipartFiles.stream()
                .map(file -> saveImage(file, head))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }
}

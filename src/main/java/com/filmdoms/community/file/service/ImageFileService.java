package com.filmdoms.community.file.service;

import com.filmdoms.community.exception.ApplicationException;
import com.filmdoms.community.exception.ErrorCode;
import com.filmdoms.community.article.data.entity.Content;
import com.filmdoms.community.file.data.entity.File;
import com.filmdoms.community.file.data.entity.FileContent;
import com.filmdoms.community.file.repository.FileContentRepository;
import com.filmdoms.community.file.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ImageFileService {

    private final FileRepository imageFileRepository;
    private final FileContentRepository fileContentRepository;

    public void setImageContent(Set<Long> imageIds, Content content) {
        if (imageIds == null) {
            return;
        }
        imageIds.forEach(id -> setImageContent(id, content));
    }

    public void setImageContent(Long imageId, Content content) {
        if (imageId == null) {
            return;
        }
        File imageFile = imageFileRepository.findById(imageId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_FILE_ID));
        FileContent fileContent = FileContent.builder()
                .content(content)
                .file(imageFile)
                .build();
        if (fileContentRepository.findByFile(imageFile).isPresent()) {
            throw new ApplicationException(ErrorCode.IMAGE_BELONG_TO_OTHER_POST); //이미지에 이미 게시글 컨텐츠가 매핑되어 있으면 예외 발생
        }
        fileContentRepository.save(fileContent);
    }

    public void updateImageContent(Set<Long> updateImageIds, Content boardContent) {
        if (updateImageIds == null) {
            return;
        }
        Set<File> updateImageFiles;

        //업데이트할 ImageFile 엔티티들을 불러오고, 유효하지 않은 이미지 ID라면 예외를 발생시킴
        try {
            updateImageFiles = updateImageIds.stream()
                    .map(id -> imageFileRepository.findById(id).get())
                    .collect(Collectors.toSet());
        } catch (NoSuchElementException e) {
            throw new ApplicationException(ErrorCode.INVALID_FILE_ID);
        }

        //원래 가지고 있던 이미지 중 업데이트 목록에 없는 것들은 삭제
        Set<FileContent> originalImageFiles = boardContent.getFileContents();
        originalImageFiles.stream()
                .filter(image -> !updateImageFiles.contains(image))
                .forEach(image -> fileContentRepository.delete(image));

        //업데이트 목록에 있는 이미지 중 원래 목록에 없는 것들은 헤더 설정
        updateImageFiles.stream()
                .filter(image -> !originalImageFiles.contains(image))
                .forEach(image -> {

                    FileContent fileContent = FileContent.builder()
                            .content(boardContent)
                            .file(image)
                            .build();
                    if (fileContentRepository.findByFile(image).isPresent()) {
                        throw new ApplicationException(ErrorCode.IMAGE_BELONG_TO_OTHER_POST); //이미지에 이미 게시글 컨텐츠가 매핑되어 있으면 예외 발생
                    }
                    fileContentRepository.save(fileContent);

                });
    }
}

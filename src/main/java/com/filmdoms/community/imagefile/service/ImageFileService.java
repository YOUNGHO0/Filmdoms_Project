package com.filmdoms.community.imagefile.service;

import com.filmdoms.community.account.exception.ApplicationException;
import com.filmdoms.community.account.exception.ErrorCode;
import com.filmdoms.community.board.data.BoardContent;
import com.filmdoms.community.imagefile.data.entitiy.ImageFile;
import com.filmdoms.community.imagefile.repository.ImageFileRepository;
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

    private final ImageFileRepository imageFileRepository;

    public void setImageContent(Set<Long> imageIds, BoardContent content) {
        if (imageIds == null) {
            return;
        }
        imageIds.forEach(id -> setImageContent(id, content));
    }

    public void setImageContent(Long imageId, BoardContent content) {
        if (imageId == null) {
            return;
        }
        ImageFile imageFile = imageFileRepository.findById(imageId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_IMAGE_ID));
        if (imageFile.getBoardContent() != null) {
            throw new ApplicationException(ErrorCode.IMAGE_BELONG_TO_OTHER_POST); //이미지에 이미 게시글 컨텐츠가 매핑되어 있으면 예외 발생
        }
        imageFile.updateBoardContent(content);
    }

    public void updateImageContent(Set<Long> updateImageIds, BoardContent boardContent) {
        if (updateImageIds == null) {
            return;
        }
        Set<ImageFile> updateImageFiles;

        //업데이트할 ImageFile 엔티티들을 불러오고, 유효하지 않은 이미지 ID라면 예외를 발생시킴
        try {
            updateImageFiles = updateImageIds.stream()
                    .map(id -> imageFileRepository.findById(id).get())
                    .collect(Collectors.toSet());
        } catch (NoSuchElementException e) {
            throw new ApplicationException(ErrorCode.INVALID_IMAGE_ID);
        }

        //원래 가지고 있던 이미지 중 업데이트 목록에 없는 것들은 삭제
        Set<ImageFile> originalImageFiles = boardContent.getImageFiles();
        originalImageFiles.stream()
                .filter(image -> !updateImageFiles.contains(image))
                .forEach(image -> imageFileRepository.delete(image));

        //업데이트 목록에 있는 이미지 중 원래 목록에 없는 것들은 헤더 설정
        updateImageFiles.stream()
                .filter(image -> !originalImageFiles.contains(image))
                .forEach(image -> {
                    if(image.getBoardContent() != null) {
                        throw new ApplicationException(ErrorCode.IMAGE_BELONG_TO_OTHER_POST);
                    }
                    image.updateBoardContent(boardContent);
                });
    }
}

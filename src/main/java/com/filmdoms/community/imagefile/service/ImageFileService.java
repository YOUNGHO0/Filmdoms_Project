package com.filmdoms.community.imagefile.service;

import com.filmdoms.community.account.exception.ApplicationException;
import com.filmdoms.community.account.exception.ErrorCode;
import com.filmdoms.community.board.data.BoardContent;
import com.filmdoms.community.imagefile.data.entitiy.ImageFile;
import com.filmdoms.community.imagefile.repository.ImageFileRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}

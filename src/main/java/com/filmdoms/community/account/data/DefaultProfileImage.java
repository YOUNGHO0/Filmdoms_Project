package com.filmdoms.community.account.data;

import com.filmdoms.community.file.data.entity.File;
import com.filmdoms.community.file.repository.FileRepository;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Getter
@RequiredArgsConstructor
public class DefaultProfileImage {

    private final FileRepository fileRepository;
    private File defaultProfileImage;

    @PostConstruct
    public void init() {

        File file = File.builder()
                .uuidFileName("7f5fb6d2-40fa-4e3d-81e6-a013af6f4f23.png")
                .originalFileName("original_file_name")
                .build();
        defaultProfileImage = fileRepository.save(file);

    }

}



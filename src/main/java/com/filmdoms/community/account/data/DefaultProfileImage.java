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
                .uuidFileName("8ffbe57a-741e-46a2-bbda-f507ed6c51fb.png")
                .originalFileName("profile")
                .build();
        defaultProfileImage = fileRepository.save(file);

    }

}



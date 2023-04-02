package com.filmdoms.community.article.data.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.filmdoms.community.article.data.dto.notice.NoticeRequestDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.filmdoms.community.account.exception.ValidationMessage.*;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "articleType",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(name = "DEFAULT", value =ArticleRequestDto.class),
        @JsonSubTypes.Type(name = "NOTICE", value = NoticeRequestDto.class)

})
@RequiredArgsConstructor
@Getter
public abstract class ParentRequestDto {

    private ArticleType articleType;

    public enum ArticleType {
        DEFAULT,NOTICE
    }
    @NotBlank(message = TITLE_NOT_BLANK)
    @Size(max = 100, message = TITLE_SIZE)
    private String title;
    @NotBlank(message = CONTENT_NOT_BLANK)
    @Size(max = 10000, message = CONTENT_SIZE)
    private String content;

}

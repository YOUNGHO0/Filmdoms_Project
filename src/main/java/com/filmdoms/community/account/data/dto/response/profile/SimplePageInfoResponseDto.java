package com.filmdoms.community.account.data.dto.response.profile;

import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class SimplePageInfoResponseDto {

    private int pageNumber;
    private int pageSize;
    private int totalPages;
    private long totalElements;
    private int numberOfElements;

    private SimplePageInfoResponseDto(Page page) {
        this.pageNumber = page.getNumber();
        this.pageSize = page.getSize();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.numberOfElements = page.getContent().size();
    }

    public static SimplePageInfoResponseDto from(Page page) {
        return new SimplePageInfoResponseDto(page);
    }
}

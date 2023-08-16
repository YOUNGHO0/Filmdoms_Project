package com.filmdoms.community.article.controller;

import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.constant.Tag;
import com.filmdoms.community.article.data.dto.response.editor.ArticleEditorResponseDto;
import com.filmdoms.community.article.data.dto.response.enuminfo.CategoryListDto;
import com.filmdoms.community.article.data.dto.response.enuminfo.TagListDto;
import com.filmdoms.community.article.service.ArticleEditorService;
import com.filmdoms.community.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ArticleInfoController {

    private final ArticleEditorService articleEditorService;

    @GetMapping("/category")
    public Response getCategoryInfo() {
        List<CategoryListDto> categoryListDtos = Arrays.stream(Category.values()).map(CategoryListDto::from).collect(Collectors.toList());
        return Response.success(categoryListDtos);
    }

    @GetMapping("/{category}/tag")
    public Response getTagInCategory(@PathVariable Category category) {
        List<TagListDto> tagListDtos = Arrays.stream(Tag.values()).filter(tag -> tag.getCategory() == category).map(TagListDto::from).collect(Collectors.toList());
        return Response.success(tagListDtos);
    }

    @GetMapping("/tag")
    public Response getTag() {
        List<TagListDto> tagListDtos = Arrays.stream(Tag.values()).map(TagListDto::from).collect(Collectors.toList());
        return Response.success(tagListDtos);
    }

    @GetMapping("/editor/{category}")
    public Response getEditorNotice(@PathVariable Category category) {

        ArticleEditorResponseDto dto = null;

        switch (category) {
            case CRITIC -> dto = articleEditorService.getCriticNotice();
            case MOVIE -> dto = articleEditorService.getMovieNotice();
            case FILM_UNIVERSE -> dto = articleEditorService.getFilmUniverseNotice();
        }

        if (dto == null)
            return Response.error(ErrorCode.CATEGORY_DOES_NOT_MATCH.getMessage());
        else
            return Response.success(dto);

    }

}

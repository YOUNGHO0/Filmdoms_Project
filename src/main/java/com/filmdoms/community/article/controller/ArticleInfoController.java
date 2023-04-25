package com.filmdoms.community.article.controller;

import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.constant.Tag;
import com.filmdoms.community.article.data.dto.response.enuminfo.CategoryListDto;
import com.filmdoms.community.article.data.dto.response.enuminfo.TagListDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class ArticleInfoController {

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
}

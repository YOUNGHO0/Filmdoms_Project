package com.filmdoms.community.article.controller;

import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final AdminService adminService;

    @DeleteMapping("/{category}/all")
    public Response deleteAllArticles(@PathVariable Category category, @AuthenticationPrincipal AccountDto accountDto) {
        adminService.deleteAllArticlesService(category, accountDto);
        return Response.success();
    }

}

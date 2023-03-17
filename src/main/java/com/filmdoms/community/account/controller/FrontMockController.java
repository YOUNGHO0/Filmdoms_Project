package com.filmdoms.community.account.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontMockController {

    @GetMapping("/front/oauth2/google")
    public String testRedirect(HttpServletRequest request) {
        return "redirect:/login/oauth2/code/google?" + request.getQueryString();
    }
}

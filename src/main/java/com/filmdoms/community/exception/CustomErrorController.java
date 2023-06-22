package com.filmdoms.community.exception;

import com.filmdoms.community.account.data.dto.response.Response;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping(value = "/error")
    public ResponseEntity<?> error() {
        return ResponseEntity.status(ErrorCode.URI_NOT_FOUND.getStatus())
                .body(Response.error(ErrorCode.URI_NOT_FOUND.name()));
    }
}

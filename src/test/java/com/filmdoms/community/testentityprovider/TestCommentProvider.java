package com.filmdoms.community.testentityprovider;

import com.filmdoms.community.account.data.constant.AccountRole;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.article.data.entity.Article;
import com.filmdoms.community.comment.data.entity.Comment;

public class TestCommentProvider {

    private static int count;

    public static Comment get(Account author, Article article, Comment parentComment) {
        count++;
        Comment comment = Comment.builder()
                .isManagerComment(false)
                .content("test_comment_content_" + count)
                .author(author)
                .article(article)
                .parentComment(parentComment)
                .build();

        return comment;
    }
}

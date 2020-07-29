package com.project.publicNo.pojo.impl;

import com.project.publicNo.pojo.Response;
import lombok.Data;
import lombok.ToString;

@ToString
public class AddArticleResponse extends Response {
    private Integer articleId;

    public AddArticleResponse(boolean result, String reaponseMessage, Integer articleId) {
        super(result, reaponseMessage);
        this.articleId = articleId;
    }

    public AddArticleResponse() {
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }
}

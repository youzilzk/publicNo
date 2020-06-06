package com.project.publicNo.pojo.impl;

import com.project.publicNo.entity.Article;
import com.project.publicNo.pojo.Response;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;

@Data
@ToString
public class ArticleResponse extends Response {
    private ArrayList<Article> articles;
}

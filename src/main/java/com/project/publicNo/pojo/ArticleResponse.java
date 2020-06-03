package com.project.publicNo.pojo;

import com.project.publicNo.entity.Article;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;

@Data
@ToString
public class ArticleResponse extends Response {
    private ArrayList<Article> articles;
}

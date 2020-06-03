package com.project.publicNo.dao;

import com.project.publicNo.entity.Article;
import tk.mybatis.mapper.common.Mapper;

import java.util.ArrayList;

@org.apache.ibatis.annotations.Mapper
public interface ArticleDao{
    public ArrayList<Article> selectArticlesByUserId(int userId);
}

package com.project.publicNo.dao;

import com.project.publicNo.entity.Article;
import tk.mybatis.mapper.common.Mapper;
import java.util.ArrayList;

@org.apache.ibatis.annotations.Mapper
public interface ArticleDao extends Mapper<Article>{
    //查询用户的文章
    public ArrayList<Article> selectArticlesByUserId(int userId);
    //添加文章
    public int insertArticle(Article article);
}

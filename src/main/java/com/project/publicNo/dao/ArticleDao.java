package com.project.publicNo.dao;

import com.project.publicNo.entity.Article;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;
import java.util.ArrayList;

@org.apache.ibatis.annotations.Mapper
public interface ArticleDao extends Mapper<Article>{
    //查询用户的文章
    public ArrayList<Article> selectArticlesByUserId(@Param("userId")int userId, @Param("isSelf")int isSelf);
    //添加文章
    public int insertArticle(Article article);
    public void updateExposure(Integer articleId);
    public Article selectArticle(Integer articleId);
}

package com.project.publicNo.dao;


import com.project.publicNo.entity.ReadArticle;
import tk.mybatis.mapper.common.Mapper;

@org.apache.ibatis.annotations.Mapper
public interface ReadArticleDao extends Mapper<ReadArticle> {
    public int updateReadTime(ReadArticle readArticle);
}

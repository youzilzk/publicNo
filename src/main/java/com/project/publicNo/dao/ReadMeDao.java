package com.project.publicNo.dao;

import com.project.publicNo.entity.ReadArticle;
import com.project.publicNo.pojo.ReadMeData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

@Mapper
public interface ReadMeDao {
    public ArrayList<ReadMeData> getReadMeInfo(Integer userId);
    public ArrayList<ReadArticle> selectReadOtherInfoByMe(@Param("authorId")Integer authorId, @Param("readerId")Integer readerId);
}

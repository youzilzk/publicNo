package com.project.publicNo.dao;

import com.project.publicNo.entity.ReadArticle;
import com.project.publicNo.entity.ReadMe;
import com.project.publicNo.pojo.ReadMeData;
import org.apache.ibatis.annotations.Param;
import java.util.ArrayList;
import tk.mybatis.mapper.common.Mapper;

@org.apache.ibatis.annotations.Mapper
public interface ReadMeDao extends Mapper<ReadMe>{
    public ArrayList<ReadMeData> getReadMeInfo(Integer userId);
    public ArrayList<ReadArticle> selectReadOtherInfoByMe(@Param("authorId")Integer authorId, @Param("readerId")Integer readerId);
    public int selectCount(ReadMe readMe);
    public int updateReadTime(ReadMe readMe);
}

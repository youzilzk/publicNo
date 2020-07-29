package com.project.publicNo.dao;

import com.project.publicNo.pojo.RankData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.ArrayList;

@Mapper
public interface InitDao {
    public int selectReadPeas(int userId);
    public int selectArticleCount(@Param("userId")int userId, @Param("isSelf")int isSelf);
    public int selectWaitReadCount(int userId);
    public ArrayList<RankData> selectRankData();
    public ArrayList<Integer> selectAllRankUser();
    public int selectExposure(int userId);
}

package com.project.publicNo.dao;

import com.project.publicNo.pojo.RankData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.ArrayList;

@Mapper
public interface InitDao {
    public Integer selectReadPeas(int userId);
    public Integer selectArticleCount(@Param("userId")int userId, @Param("isSelf")int isSelf);
    public Integer selectWaitReadCount(int userId);
    public ArrayList<RankData> selectRankData();
    public ArrayList<Integer> selectAllRankUser();
    public Integer selectExposure(int userId);
    public ArrayList<Integer> selectArticleIdsAndReadedToday(int userId);
}

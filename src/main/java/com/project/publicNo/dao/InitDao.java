package com.project.publicNo.dao;

import com.project.publicNo.pojo.RankData;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface InitDao {
    public int selectReadPeas(int userId);
    public int selectArticleCount(int userId);
    public int selectWaitReadCount(int userId);
    public ArrayList<RankData> selectRankData();
    public ArrayList<Integer> selectAllRankUser();
}

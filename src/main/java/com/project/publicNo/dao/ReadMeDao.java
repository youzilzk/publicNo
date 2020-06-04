package com.project.publicNo.dao;

import com.project.publicNo.pojo.ReadMeData;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface ReadMeDao {
    public ArrayList<ReadMeData> getReadMeInfo(Integer userId);
}

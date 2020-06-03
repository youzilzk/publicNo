package com.project.publicNo.dao;

import com.project.publicNo.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface ReadMeDao {
    public ArrayList<User> getReadMeInfo(Integer userId);
}

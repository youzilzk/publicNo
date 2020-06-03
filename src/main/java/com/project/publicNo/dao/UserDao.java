package com.project.publicNo.dao;

import com.project.publicNo.entity.User;
import tk.mybatis.mapper.common.Mapper;

@org.apache.ibatis.annotations.Mapper
public interface UserDao extends Mapper<User> {
    public int updateReadPeas(User user);
}

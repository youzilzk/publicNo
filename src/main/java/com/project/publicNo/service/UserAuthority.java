package com.project.publicNo.service;

import com.project.publicNo.dao.UserDao;
import com.project.publicNo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserAuthority {
    @Autowired
    private UserDao userDao;
    public UserDetails getUserAuthority(int userId){
        User user=new User();
        user.setUserId(userId);
        User selectOne = userDao.selectOne(user);
        List<GrantedAuthority> role_login = AuthorityUtils.commaSeparatedStringToAuthorityList("LOGIN");
        return new  org.springframework.security.core.userdetails.User(Integer.toString(userId),selectOne.getPassword(),role_login);
    }
}

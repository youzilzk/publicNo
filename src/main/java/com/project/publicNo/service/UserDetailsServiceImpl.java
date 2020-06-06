package com.project.publicNo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserAuthority userAuthority;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //直接远程调用UserAuthority类
        return userAuthority.getUserAuthority(Integer.parseInt(username));
    }
}


 package com.project.publicNo.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@Order(-1)
@EnableGlobalMethodSecurity(prePostEnabled=true)
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) throws Exception {
        //忽略哪些资源不用security来管理
        web.ignoring().antMatchers("/api/userInfo","/api/initPage","/api/articles","/api/readMeInfo");
    }

    //采用bcrypt对密码进行编码,也可以new 一个MD5加密, 看需要
    @Bean
    public PasswordEncoder passwordEncoder() {
        //对密码使用密文储存,如果明文存储,可使用自定义的SimplePasswordEncoder类
        return new BCryptPasswordEncoder();
    }
//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//        //配置策略,比如防止csrf攻击,根据需求,不配就使用默认的也行
//        http.csrf().disable()
//                .httpBasic().and()
//                .formLogin()
//                .and()
//                .authorizeRequests().anyRequest().authenticated();
//
//    }

}


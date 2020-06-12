package com.project.publicNo.controller;

import com.project.publicNo.entity.User;
import com.project.publicNo.pojo.*;
import com.project.publicNo.service.PublicNoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class PublicNoController {
    @Autowired
    private PublicNoService publicNoService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @RequestMapping(value = "/userInfo")
    public Response userInfo(@RequestParam(value = "userId") Integer userId){
        return publicNoService.loginService(userId);
    }

    @RequestMapping(value = "/initPage")
    public Response initPage(){
        return publicNoService.initService();
    }

    @RequestMapping("/articles")
    public Response getArticles(@RequestParam(value = "userId") Integer userId){
        return publicNoService.getArticles(userId);
    }

    @RequestMapping("/addArticle")
    //业务层实事务控制,异常捕获放在控制层
    public Response addArticle(@RequestBody Map<String,String> map){
       try {
           int i = publicNoService.addArticle(map);
           if(i==0){
               return new Response(false,"阅豆不足!");
           }
           return new Response(true,"发布文章成功!");
       }catch (Exception e){
           e.printStackTrace();
           return new Response(false,"发布文章失败!");
       }
    }

    @RequestMapping("/readMeInfo")
    public Response getReadMeInfo(@RequestParam(value = "userId") Integer userId){
        return publicNoService.getReadMeInfo(userId);
    }

    @RequestMapping("/delArticle")
    public Response delArticle(@RequestParam(value = "articleId") Integer articleId){
        try {
            int i = publicNoService.delArticle(articleId);
            switch (i){
                case 0:return new Response(false,"文章不存在!");
                case 1:return new Response(true,"下架文章成功!");
                case 2:return new Response(false,"文章已下架,勿重复操作!");
                default:return new Response(false,"系统异常!");
            }
        }catch (Exception e){
            e.printStackTrace();
            return new Response(false,"下架文章失败!");
        }
    }

    @RequestMapping("/reAddArticle")
    public Response putArticle(@RequestParam(value = "articleId") Integer articleId){
        try {
            int i = publicNoService.reAddArticle(articleId);
            switch (i){
                case 0:return new Response(false,"文章不存在!");
                case 1:return new Response(true,"上架文章成功!");
                case 2:return new Response(false,"文章已上架,勿重复操作!");
                default:return new Response(false,"系统异常!");
            }
        }catch (Exception e){
            e.printStackTrace();
            return new Response(false,"上架文章失败!");
        }
    }


    @RequestMapping("/login")
    public Response loginPage(HttpServletResponse response,@RequestParam(value = "userId") Integer userId,@RequestParam(value = "password") String password){
        User user = publicNoService.getUser(userId);
        if(user.getPassword()==null){
            return new Response(false,"用户不存在!");
        }
        boolean matches = passwordEncoder.matches(password, user.getPassword());
        if(!matches){
            return new Response(false,"用户或密码错误!");
        }else {//userId+user.getPassword()
            String encode = passwordEncoder.encode(new String(userId + user.getPassword()));
            Cookie cookie = new Cookie(userId.toString(), encode);
            response.addCookie(cookie);
            return new Response(true,"登录成功!");
        }
    }
}

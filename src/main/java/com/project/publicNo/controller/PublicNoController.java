package com.project.publicNo.controller;

import com.project.publicNo.pojo.*;
import com.project.publicNo.service.PublicNoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class PublicNoController {
    @Autowired
    private PublicNoService publicNoService;

    @RequestMapping(value = "/userInfo")
    public Response userInfo(@RequestParam(value = "userId") Integer userId){
        return publicNoService.loginService(userId);
    }

    @RequestMapping(value = "/initPage")
    public Response initPage(@RequestParam(value = "userId") Integer userId){
        return publicNoService.initService(userId);
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
            publicNoService.delArticle(articleId);
            return new Response(true,"下架文章成功!");
        }catch (Exception e){
            e.printStackTrace();
            return new Response(false,"下架文章失败!");
        }
    }
}

package com.project.publicNo.controller;

import com.project.publicNo.pojo.ArticleResponse;
import com.project.publicNo.pojo.InitResponse;
import com.project.publicNo.pojo.LoginResponse;
import com.project.publicNo.pojo.Response;
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
    public LoginResponse userInfo(@RequestParam(value = "userId") Integer userId){
        return publicNoService.loginService(userId);
    }

    @RequestMapping(value = "/initPage")
    public InitResponse initPage(@RequestParam(value = "userId") Integer userId){
        return publicNoService.initService(userId);
    }

    @RequestMapping("/articles")
    public ArticleResponse getArticles(@RequestParam(value = "userId") Integer userId){
        return publicNoService.getArticles(userId);
    }

    @RequestMapping("/addArticle")
    public Response addArticle(@RequestBody Map<String,String> map){
       try {
           publicNoService.addArticle(map);
           return new Response(true);
       }catch (Exception e){
           return new Response(false);
       }
    }
}

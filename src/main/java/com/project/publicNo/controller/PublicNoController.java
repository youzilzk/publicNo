package com.project.publicNo.controller;

import com.project.publicNo.entity.User;
import com.project.publicNo.pojo.*;
import com.project.publicNo.pojo.impl.LoginResponse;
import com.project.publicNo.service.PublicNoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
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
    public Response userInfo(@RequestParam(value = "userId") Integer userId, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String userId_session = "";
        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equals("uid")) {
                userId_session = cookies[i].getValue();
                break;
            }
        }
        if (userId_session.equals("") || !userId_session.equals(userId.toString())) {
            String isSelf = "0";
            return publicNoService.loginService(userId, isSelf);
        } else {
            String isSelf = "1";
            return publicNoService.loginService(userId, isSelf);
        }
    }

    @RequestMapping(value = "/initPage")
    public Response initPage() {
        return publicNoService.initService();
    }

    @RequestMapping("/articles")
    public Response getArticles(@RequestParam(value = "userId") Integer userId,HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String userId_session = "";
        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equals("uid")) {
                userId_session = cookies[i].getValue();
                break;
            }
        }
        if (userId_session.equals("") || !userId_session.equals(userId.toString())) {
            boolean a = userId_session.equals(userId);
            String isSelf = "0";
            return publicNoService.getArticles(userId, isSelf);
        } else {
            String isSelf = "1";
            return publicNoService.getArticles(userId, isSelf);
        }
    }

    @RequestMapping("/addArticle")
    //业务层实事务控制,异常捕获放在控制层
    public Response addArticle(@RequestParam(value = "userId") String userId, @RequestParam(value = "title") String title, @RequestParam(value = "articleLink") String articleLink, @RequestParam(value = "isTop") String isTop) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("userId", userId);
            map.put("title", title);
            map.put("articleLink", articleLink);
            map.put("isTop", isTop);
            int i = publicNoService.addArticle(map);
            if (i == 0) {
                return new Response(false, "阅豆不足!");
            }
            return new Response(true, "发布文章成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(false, "发布文章失败!");
        }
    }

    @RequestMapping("/readMeInfo")
    public Response getReadMeInfo(@RequestParam(value = "userId") Integer userId) {
        return publicNoService.getReadMeInfo(userId);
    }

    @RequestMapping("/delArticle")
    public Response delArticle(@RequestParam(value = "userId") Integer userId, @RequestParam(value = "articleId") Integer articleId) {
        try {
            if (!publicNoService.userMatchArticle(userId, articleId)) {
                return new Response(false, "用户和文章不匹配");
            }
            int i = publicNoService.delArticle(articleId);
            switch (i) {
                case 0:
                    return new Response(false, "文章不存在!");
                case 1:
                    return new Response(true, "删除文章成功!");
                case 2:
                    return new Response(false, "文章已删除,勿重复操作!");
                default:
                    return new Response(false, "系统异常!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(false, "文章不存在,删除失败!");
        }
    }

    @RequestMapping("/reAddArticle")
    public Response putArticle(@RequestParam(value = "userId") Integer userId, @RequestParam(value = "articleId") Integer articleId) {
        try {
            if (!publicNoService.userMatchArticle(userId, articleId)) {
                return new Response(false, "用户和文章不匹配!");
            }
            int i = publicNoService.reAddArticle(articleId);
            switch (i) {
                case 0:
                    return new Response(false, "文章不存在!");
                case 1:
                    return new Response(true, "上架文章成功!");
                case 2:
                    return new Response(false, "文章已上架,勿重复操作!");
                default:
                    return new Response(false, "系统异常!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(false, "文章不存在,上架失败!");
        }
    }

    @RequestMapping("/removeArticle")
    public Response popArticle(@RequestParam(value = "userId") Integer userId, @RequestParam(value = "articleId") Integer articleId) {
        try {
            if (!publicNoService.userMatchArticle(userId, articleId)) {
                return new Response(false, "用户和文章不匹配!");
            }
            int i = publicNoService.removeArticle(articleId);
            switch (i) {
                case 0:
                    return new Response(false, "文章不存在!");
                case 1:
                    return new Response(true, "下架文章成功!");
                case 2:
                    return new Response(false, "文章已下架,勿重复操作!");
                default:
                    return new Response(false, "系统异常!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(false, "文章不存在,下架失败!");
        }
    }


    @RequestMapping("/login")
    public Response loginPage(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "openid") String openid) {
        try {
            int loginType = 2;
            User user = publicNoService.getUser(openid);
            if (user == null) {
                loginType = 1;
                //这是新用户,立即注册
                publicNoService.addUser(openid);
                user = publicNoService.getUser(openid);
            }
            String userId = user.getUserId().toString();
            Cookie uid = new Cookie("uid", userId);
            Cookie token = new Cookie(userId, passwordEncoder.encode(new String(openid + userId)));//token=openid+userId
            response.addCookie(uid);
            response.addCookie(token);
            //存session
            request.getSession().setAttribute(userId, openid);
            return new LoginResponse(true, "登录成功!", Integer.parseInt(userId), loginType);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(false, "系统异常!");

        }
    }

    @RequestMapping("/competeUserInfo")
    public Response competeUserInfo(@RequestBody User user) {
        try {
            publicNoService.competeUser(user);
            return new Response(true, "完善用户信息成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(false, "系统异常!");
        }
    }
}

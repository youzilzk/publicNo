package com.project.publicNo.controller;

import com.alibaba.fastjson.JSON;
import com.project.publicNo.entity.User;
import com.project.publicNo.pojo.*;
import com.project.publicNo.pojo.impl.LoginResponse;
import com.project.publicNo.service.PublicNoService;
import com.project.publicNo.utils.ImgUtil;
import com.project.publicNo.utils.QRCodeUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;


@RequestMapping("/api")
@Controller
@ResponseBody
public class PublicNoController {
    @Autowired
    private PublicNoService publicNoService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**>>>>>>>>>>>>>>>用户信息>>>>>>>>>>>>>>>**/
    @RequestMapping(value = "/userInfo")
    public Response userInfo(@RequestParam(value = "userId") Integer userId, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String userId_session = "";
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals("uid")) {
                    userId_session = cookies[i].getValue();
                    break;
                }
            }
        }
        if (userId_session.equals("") || !userId_session.equals(userId.toString())) {
            String isSelf = "0";
            return publicNoService.loginService(userId, isSelf);
        } else {
            String isSelf = "1";
            Response response = publicNoService.loginService(userId, isSelf);
            return response;
        }
    }

    @RequestMapping("/login")
    public Response loginPage(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "openid") String openid) {
        Cookie[] cookies = request.getCookies();
        String shareId = "";
        //尝试获取shareId
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals("shareId")) {
                    shareId = cookies[i].getValue();
                    break;
                }
            }
        }
        //如果用户来自他人分享,则对分享用户增加阅豆奖励
        if (!shareId.equals("")) {
            publicNoService.addReadpeaForShareUser(Integer.valueOf(shareId), 5);
        }
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
            Cookie token = new Cookie("token", passwordEncoder.encode(new String(openid + userId)));//token=openid+userId
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
    public Response competeUserInfo(@RequestParam Integer userId,@RequestParam String nickname,@RequestParam String picUrl,@RequestParam String phone) {
        try {
            User user = new User(userId, nickname, picUrl, phone);
            publicNoService.competeUser(user);
            return new Response(true, "完善用户信息成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(false, "系统异常!");
        }
    }

    @RequestMapping("/share")
    public void share(@Param("userId") Integer userId, HttpServletResponse response) throws Exception {
        try {
            response.setHeader("Content-Type","image/jpg");
            String insertImgPath=publicNoService.getPicUrl(userId);
            System.out.println(insertImgPath);
            BufferedImage qrcodeImage = QRCodeUtil.encode("http://huyue.group:8000/api/visitor?shareId=" + userId, insertImgPath, true);
            InputStream inputStream = ImgUtil.load(qrcodeImage, "img/background.jpg");
            ServletOutputStream outputStream = response.getOutputStream();
            byte[] bytes = new byte[1024];
            while (inputStream.read(bytes) != -1) {
                outputStream.write(bytes, 0, bytes.length);
                outputStream.flush();
            }
            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("application/json;charset=utf-8");
            PrintWriter writer = response.getWriter();
            String json = JSON.toJSONString(new Response(false, "系统错误,请检查请求参数!"));
            writer.write(json);
            writer.flush();
            writer.close();
        }
    }

    @RequestMapping("/visitor")
    public void visitor(@Param("shareId") String shareId, HttpServletResponse response) throws Exception {
        Cookie cookie = new Cookie("shareId", shareId);
        //设置cookie为一周
        cookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(cookie);
        response.sendRedirect("http://huyue.group/#/");
    }

    /**<<<<<<<<<<<<<<<用户信息<<<<<<<<<<<<<<<**/

    /**>>>>>>>>>>>>>>>文章操作>>>>>>>>>>>>>>>**/
    @RequestMapping(value = "/initPage")
    public Response initPage() {
        return publicNoService.initService();
    }

    @RequestMapping("/articles")
    public Response getArticles(@RequestParam(value = "userId") Integer userId, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String userId_cookie = "";
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals("uid")) {
                    userId_cookie = cookies[i].getValue();
                    break;
                }
            }
        }
        if (userId_cookie.equals("") || !userId_cookie.equals(userId.toString())) {
            String isSelf = "0";
            return publicNoService.getArticles(userId, isSelf, userId_cookie);
        } else {
            String isSelf = "1";
            return publicNoService.getArticles(userId, isSelf, userId_cookie);
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

    @RequestMapping("/readSuccess")
    public Response readSuccess(@Param("userId") String userId, @Param("articleId") String articleId) {
        try {
            int i = publicNoService.readSuccess(userId, articleId);
            if (i == 1) {
                return new Response(true, "更新数据成功!");
            } else {
                return new Response(false, "更新数据失败!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(false, "更新数据失败,系统错误!");
        }
    }

    /**<<<<<<<<<<<<<<<文章操作<<<<<<<<<<<<<<<**/
}

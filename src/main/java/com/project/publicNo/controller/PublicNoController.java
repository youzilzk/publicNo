package com.project.publicNo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.project.publicNo.entity.User;
import com.project.publicNo.pojo.*;
import com.project.publicNo.pojo.impl.AddArticleResponse;
import com.project.publicNo.pojo.impl.LoginResponse;
import com.project.publicNo.service.PublicNoService;
import com.project.publicNo.utils.ImgUtil;
import com.project.publicNo.utils.QRCodeUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;


@RequestMapping("/api")
@Controller
@ResponseBody
public class PublicNoController {
    @Autowired
    private PublicNoService publicNoService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * >>>>>>>>>>>>>>>用户信息>>>>>>>>>>>>>>>
     **/
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

    //获取授权数据
    @RequestMapping("/grantData")
    public Object grantData(@RequestParam String code) {
        RestTemplate restTemplate = new RestTemplate();
        //解决中文乱码
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        //获取access_token
        String access_token_url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx317cf7687db0f67f&secret=00d859baa981685f6c7a44082bf0031e&code=" + code + "&grant_type=authorization_code";
        ResponseEntity<String> responseEntity = restTemplate.exchange(access_token_url, HttpMethod.GET, null, String.class);
        JSONObject json = JSON.parseObject(responseEntity.getBody());
        String openid = (String) json.get("openid");
        String access_token = (String) json.get("access_token");
        //获取微信用户信息
        String wx_user_info_url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token + "&openid=" + openid;
        System.out.println(wx_user_info_url);
        ResponseEntity<String> exchange = restTemplate.exchange(wx_user_info_url, HttpMethod.GET, null, String.class);
        return JSON.parseObject(exchange.getBody());
    }

    @RequestMapping("/login")
    public Response loginPage(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "openid") String openid) {
     /*   Cookie[] cookies = request.getCookies();
        String shareId = "";
        //尝试获取shareId
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals("shareId")) {
                    shareId = cookies[i].getValue();
                    break;
                }
            }
        }*/

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
        /*    //如果用户来自他人分享,且是新用户,则对分享用户增加阅豆奖励
            if (!shareId.equals("")&&loginType==1) {
                //销毁该cookie
                Cookie cookie = new Cookie("shareId", "");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
                publicNoService.addReadpeaForShareUser(Integer.valueOf(shareId), 5);
            }*/
            return new LoginResponse(true, "登录成功!", Integer.parseInt(userId), loginType);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(false, "系统异常!");

        }
    }

    @RequestMapping("/competeUserInfo")
    public Response competeUserInfo(@RequestParam Integer userId, @RequestParam String nickname, @RequestParam String picUrl, @RequestParam String phone) {
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
            response.setHeader("Content-Type", "image/jpg");
            String insertImgPath = publicNoService.getPicUrl(userId);
            System.out.println(insertImgPath);
            BufferedImage qrcodeImage = QRCodeUtil.encode("http://huyue.group?shareId=" + userId, insertImgPath, true);
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
    public Response visitor(@Param("shareId") String shareId, HttpServletResponse response) throws Exception {
        /*Cookie cookie = new Cookie("shareId", shareId);
        //设置cookie为一周
        cookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(cookie);
        response.sendRedirect("http://huyue.group/#/");*/
        //奖励阅豆
        publicNoService.addReadpeaForUser(Integer.valueOf(shareId), 5);
        return new Response(true,"奖励成功!");
    }

    /**<<<<<<<<<<<<<<<用户信息<<<<<<<<<<<<<<<**/

    /**
     * >>>>>>>>>>>>>>>文章操作>>>>>>>>>>>>>>>
     **/
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
            int articleId = publicNoService.addArticle(map);
            //articleId为0表示阅豆不足
            if (articleId == 0) {
                return new Response(false, "阅豆不足!");
            }
            return new AddArticleResponse(true, "发布文章成功!",articleId);
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

    /**<<<<<<<<<<<<<<<反馈建议<<<<<<<<<<<<<<<**/

    @RequestMapping("/feedback")
    public Response feedback(@Param("userId") Integer userId, @Param("phone") String phone, @Param("feedbackType") Integer feedbackType, @Param("feedbackDesc") String feedbackDesc) {
        try {
            int feedback = publicNoService.feedback(userId, phone, feedbackType, feedbackDesc);
            if(feedback==1){
                publicNoService.addReadpeaForUser(userId,5);
                return new Response(true, "反馈成功,奖励5个阅豆!");
            }else {
                return new Response(false, "提交反馈时失败!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new Response(false, "反馈失败,系统出了点小问题!");
        }
    }

    /**<<<<<<<<<<<<<<<反馈建议<<<<<<<<<<<<<<<**/
}

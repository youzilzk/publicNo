package com.project.publicNo.config;

import com.alibaba.fastjson.JSON;
import com.project.publicNo.pojo.Response;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class SimpleInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object json = JSON.toJSON(new Response(false, "进行此操作,请先登录!"));

        System.out.println(json);
            return true;
    }
}

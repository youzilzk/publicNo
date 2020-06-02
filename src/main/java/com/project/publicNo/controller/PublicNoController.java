package com.project.publicNo.controller;

import com.project.publicNo.pojo.InitResponse;
import com.project.publicNo.pojo.LoginResponse;
import com.project.publicNo.service.PublicNoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
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
}

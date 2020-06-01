package com.project.publicNo.controller;

import com.project.publicNo.pojo.ResponseBody;
import com.project.publicNo.service.PublicNoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController

public class PublicNoController {
    @Autowired
    private PublicNoService publicNoService;
    @RequestMapping("/initPage")
    @CrossOrigin
    public ResponseBody initPage(@RequestParam(value = "userId") Integer userId){
        return publicNoService.initService(userId);
    }
}

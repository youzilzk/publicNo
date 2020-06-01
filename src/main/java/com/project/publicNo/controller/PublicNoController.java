package com.project.publicNo.controller;

import com.project.publicNo.pojo.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublicNoController {

    public ResponseBody initIndex(@RequestBody Integer userId){

        return null;
    }
}

package com.project.publicNo.pojo.impl;

import com.project.publicNo.pojo.Response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
public class LoginResponse extends Response {
    private Integer userId;
    private int loginType;
    public LoginResponse(){
    }
    public LoginResponse(boolean result,String reaponseMessage,int userId,int loginType){
        this.loginType=loginType;//1-新用户,2-老用户
        super.result=result;
        this.userId=userId;
        super.reaponseMessage=reaponseMessage;
    }
}

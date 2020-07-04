package com.project.publicNo.pojo.impl;

import com.project.publicNo.pojo.Response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class UserInfoResponse extends Response {
    private Integer userId;
    private String nickname;
    private String openid;
    private String picUrl;
    private Integer readPeas;
    private String phone;
    private String registerTime;
    private Integer articleCount;
    private Integer waitReadCount;
}

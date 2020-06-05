package com.project.publicNo.pojo;

import com.project.publicNo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class UserInfoResponse extends Response {
    private Integer userId;
    private String nickname;
    private String password;
    private String picUrl;
    private Integer readPeas;
    private String phone;
    private String registerTime;
    private Integer articleCount;
    private Integer waitReadCount;
}

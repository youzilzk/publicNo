package com.project.publicNo.pojo;

import com.project.publicNo.entity.User;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class LoginResponse extends Response{
    private User user;
}

package com.project.publicNo.pojo;

import com.project.publicNo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse extends Response{
    private User user;
}

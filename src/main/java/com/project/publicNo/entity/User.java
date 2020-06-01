package com.project.publicNo.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@ToString
@Table(name = "t_user")
public class User {
    @Id
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "nickname")
    private String nickname;
    @Column(name = "password")
    private String password;
    @Column(name = "pic_url")
    private String picUrl;
    @Column(name = "read_peas")
    private Integer readPeas;
    @Column(name = "phone")
    private Integer phone;
    @Column(name = "register_time")
    private Integer registerTime;
}

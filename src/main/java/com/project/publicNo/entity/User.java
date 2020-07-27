package com.project.publicNo.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;


@ToString
@Table(name = "t_user")
public class User {
    @Id
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "nickname")
    private String nickname;
    @Column(name = "openid")
    private String openid;
    @Column(name = "pic_url")
    private String picUrl;
    @Column(name = "read_peas")
    private Integer readPeas;
    @Column(name = "phone")
    private String phone;
    @Column(name = "register_time")
    private String registerTime;

    public User() {
    }

    public User(Integer userId, String nickname, String picUrl, String phone) {
        this.userId = userId;
        this.nickname = nickname;
        this.picUrl = picUrl;
        this.phone = phone;
    }

    public User(Integer userId, String nickname, String openid, String picUrl, Integer readPeas, String phone, String registerTime) {
        this.userId = userId;
        this.nickname = nickname;
        this.openid = openid;
        this.picUrl = picUrl;
        this.readPeas = readPeas;
        this.phone = phone;
        this.registerTime = registerTime;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public Integer getReadPeas() {
        return readPeas;
    }

    public void setReadPeas(Integer readPeas) {
        this.readPeas = readPeas;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }
}

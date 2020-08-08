package com.project.publicNo.entity;


import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@ToString
@Table(name = "t_feedback")
@Data
public class Feedback {
    @Id
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "phone")
    private String phone;
    @Column(name = "fd_type")
    private Integer feedbackType;
    @Column(name = "fd_desc")
    private String feedbackDdesc;

    public Feedback(Integer userId, String phone, Integer feedbackType, String feedbackDdesc) {
        this.userId = userId;
        this.phone = phone;
        this.feedbackType = feedbackType;
        this.feedbackDdesc = feedbackDdesc;
    }
}

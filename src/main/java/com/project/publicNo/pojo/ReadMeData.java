package com.project.publicNo.pojo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ReadMeData {
    private Integer userId;
    private String nickname;
    private String picUrl;
    private Integer readPeas;
    private Integer phone;
    private String registerTime;
    private String readTime;
}

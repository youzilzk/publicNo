package com.project.publicNo.pojo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RankData {
    private String publisher;//发布人
    private String userPic;//用户头像链接
    private String articleTitle;//文章标题
    private String exposure;//曝光度
    private String articleUrl;//文章地址
}

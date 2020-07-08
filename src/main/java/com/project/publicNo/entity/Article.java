package com.project.publicNo.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;


@ToString
@Table(name = "t_article")
public class Article {
    @Id
    @Column(name = "article_id")
    private Integer articleId;
    @Column(name = "title")
    private String title;
    @Column(name = "article_link")
    private String articleLink;
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "isTop")
    private Integer isTop;
    @Column(name = "delete_flg")
    //1-删除 0-未删除
    private Integer deleteFlg;
    @Column(name = "article_status")
    //1-已上架 0-已下架
    private Integer articleStatus;
    //曝光度
    @Column(name = "exposure")
    private Integer exposure;

    //非映射字段
    private Integer readStatus;
    private String readTime;

    public Article() {
        this.readStatus=0;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArticleLink() {
        return articleLink;
    }

    public void setArticleLink(String articleLink) {
        this.articleLink = articleLink;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getIsTop() {
        return isTop;
    }

    public void setIsTop(Integer isTop) {
        this.isTop = isTop;
    }

    public Integer getDeleteFlg() {
        return deleteFlg;
    }

    public void setDeleteFlg(Integer deleteFlg) {
        this.deleteFlg = deleteFlg;
    }

    public Integer getArticleStatus() {
        return articleStatus;
    }

    public void setArticleStatus(Integer articleStatus) {
        this.articleStatus = articleStatus;
    }

    public Integer getExposure() {
        return exposure;
    }

    public void setExposure(Integer exposure) {
        this.exposure = exposure;
    }

    public Integer getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(Integer readStatus) {
        this.readStatus = readStatus;
    }

    public String getReadTime() {
        return readTime;
    }

    public void setReadTime(String readTime) {
        this.readTime = readTime;
    }
}

package com.project.publicNo.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_user_article")
public class UserArticle {
    @Id
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "article_id")
    private Integer articleId;
}

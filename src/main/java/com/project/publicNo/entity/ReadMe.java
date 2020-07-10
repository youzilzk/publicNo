package com.project.publicNo.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@ToString
@Table(name = "t_readme")
public class ReadMe {
    @Id
    @Column(name = "reader_id")
    private Integer readerId;
    @Column(name = "author_id")
    private Integer authorId;
    @Column(name = "read_time")
    private String readTime;
    @Column(name = "read_count")
    private String readCount;
}

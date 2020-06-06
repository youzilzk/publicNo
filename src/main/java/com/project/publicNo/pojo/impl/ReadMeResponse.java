package com.project.publicNo.pojo.impl;

import com.project.publicNo.entity.User;
import com.project.publicNo.pojo.ReadMeData;
import com.project.publicNo.pojo.Response;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;

@Data
@ToString
public class ReadMeResponse extends Response {
    private ArrayList<ReadMeData> readMeData;
}

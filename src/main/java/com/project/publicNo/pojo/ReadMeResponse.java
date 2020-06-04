package com.project.publicNo.pojo;

import com.project.publicNo.entity.User;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;

@Data
@ToString
public class ReadMeResponse extends Response {
    private ArrayList<ReadMeData> readMeData;
}

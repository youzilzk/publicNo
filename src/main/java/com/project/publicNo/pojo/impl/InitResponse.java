package com.project.publicNo.pojo.impl;

import com.project.publicNo.pojo.RankData;
import com.project.publicNo.pojo.Response;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;

@Data
@ToString
public class InitResponse extends Response {
   private ArrayList<RankData> rankList;
}

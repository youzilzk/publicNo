package com.project.publicNo.pojo;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;

@Data
@ToString
public class InitResponse extends Response{
   private ArrayList<RankData> rankList;
}

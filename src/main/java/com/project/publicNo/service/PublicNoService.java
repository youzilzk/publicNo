package com.project.publicNo.service;

import com.project.publicNo.dao.InitDao;
import com.project.publicNo.dao.UserDao;
import com.project.publicNo.entity.User;
import com.project.publicNo.pojo.RankData;
import com.project.publicNo.pojo.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PublicNoService {

    @Autowired
    private InitDao initDao;
    @Autowired
    private UserDao userDao;
    public ResponseBody initService(int userId){
        ResponseBody responseBody = new ResponseBody();
        int readPeas = initDao.selectReadPeas(userId);
        responseBody.setReadPeas(readPeas);
        int articleCount = initDao.selectArticleCount(userId);
        responseBody.setArticleCount(articleCount);
        int waitReadCount = initDao.selectWaitReadCount(userId);
        responseBody.setWaitReadCount(waitReadCount);
        //List<User> users = userDao.selectAll();
        ArrayList<Integer> userIdList = initDao.selectAllRankUser();
        ArrayList<RankData> rankList=new ArrayList<>();
        for (int i = 0; i < userIdList.size(); i++) {
            RankData rankData = initDao.selectRankDataByUserId(userIdList.get(i));
            if(rankData==null){
                continue;
            }
            rankList.add(rankData);
        }
        responseBody.setRankList(rankList);
        responseBody.setResult(true);
        return responseBody;
    }
}

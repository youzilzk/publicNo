package com.project.publicNo.service;


import com.project.publicNo.dao.ArticleDao;
import com.project.publicNo.dao.InitDao;
import com.project.publicNo.dao.UserDao;
import com.project.publicNo.entity.Article;
import com.project.publicNo.entity.User;
import com.project.publicNo.pojo.ArticleResponse;
import com.project.publicNo.pojo.LoginResponse;
import com.project.publicNo.pojo.RankData;
import com.project.publicNo.pojo.InitResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class PublicNoService {

    @Autowired
    private InitDao initDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private ArticleDao articleDao;

    //获取登录的用户信息
    public LoginResponse loginService(int userId){
        User user=new User();
        user.setUserId(userId);
        User selectOne = userDao.selectOne(user);
        //把密码置空,防止网络中传输
        selectOne.setPassword("");
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setResult(true);
        loginResponse.setUser(selectOne);
        return loginResponse;
    }
    //初始化首页
    public InitResponse initService(int userId){
        InitResponse initResponse = new InitResponse();
        //根据用户id查询阅豆
        int readPeas = initDao.selectReadPeas(userId);
        initResponse.setReadPeas(readPeas);
        //查询该用户发布的文章数
        int articleCount = initDao.selectArticleCount(userId);
        initResponse.setArticleCount(articleCount);
        //查询待阅数
        int waitReadCount = initDao.selectWaitReadCount(userId);
        initResponse.setWaitReadCount(waitReadCount);
        //查询所有用户,并根据阅豆从大到小对用户排序
        ArrayList<Integer> userIdList = initDao.selectAllRankUser();
        ArrayList<RankData> rankList=new ArrayList<>();
        //查询阅读排行
        for (int i = 0; i < userIdList.size(); i++) {
            RankData rankData = initDao.selectRankDataByUserId(userIdList.get(i));
            if(rankData==null){
                continue;
            }
            rankList.add(rankData);
        }
        initResponse.setRankList(rankList);
        initResponse.setResult(true);
        return initResponse;
    }

    //获取用户所有文章
    public ArticleResponse getArticles(int userId){
        ArrayList<Article> articles = articleDao.selectArticlesByUserId(userId);
        ArticleResponse response = new ArticleResponse();
        response.setArticles(articles);
        response.setResult(true);
        return response;
    }
}

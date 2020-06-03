package com.project.publicNo.service;


import com.project.publicNo.dao.*;
import com.project.publicNo.entity.Article;
import com.project.publicNo.entity.User;
import com.project.publicNo.entity.UserArticle;
import com.project.publicNo.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class PublicNoService {

    @Autowired
    private InitDao initDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private UserArticleDao userArticleDao;
    @Autowired
    private ReadMeDao readMeDao;

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
        try {
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
            //ArrayList<Integer> userIdList = initDao.selectAllRankUser();
            //查询阅读排行
            ArrayList<RankData> rankDataList=initDao.selectRankData();
            //过滤掉文章url为空的数据
            ArrayList<RankData> rankList=new ArrayList<>();
            for (int i = 0; i < rankDataList.size(); i++) {
                if(rankDataList.get(i).getArticleUrl()==null||rankDataList.get(i).getArticleUrl().trim().equals("")){
                    continue;
                }
                rankList.add(rankDataList.get(i));
            }
            initResponse.setRankList(rankList);
            initResponse.setReaponseMessage("查询成功!");
            initResponse.setResult(true);
            return initResponse;
        }catch (Exception e){
            e.printStackTrace();
            InitResponse initResponse = new InitResponse();
            initResponse.setReaponseMessage("查询失败!");
            initResponse.setResult(false);
            return initResponse;
        }
    }

    //获取用户所有文章
    public ArticleResponse getArticles(int userId){
        ArrayList<Article> articles = articleDao.selectArticlesByUserId(userId);
        ArticleResponse response = new ArticleResponse();
        response.setArticles(articles);
        response.setResult(true);
        return response;
    }

    //添加文章信息
    @Transactional(rollbackFor = Exception.class)
    public void addArticle(Map<String,String> map){
        Article article = new Article();
        int userId = Integer.parseInt(map.get("userId"));
        int isTop = Integer.parseInt(map.get("isTop"));
        article.setTitle(map.get("title"));
        article.setArticleLink(map.get("articleLink"));
        article.setIsTop(isTop);
        //插入文章表
        articleDao.insertArticle(article);
        //添加用户和文章映射
        UserArticle userArticle = new UserArticle(userId,article.getArticleId());
        userArticleDao.insert(userArticle);
        //判断是否开启置顶
        if(isTop==1){
            int readPeas = Integer.parseInt(map.get("readPeas"));
            User user = new User();
            user.setUserId(userId);
            user.setReadPeas(readPeas);
            userDao.updateReadPeas(user);
        }
    }

    //查询阅我用户信息
    public ReadMeResponse getReadMeInfo(int userId){
        ArrayList<User> readMeInfo = readMeDao.getReadMeInfo(userId);
        ReadMeResponse readMeResponse = new ReadMeResponse();
        readMeResponse.setResult(true);
        readMeResponse.setReaponseMessage("查询阅我用户列表成功!");
        readMeResponse.setUsers(readMeInfo);
        return readMeResponse;
    }
}

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
       try {
           User user=new User();
           user.setUserId(userId);
           User selectOne = userDao.selectOne(user);
           //把密码置空,防止网络中传输
           selectOne.setPassword("");
           LoginResponse loginResponse = new LoginResponse(selectOne);
           loginResponse.setResult(true);
           loginResponse.setReaponseMessage("获取登录用户信息成功!");
           return loginResponse;
       }catch (Exception e){
           e.printStackTrace();
           LoginResponse loginResponse = new LoginResponse();
           loginResponse.setResult(false);
           loginResponse.setReaponseMessage("获取登录用户信息失败!");
           return loginResponse;
       }
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
            ArrayList<RankData> rankList=initDao.selectRankData();
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
      try {
          ArrayList<Article> articles = articleDao.selectArticlesByUserId(userId);
          ArticleResponse response = new ArticleResponse();
          response.setArticles(articles);
          response.setReaponseMessage("查询用户文章成功!");
          response.setResult(true);
          return response;
      }catch (Exception e){
          e.printStackTrace();
          ArticleResponse response = new ArticleResponse();
          response.setReaponseMessage("查询用户文章失败!");
          response.setResult(false);
          return response;
      }
    }

    //添加文章信息
    @Transactional(rollbackFor = Exception.class)
    public int addArticle(Map<String,String> map){
        Article article = new Article();
        int userId = Integer.parseInt(map.get("userId"));
        int isTop = Integer.parseInt(map.get("isTop"));
        int readPeas = Integer.parseInt(map.get("readPeas"));
        //如果用户开启置顶,则先判断用户阅豆是否足够置顶消耗
        if(isTop==1){
            int i = userDao.selectReadPeasByUserId(userId);
            if(i<readPeas){
                return 0;
            }
        }
        //插入文章表前初始化参数
        article.setTitle(map.get("title"));
        article.setArticleLink(map.get("articleLink"));
        article.setIsTop(isTop);
        //插入文章表
        articleDao.insertArticle(article);
        //添加用户和文章映射
        UserArticle userArticle = new UserArticle(userId,article.getArticleId());
        userArticleDao.insert(userArticle);
        //更新阅豆
        if(isTop==1){
            User user = new User();
            user.setUserId(userId);
            user.setReadPeas(readPeas);
            userDao.updateReadPeas(user);
        }
        return 1;
    }

    //查询阅我用户信息
    public ReadMeResponse getReadMeInfo(int userId){
        ArrayList<ReadMeData> readMeInfo = readMeDao.getReadMeInfo(userId);
        ReadMeResponse readMeResponse = new ReadMeResponse();
        readMeResponse.setResult(true);
        readMeResponse.setReaponseMessage("查询阅我用户列表成功!");
        readMeResponse.setReadMeData(readMeInfo);
        for (int i = 0; i < readMeInfo.size(); i++) {
            System.out.println(readMeInfo.get(i).toString());
        }
        return readMeResponse;
    }

    //删除文章
    @Transactional(rollbackFor = Exception.class)
    public void delArticle(int articleId){
        Article article = new Article();
        article.setArticleId(articleId);
        article.setDeleteFlg(1);
        articleDao.updateByPrimaryKeySelective(article);
    }
}

package com.project.publicNo.service;


import com.project.publicNo.dao.*;
import com.project.publicNo.entity.Article;
import com.project.publicNo.entity.User;
import com.project.publicNo.entity.UserArticle;
import com.project.publicNo.pojo.*;
import com.project.publicNo.pojo.impl.ArticleResponse;
import com.project.publicNo.pojo.impl.InitResponse;
import com.project.publicNo.pojo.impl.ReadMeResponse;
import com.project.publicNo.pojo.impl.UserInfoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    public Response loginService(int userId,String isSelf) {
        try {
            User user = new User();
            user.setUserId(userId);
            User selectOne = userDao.selectOne(user);
            //根据用户id查询阅豆
            int readPeas = initDao.selectReadPeas(userId);
            //查询该用户发布的文章数
            int articleCount = initDao.selectArticleCount(userId,Integer.parseInt(isSelf));
            //查询待阅数
            int waitReadCount = initDao.selectWaitReadCount(userId);
            UserInfoResponse userInfoResponse = new UserInfoResponse(
                    userId, selectOne.getNickname(), selectOne.getOpenid(),
                    selectOne.getPicUrl(), selectOne.getReadPeas(), selectOne.getPhone(),
                    selectOne.getRegisterTime(), articleCount, waitReadCount);
            userInfoResponse.setResult(true);
            userInfoResponse.setReaponseMessage("获取用户信息成功!");
            return userInfoResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(false, "获取用户信息失败!");
        }
    }

    //初始化首页
    public Response initService() {
        try {
            InitResponse initResponse = new InitResponse();
            //查询阅读排行
            ArrayList<RankData> rankList = initDao.selectRankData();
            initResponse.setRankList(rankList);
            initResponse.setReaponseMessage("查询成功!");
            initResponse.setResult(true);
            return initResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(false, "查询失败!");
        }
    }

    //获取用户所有文章
    public Response getArticles(int userId,String isSelf) {
        try {
            ArrayList<Article> articles = articleDao.selectArticlesByUserId(userId,Integer.parseInt(isSelf));
            ArticleResponse response = new ArticleResponse();
            response.setArticles(articles);
            response.setReaponseMessage("查询用户文章成功!");
            response.setResult(true);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(false, "查询用户文章失败!");
        }
    }

    //添加文章信息
    @Transactional(rollbackFor = Exception.class)
    public int addArticle(Map<String, String> map) {
        Article article = new Article();
        int userId = Integer.parseInt(map.get("userId"));
        int isTop = Integer.parseInt(map.get("isTop"));
        int readPeas = 0;
        //如果用户开启置顶,则先判断用户阅豆是否足够置顶消耗
        if (isTop == 1) {
            try {
                //不指定readPeas就默认给5
                readPeas = Integer.parseInt(map.get("readPeas"));
            }catch (Exception e){
                readPeas=5;
            }
            int i = userDao.selectReadPeasByUserId(userId);
            if (i < readPeas) {
                return 0;//阅豆不足,直接返回
            }
        }
        //插入文章表前初始化参数
        article.setTitle(map.get("title"));
        article.setArticleLink(map.get("articleLink"));
        article.setIsTop(isTop);
        article.setArticleStatus(1);
        //插入文章表
        articleDao.insertArticle(article);
        //添加用户和文章映射
        UserArticle userArticle = new UserArticle(userId, article.getArticleId());
        userArticleDao.insertSelective(userArticle);
        //更新阅豆
        if (isTop == 1) {
            User user = new User();
            user.setUserId(userId);
            user.setReadPeas(readPeas);
            userDao.subReadPeas(user);
        }
        return 1;
    }

    //查询阅我用户信息
    public Response getReadMeInfo(int userId) {
        try {
            ArrayList<ReadMeData> readMeInfo = readMeDao.getReadMeInfo(userId);
            ReadMeResponse readMeResponse = new ReadMeResponse();
            readMeResponse.setResult(true);
            readMeResponse.setReaponseMessage("查询阅我用户列表成功!");
            readMeResponse.setReadMeData(readMeInfo);
            return readMeResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(false, "查询阅我用户列表失败!");
        }
    }

    //删除文章
    public boolean userMatchArticle(Integer userId, Integer articleId) {
        UserArticle userArticle = new UserArticle();
        userArticle.setArticleId(articleId);
        UserArticle selectOne = userArticleDao.selectOne(userArticle);
        return selectOne.getUserId().equals(userId) ? true : false;
    }

    //删除文章
    @Transactional(rollbackFor = Exception.class)
    public int delArticle(int articleId) {
        Article article = new Article();
        article.setArticleId(articleId);
        Article selectOne = articleDao.selectOne(article);
        if (selectOne == null) {
            return 0;//表示文章不存在
        }
        if (selectOne.getDeleteFlg().equals(1)) {
            return 2;//返回2表示,文章已经删除
        }
        article.setDeleteFlg(1);
        return articleDao.updateByPrimaryKeySelective(article);
    }

    //上架文章
    @Transactional(rollbackFor = Exception.class)
    public int reAddArticle(int articleId) {
        Article article = new Article();
        article.setArticleId(articleId);
        Article selectOne = articleDao.selectOne(article);
        if (selectOne.getArticleStatus() == null) {
            return 0;//表示文章不存在
        }
        if (selectOne.getArticleStatus().equals(1)) {
            return 2;//返回2表示,文章已经上架
        }
        article.setArticleStatus(1);
        return articleDao.updateByPrimaryKeySelective(article);
    }

    //下架文章
    @Transactional(rollbackFor = Exception.class)
    public int removeArticle(int articleId) {
        Article article = new Article();
        article.setArticleId(articleId);
        Article selectOne = articleDao.selectOne(article);
        if (selectOne.getArticleStatus() == null) {
            return 0;//表示文章不存在
        }
        if (selectOne.getArticleStatus().equals(0)) {
            return 2;//返回2表示,文章已经下架
        }
        article.setArticleStatus(0);
        return articleDao.updateByPrimaryKeySelective(article);
    }

    /*****************用户操作*****************/
    //获取用户
    public User getUser(String openid) {
        User user = new User();
        user.setOpenid(openid);
        return userDao.selectOne(user);
    }

    //添加用户
    @Transactional(rollbackFor = Exception.class)
    public int addUser(String openid) {
        User user = new User();
        user.setOpenid(openid);
        return userDao.insertSelective(user);
    }

    //完善用户信息
    @Transactional(rollbackFor = Exception.class)
    public int competeUser(User user) {
        user.setOpenid(null);
        user.setReadPeas(null);
        user.setRegisterTime(null);
        return userDao.updateByPrimaryKeySelective(user);
    }

    public int addReadpeaForShareUser(Integer shareId){
        User user = new User();
        user.setUserId(shareId);
        return userDao.addReadPeas(user);
    }
}

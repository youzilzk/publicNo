package com.project.publicNo.service;


import com.project.publicNo.dao.*;
import com.project.publicNo.entity.*;
import com.project.publicNo.pojo.*;
import com.project.publicNo.pojo.impl.ArticleResponse;
import com.project.publicNo.pojo.impl.InitResponse;
import com.project.publicNo.pojo.impl.ReadMeResponse;
import com.project.publicNo.pojo.impl.UserInfoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
    @Autowired
    private ReadArticleDao ReadArticleDao;
    @Autowired
    private FeedbackDao feedbackDao;

    //获取登录的用户信息
    public Response loginService(int userId, String isSelf) {
        try {
            User user = new User();
            user.setUserId(userId);
            User selectOne = userDao.selectOne(user);
            //根据用户id查询阅豆
            int readPeas = initDao.selectReadPeas(userId);
            //查询该用户发布的文章数
            int articleCount = initDao.selectArticleCount(userId, Integer.parseInt(isSelf));
            //查询待阅数
            int waitReadCount = initDao.selectWaitReadCount(userId);
            //查询用户曝光度,用户所有文章曝光度之和
            Integer exposure=initDao.selectExposure(userId);
            exposure=exposure==null?0:exposure;
            UserInfoResponse userInfoResponse = new UserInfoResponse(
                    userId, selectOne.getNickname(), selectOne.getOpenid(),
                    selectOne.getPicUrl(), selectOne.getReadPeas(), selectOne.getPhone(),
                    selectOne.getRegisterTime(), articleCount, waitReadCount,exposure);
            userInfoResponse.setResult(true);
            userInfoResponse.setReaponseMessage("获取用户信息成功!");
            return userInfoResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(false, "获取用户信息失败!");
        }
    }

    //初始化首页
    public Response initService(String userId) {
        try {
            InitResponse initResponse = new InitResponse();
            //查询阅读排行
            ArrayList<RankData> rankList = initDao.selectRankData();
            HashSet<Integer> set = new HashSet<>();
            if(!userId.equals("")){
                ArrayList<Integer> articleIds=initDao.selectArticleIdsAndReadedToday(Integer.parseInt(userId));
                for (int i = 0; i < articleIds.size(); i++) {
                    set.add(articleIds.get(i));
                }
            }
            for (int i = 0; i < rankList.size(); i++) {
                Integer articleId = rankList.get(i).getArticleId();
                if(set.contains(articleId)){
                    //设置为已阅
                    rankList.get(i).setIsReaded(1);
                }else {
                    //设置为未阅
                    rankList.get(i).setIsReaded(0);
                }
            }
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
    /* isSelf区别是否为当前登录用户,有的信息不返回给非当前登录用户 */
    public Response getArticles(int userId, String isSelf, String readerId) {
        try {
            ArrayList<Article> articles = articleDao.selectArticlesByUserId(userId, Integer.parseInt(isSelf));
            ArticleResponse response = new ArticleResponse();
            if (isSelf.equals("0") && !readerId.equals("")) {
                int authorId = userId;
                ArrayList<ReadArticle> readArticles = readMeDao.selectReadOtherInfoByMe(authorId, Integer.parseInt(readerId));
                HashMap<Integer, Article> hashMap = new HashMap<>();
                for (int i = 0; i < articles.size(); i++) {
                    hashMap.put(articles.get(i).getArticleId(), articles.get(i));
                }
                for (int i = 0; i < readArticles.size(); i++) {
                    ReadArticle readArticle = readArticles.get(i);
                    hashMap.get(readArticle.getArticleId()).setReadStatus(1);
                    hashMap.get(readArticle.getArticleId()).setReadTime(readArticle.getReadTime());
                }
                articles = new ArrayList<Article>();
                for (Map.Entry<Integer, Article> entry : hashMap.entrySet()) {
                    articles.add(entry.getValue());
                }
            }
            response.setArticles(articles);
            response.setReaponseMessage("查询用户文章成功!");
            response.setResult(true);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(false, "查询用户文章失败!");
        }
    }

    //获取用户头像地址
    public String getPicUrl(Integer userId){
        User user = new User();
        user.setUserId(userId);
        User selectOne = userDao.selectOne(user);
        return selectOne.getPicUrl();
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
            } catch (Exception e) {
                readPeas = 5;
            }

        }else {
            readPeas=1;
        }
        int i = userDao.selectReadPeasByUserId(userId);
        if (i < readPeas) {
            return 0;//阅豆不足,直接返回
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
        User user = new User();
        user.setUserId(userId);
        user.setReadPeas(readPeas);
        userDao.subReadPeas(user);
        //返回文章id
        return  article.getArticleId();
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

        Article selectOne = articleDao.selectArticle(articleId);
        if (selectOne == null) {
            return 0;//表示文章不存在
        }
        if (selectOne.getDeleteFlg().equals(1)) {
            return 2;//返回2表示,文章已经删除
        }
        article.setDeleteFlg(1);
        //使用mybatis需要将阅读状态置空
        article.setReadStatus(null);
        return articleDao.updateByPrimaryKeySelective(article);
    }

    //上架文章
    @Transactional(rollbackFor = Exception.class)
    public int reAddArticle(int articleId) {
        Article article = new Article();
        article.setArticleId(articleId);
        Article selectOne = articleDao.selectArticle(articleId);
        if (selectOne.getArticleStatus() == null) {
            return 0;//表示文章不存在
        }
        if (selectOne.getArticleStatus().equals(1)) {
            return 2;//返回2表示,文章已经上架
        }
        article.setArticleStatus(1);
        article.setReadStatus(null);
        return articleDao.updateByPrimaryKeySelective(article);
    }

    //下架文章
    @Transactional(rollbackFor = Exception.class)
    public int removeArticle(int articleId) {
        Article article = new Article();
        article.setArticleId(articleId);
        Article selectOne = articleDao.selectArticle(articleId);
        if (selectOne.getArticleStatus() == null) {
            return 0;//表示文章不存在
        }
        if (selectOne.getArticleStatus().equals(0)) {
            return 2;//返回2表示,文章已经下架
        }
        article.setArticleStatus(0);
        article.setReadStatus(null);
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

    public int addReadpeaForUser(Integer shareId, Integer readPeas) {
        User user = new User();
        user.setUserId(shareId);
        user.setReadPeas(readPeas);
        return userDao.addReadPeas(user);
    }

    //阅读成功,给与奖励,更新数据等
    @Transactional(rollbackFor = Exception.class)
    public int readSuccess(String userId, String articleId) {
        ReadArticle readArticle = new ReadArticle();
        readArticle.setArticleId(Integer.parseInt(articleId));
        readArticle.setReaderId(Integer.parseInt(userId));
        /*插入阅读文章表*/
        //先查询
        if (ReadArticleDao.selectCount(readArticle) == 1) {
            //更新
            ReadArticleDao.updateReadTime(readArticle);
        } else {
            //插入
            ReadArticleDao.insertSelective(readArticle);
        }
        //查询文章作者
        UserArticle userArticle = new UserArticle();
        userArticle.setArticleId(Integer.parseInt(articleId));
        Integer authorId = userArticleDao.selectOne(userArticle).getUserId();
        /*更新readMe表*/
        //先查询有无记录
        ReadMe readMe = new ReadMe();
        readMe.setAuthorId(authorId);
        readMe.setReaderId(Integer.parseInt(userId));
        if (readMeDao.selectCount(readMe) == 1) {
            //更新
            readMeDao.updateReadTime(readMe);
        } else {
            //插入
            readMeDao.insertSelective(readMe);
        }
        //阅读自己文章不奖励阅豆也不更新曝光度,否则增加
       if(!userId.toString().equals(authorId)){
           //更新文章曝光数
           articleDao.updateExposure(Integer.parseInt(articleId));
           //更新用户阅豆
           User user = new User();
           user.setUserId(Integer.parseInt(userId));
           user.setReadPeas(1);
           userDao.addReadPeas(user);
       }
        return 1;
    }

    //反馈建议
    @Transactional(rollbackFor = Exception.class)
    public int feedback(Integer userId,String phone, Integer feedbackType,String feedbackDdesc){
        Feedback feedback = new Feedback(userId, phone, feedbackType, feedbackDdesc);
        int i = feedbackDao.insertSelective(feedback);
        return i;
    }
}

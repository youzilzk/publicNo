/*
SQLyog v10.2 
MySQL - 5.7.28 : Database - project
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`project` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `project`;

/*Table structure for table `t_article` */

DROP TABLE IF EXISTS `t_article`;

CREATE TABLE `t_article` (
  `article_id` int(10) NOT NULL AUTO_INCREMENT,
  `title` varchar(32) NOT NULL,
  `article_link` varchar(32) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `delete_flg` tinyint(1) NOT NULL DEFAULT '0',
  `isTop` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`article_id`)
) ENGINE=InnoDB AUTO_INCREMENT=345345373 DEFAULT CHARSET=utf8;

/*Data for the table `t_article` */

insert  into `t_article`(`article_id`,`title`,`article_link`,`create_time`,`delete_flg`,`isTop`) values (12345,'他还是个','gzsfd','2020-05-28 20:22:40',1,1),(86756,'让人tea个人','shsgf','2020-05-28 20:21:54',1,1),(1321231,'特哈热热热台湾','shdgsdfs','2020-05-28 20:20:44',0,1),(5467564,'jet和认识','rhtsgsf','2020-05-28 22:21:25',0,1),(123123123,'如何变成帅哥','www.baidu.com','2020-05-30 21:12:11',0,1),(234234234,'如何让富婆爱上我','www.baidu.com','2020-05-27 21:12:11',0,1),(345345345,'如何快速致富','www.baidu.com','2020-05-27 21:12:11',0,1),(345345346,'人家无色让他如果','ueyrter','2020-06-03 14:50:54',0,1),(345345347,'可他依然他','yjdhgdsfh','2020-06-03 14:52:03',1,1),(345345348,'添加用户的感受','ykjtyhgdf','2020-06-03 15:42:59',0,1),(345345366,'酷酷酷酷酷','kdyjdhgsf','2020-06-03 19:38:37',0,1),(345345367,'酷酷酷酷酷','kdyjdhgsf','2020-06-03 19:39:17',0,1),(345345368,'酷酷酷酷酷','kdyjdhgsf','2020-06-03 19:40:25',0,1),(345345369,'酷酷酷酷酷','kdyjdhgsf','2020-06-03 19:41:53',0,0),(345345370,'酷酷酷酷酷','kdyjdhgsf','2020-06-03 19:42:41',0,0),(345345371,'给客户了解','jdfhsdgsdfsd','2020-06-04 10:49:54',0,1);

/*Table structure for table `t_readme` */

DROP TABLE IF EXISTS `t_readme`;

CREATE TABLE `t_readme` (
  `reader_id` int(10) NOT NULL,
  `read_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `author_id` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `t_readme` */

insert  into `t_readme`(`reader_id`,`read_time`,`author_id`) values (2147483647,'2020-05-27 13:34:24',1396511473),(1312466325,'2020-05-30 13:34:17',1396511473);

/*Table structure for table `t_user` */

DROP TABLE IF EXISTS `t_user`;

CREATE TABLE `t_user` (
  `user_id` int(10) NOT NULL AUTO_INCREMENT,
  `nickname` varchar(32) DEFAULT NULL,
  `password` varchar(32) DEFAULT NULL,
  `pic_url` varchar(32) DEFAULT NULL,
  `read_peas` int(10) unsigned NOT NULL,
  `phone` varchar(32) NOT NULL,
  `register_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY `user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2147483647 DEFAULT CHARSET=utf8;

/*Data for the table `t_user` */

insert  into `t_user`(`user_id`,`nickname`,`openid`,`pic_url`,`read_peas`,`phone`,`register_time`) values (1312466325,'荷花',NULL,'miao',3,'','2020-05-28 17:37:02'),(1396511473,'寻',NULL,'bj3',5,'','2020-05-28 17:37:02'),(1555693665,'简单1',NULL,'bj4',7,'','2020-05-28 17:37:02'),(1773244177,'花花1',NULL,'timg',4,'','2020-05-28 17:39:49'),(2147483647,'好好先生',NULL,'bj4',8,'','2020-05-29 21:25:16');

/*Table structure for table `t_user_article` */

DROP TABLE IF EXISTS `t_user_article`;

CREATE TABLE `t_user_article` (
  `user_id` int(10) NOT NULL,
  `article_id` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `t_user_article` */

insert  into `t_user_article`(`user_id`,`article_id`) values (1396511473,123123123),(1312466325,1321231),(2147483647,234234234),(2147483647,5467564),(1396511473,345345345),(1396511473,86756),(1396511473,12345),(1396511473,345345366),(1396511473,345345367),(1396511473,345345368),(1396511473,345345369),(1396511473,345345370),(1396511473,345345371);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

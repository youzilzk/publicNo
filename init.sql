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
  `article_link` varchar(128) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `delete_flg` tinyint(1) NOT NULL DEFAULT '0',
  `isTop` tinyint(1) NOT NULL DEFAULT '0',
  `article_status` tinyint(1) NOT NULL DEFAULT '1',
  `exposure` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`article_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000000 DEFAULT CHARSET=utf8;

/*Data for the table `t_article` */

insert  into `t_article`(`article_id`,`title`,`article_link`,`create_time`,`delete_flg`,`isTop`,`article_status`,`exposure`) values (999999,'init','init','2020-07-27 14:08:19',0,0,1,0);

/*Table structure for table `t_feedback` */

DROP TABLE IF EXISTS `t_feedback`;

CREATE TABLE `t_feedback` (
  `user_id` int(10) NOT NULL,
  `phone` varchar(32) DEFAULT NULL,
  `fd_type` int(1) NOT NULL,
  `fd_desc` varchar(320) DEFAULT NULL,
  `fd_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `t_feedback` */

/*Table structure for table `t_read_article` */

DROP TABLE IF EXISTS `t_read_article`;

CREATE TABLE `t_read_article` (
  `reader_id` int(10) NOT NULL,
  `article_id` int(10) NOT NULL,
  `read_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `read_count` int(10) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `t_read_article` */

/*Table structure for table `t_readme` */

DROP TABLE IF EXISTS `t_readme`;

CREATE TABLE `t_readme` (
  `reader_id` int(10) NOT NULL,
  `read_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `author_id` int(10) NOT NULL,
  `read_count` int(10) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `t_readme` */

/*Table structure for table `t_user` */

DROP TABLE IF EXISTS `t_user`;

CREATE TABLE `t_user` (
  `user_id` int(10) NOT NULL AUTO_INCREMENT,
  `nickname` varchar(32) DEFAULT NULL,
  `openid` varchar(128) NOT NULL,
  `pic_url` varchar(256) DEFAULT NULL,
  `read_peas` int(10) unsigned NOT NULL DEFAULT '5',
  `phone` varchar(32) DEFAULT NULL,
  `register_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY `user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000000 DEFAULT CHARSET=utf8;

/*Data for the table `t_user` */

insert  into `t_user`(`user_id`,`nickname`,`openid`,`pic_url`,`read_peas`,`phone`,`register_time`) values (999999,'init','init','init',0,NULL,'2020-07-27 14:08:00');

/*Table structure for table `t_user_article` */

DROP TABLE IF EXISTS `t_user_article`;

CREATE TABLE `t_user_article` (
  `user_id` int(10) NOT NULL,
  `article_id` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `t_user_article` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

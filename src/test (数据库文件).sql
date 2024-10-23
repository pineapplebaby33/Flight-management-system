-- phpMyAdmin SQL Dump
-- version 3.4.10.1
-- http://www.phpmyadmin.net
--
-- 主机: localhost
-- 生成日期: 2017 年 05 月 21 日 10:25
-- 服务器版本: 5.5.20
-- PHP 版本: 5.3.10

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- 数据库: `test`
--

-- --------------------------------------------------------

--
-- 表的结构 `admin`
--

CREATE TABLE IF NOT EXISTS `admin` (
  `Id` int(100) NOT NULL AUTO_INCREMENT,
  `Username` varchar(1000) NOT NULL,
  `Password` varchar(1000) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=12 ;

--
-- 转存表中的数据 `admin`
--

INSERT IGNORE INTO `admin` (`Id`, `Username`, `Password`) VALUES
(1, 'Admin', '21232f297a57a5a743894a0e4a801fc3'),
(2, 'Admin999', '00ba7ceab606427071d5d755ea99e976'),
(8, '任涛', '06fe4992a4bb5945c3796a984e0e4755'),
(9, 'admin000', '1eea36fbd4f4919251e3192dce2da380'),
(10, 'admin111', 'bbad8d72c1fac1d081727158807a8798'),
(11, 'aaa000', '6829b4940c8c717af0b4e746055a492f');

-- --------------------------------------------------------

--
-- 表的结构 `flight`
--

CREATE TABLE IF NOT EXISTS `flight` (
  `Id` int(100) NOT NULL AUTO_INCREMENT COMMENT '航班ID',
  `StartTime` varchar(100) NOT NULL COMMENT '起飞时间',
  `ArrivalTime` varchar(100) NOT NULL COMMENT '到达时间',
  `StartCity` varchar(100) NOT NULL COMMENT '起飞城市',
  `ArrivalCity` varchar(100) NOT NULL COMMENT '到达城市',
  `DepartureDate` varchar(100) NOT NULL COMMENT '起飞日期',
  `Price` float NOT NULL COMMENT '机票价格',
  `CurrentPassengers` int(100) NOT NULL COMMENT '机票当前预订人数',
  `SeatCapacity` int(100) NOT NULL COMMENT '机票容量',
  `FlightStatus` varchar(50) NOT NULL DEFAULT 'UNPUBLISHED' COMMENT '航班状态',
  `PassengerId` varchar(1000) NOT NULL COMMENT '乘客Id',
  `FlightName` varchar(100) NOT NULL COMMENT '航班名字（代替pdf中的航班ID）',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=24 ;

--
-- 转存表中的数据 `flight`
--

INSERT IGNORE INTO `flight` (`Id`, `StartTime`, `ArrivalTime`, `StartCity`, `ArrivalCity`, `DepartureDate`, `Price`, `CurrentPassengers`, `SeatCapacity`, `FlightStatus`, `PassengerId`, `FlightName`) VALUES
(1, '2017-05-17-19-00-00', '2017-05-17-23-00-29', '上海', '北京', '2017-05-02', 900, 3, 200, 'TERMINATE', '1;2;3;', 'CZ89271'),
(2, '2017-05-02-19-20-00', '2017-05-02-22-20-29', '天津', '北京', '2017-05-02', 899.4, 0, 200, 'TERMINATE', '', 'CZ83211'),
(3, '2017-05-29-19-00-00', '2017-05-29-22-00-00', '北京', '上海', '2017-05-29', 900.1, 0, 400, 'AVAILABLE', '', 'XZ98521'),
(4, '2017-05-18-19-44-00', '2017-05-19-02-00-00', '北京', '上海', '2017-05-29', 900, 0, 200, 'TERMINATE', '', 'XZ98345'),
(5, '2017-05-18-19-44-00', '2017-05-18-22-44-00', '重庆', '上海', '2017-05-18-22-44-00', 2000, 0, 200, 'TERMINATE', '', 'XZ98346'),
(6, '2017-05-23-00-00-00', '2017-05-23-04-00-00', '沈阳', '呼和浩特', '2017-05-23-04-00-00', 900, 0, 100, 'AVAILABLE', '', 'XE99021'),
(7, '2017-05-23-00-00-00', '2017-05-23-04-00-00', '太原', '郑州', '2017-05-23-04-00-00', 1000, 0, 300, 'UNPUBLISHED', '', 'RE100000'),
(8, '2017-05-22-12-00-00', '2017-05-22-16-00-00', '昆明', '拉萨', '2017-05-22-16-00-00', 1000, 0, 5, 'AVAILABLE', '', 'RE200000'),
(9, '2017-05-21-14-00-00', '2017-05-21-19-00-00', '沈阳', '天津', '2017-05-21-19-00-00', 2000, 0, 200, 'TERMINATE', '', 'ZX23413'),
(10, '2017-05-21-14-00-00', '2017-05-21-16-10-00', '上海', '天津', '2017-05-21-16-10-00', 1000, 0, 200, 'TERMINATE', '', 'CE90000'),
(11, '2017-05-21-15-00-00', '2017-05-21-18-00-00', '沈阳', '成都', '2017-05-21-18-00-00', 1100, 0, 30, 'TERMINATE', '', 'VE90000'),
(12, '2017-05-21-12-00-00', '2017-05-21-15-00-00', '武汉', '北京', '2017-05-21-15-00-00', 2000, 0, 100, 'TERMINATE', '', 'VB900002'),
(13, '2017-05-21-19-00-00', '2017-05-21-22-00-00', '重庆', '哈尔滨', '2017-05-21-22-00-00', 2000, 0, 200, 'TERMINATE', '', 'DD109002'),
(14, '2017-05-21-19-00-00', '2017-05-21-22-00-00', '杭州', '成都', '2017-05-21-22-00-00', 1050, 0, 200, 'TERMINATE', '', 'RE300000'),
(15, '2017-05-22-19-00-00', '2017-05-22-22-00-00', '重庆', '上海', '2017-05-22-22-00-00', 1322, 0, 200, 'AVAILABLE', '', 'RE400000'),
(16, '2017-05-22-19-00-00', '2017-05-22-22-00-00', '太原', '上海', '2017-05-22-22-00-00', 1302, 0, 222, 'AVAILABLE', '', 'RE50000'),
(17, '2017-05-22-19-00-00', '2017-05-22-23-00-00', '长春', '北京', '2017-05-22-23-00-00', 1111, 0, 111, 'AVAILABLE', '', 'RE111111'),
(18, '2017-05-22-15-00-00', '2017-05-22-19-00-00', '天津', '沈阳', '2017-05-22-19-00-00', 999, 0, 111, 'AVAILABLE', '', 'QE11111'),
(19, '2017-05-22-14-00-00', '2017-05-22-19-00-00', '哈尔滨', '长春', '2017-05-22-19-00-00', 1231, 0, 111, 'AVAILABLE', '', 'RE321311'),
(20, '2017-05-23-12-00-00', '2017-05-23-19-00-00', '天津', '北京', '2017-05-23-19-00-00', 1001, 0, 122, 'AVAILABLE', '', 'TR112111'),
(21, '2017-05-23-12-00-00', '2017-05-23-19-00-00', '沈阳', '北京', '2017-05-23-19-00-00', 2311, 0, 121, 'AVAILABLE', '', 'TR123123'),
(22, '2017-05-23-12-00-00', '2017-05-23-19-00-00', '哈尔滨', '南京', '2017-05-23-19-00-00', 1211, 0, 100, 'AVAILABLE', '', 'WE212311'),
(23, '2017-05-23-12-00-00', '2017-05-23-15-00-00', '重庆', '成都', '2017-05-23-15-00-00', 1231, 0, 100, 'AVAILABLE', '', 'TO112131');

-- --------------------------------------------------------

--
-- 表的结构 `order`
--

CREATE TABLE IF NOT EXISTS `order` (
  `Id` int(100) NOT NULL AUTO_INCREMENT COMMENT '订单id',
  `PassengerId` int(100) NOT NULL COMMENT '乘客Id',
  `Seat` int(100) NOT NULL COMMENT '座位号',
  `FlightId` int(100) NOT NULL COMMENT '航班Id',
  `CreateDate` varchar(100) NOT NULL COMMENT '预订日期',
  `Status` varchar(100) NOT NULL COMMENT '订单状态',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=14 ;

--
-- 转存表中的数据 `order`
--

INSERT IGNORE INTO `order` (`Id`, `PassengerId`, `Seat`, `FlightId`, `CreateDate`, `Status`) VALUES
(1, 1, 1, 1, '2016-03-04-05-20-01', 'PAID'),
(2, 2, 2, 1, '2017-04-20-00-23-11', 'PAID'),
(6, 3, 3, 1, '2017-05-02-00-00-56', 'PAID'),
(7, 11, 18, 20, '2017-05-21-17-59-11', 'PAID'),
(8, 11, 40, 21, '2017-05-21-17-59-25', 'PAID'),
(9, 11, 46, 17, '2017-05-21-17-59-33', 'CANCEL'),
(10, 11, 73, 15, '2017-05-21-18-02-42', 'PAID'),
(11, 11, 98, 19, '2017-05-21-18-22-20', 'PAID'),
(12, 11, 40, 3, '2017-05-21-18-22-35', 'PAID'),
(13, 11, 8, 3, '2017-05-21-18-22-49', 'CANCEL');

-- --------------------------------------------------------

--
-- 表的结构 `passenger`
--

CREATE TABLE IF NOT EXISTS `passenger` (
  `Id` int(100) NOT NULL AUTO_INCREMENT,
  `RealName` varchar(50) NOT NULL,
  `IdentityId` varchar(30) NOT NULL,
  `Password` varchar(100) NOT NULL,
  `OrderList` varchar(1000) NOT NULL COMMENT '航班Id',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=12 ;

--
-- 转存表中的数据 `passenger`
--

INSERT IGNORE INTO `passenger` (`Id`, `RealName`, `IdentityId`, `Password`, `OrderList`) VALUES
(1, '任涛', '410504199703042819', '4fd952b7a28daf93be5457b4318554a1', '1;'),
(2, '程思斌', '411603199804051234', '4fd952b7a28daf93be5457b4318554a1', '1;'),
(3, '孙玺', '411603199724054321', '4fd952b7a28daf93be5457b4318554a1', '1;'),
(4, 'AndrewNg', '411603199724059987', '4fd952b7a28daf93be5457b4318554a1', ''),
(5, 'A13', '410502198403027431', '9982b2a7fceaaee2c8444b5086aee008', ''),
(6, 'A14', '410503586749358242', '9982b2a7fceaaee2c8444b5086aee008', ''),
(7, '王五', '411603199804051236', '0ebce60f091d1dfc0f309101728b054f', ''),
(8, '李四', '411603199804051234', '9982b2a7fceaaee2c8444b5086aee008', ''),
(9, 'lion', '411603199804051223', '52c69e3a57331081823331c4e69d3f2e', ''),
(10, '七六', '411603199804051555', 'fbd7939d674997cdb4692d34de8633c4', ''),
(11, '源氏', '4444444444444444444', '7e9a4efc9e71f476fe7a4598c8db4762', '');

-- --------------------------------------------------------

--
-- 表的结构 `weichuncai_table`
--

CREATE TABLE IF NOT EXISTS `weichuncai_table` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(150) NOT NULL DEFAULT '',
  `value` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- 转存表中的数据 `weichuncai_table`
--

INSERT IGNORE INTO `weichuncai_table` (`id`, `title`, `value`) VALUES
(1, '', 'a:10:{s:6:"notice";s:123:"ä½ å¥½ï¼Œæˆ‘æ˜¯çœ‹æ¿å¨˜rakutoriï¼Œæ¬¢è¿Žè®¿é—®ï¼Œè¯·å¤šæŒ‡æ•™ã€‚\r\næœ‰ä»€ä¹ˆä¸ä¼šçš„å¯ä»¥é—®æˆ‘ï¼Œåæ­£æˆ‘ä¹Ÿä¸ç´«é“\r\n";s:9:"adminname";s:6:"ä¸»äºº";s:8:"isnotice";N;s:4:"ques";a:5:{i:0;s:9:"æ—©ä¸Šå¥½";i:1;s:9:"ä¸­åˆå¥½";i:2;s:9:"ä¸‹åˆå¥½";i:3;s:9:"æ™šä¸Šå¥½";i:4;s:6:"æ™šå®‰";}s:3:"ans";a:5:{i:0;s:12:"æ—©ä¸Šå¥½ï½ž";i:1;s:12:"ä¸­åˆå¥½ï½ž";i:2;s:12:"ä¸‹åˆå¥½ï½ž";i:3;s:12:"æ™šä¸Šå¥½ï½ž";i:4;s:9:"æ™šå®‰ï½ž";}s:8:"lifetime";a:3:{s:8:"rakutori";i:1467543602;s:4:"neko";i:1467543602;s:11:"chinese_moe";i:1467543602;}s:3:"ccs";a:3:{i:0;s:8:"rakutori";i:1;s:4:"neko";i:2;s:11:"chinese_moe";}s:10:"defaultccs";s:8:"rakutori";s:5:"foods";a:2:{i:0;s:9:"é‡‘å·åžƒ";i:1;s:9:"å’¸æ¢…å¹²";}s:6:"eatsay";a:2:{i:0;s:45:"åƒäº†é‡‘å·åžƒï¼Œä¸€åˆ€èƒ½ç§’ä¸€ä¸‡å…«ï½žï¼";i:1;s:42:"åƒå’¸æ¢…å¹²ï¼Œå˜è¶…äººï¼å“¦è€¶ï½žï½žï½ž";}}');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

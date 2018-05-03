/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50556
Source Host           : localhost:3306
Source Database       : cqc

Target Server Type    : MYSQL
Target Server Version : 50556
File Encoding         : 65001

Date: 2017-11-17 00:47:53
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `account`
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(32) DEFAULT NULL COMMENT '用户名',
  `nickname` varchar(32) DEFAULT NULL COMMENT '昵称',
  `password` varchar(50) DEFAULT NULL COMMENT '密码',
  `mobile` varchar(16) DEFAULT NULL COMMENT '手机',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `lock` enum('N','Y') DEFAULT 'N' COMMENT '状态',
  `role_id` bigint(50) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `remark` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNQ_name` (`username`) USING BTREE
) ENGINE=MyISAM AUTO_INCREMENT=45 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of account
-- ----------------------------
INSERT INTO `account` VALUES ('1', 'admin', 'admin', 'E10ADC3949BA59ABBE56E057F20F883E', '13434343422', '2016-12-18 23:06:04', 'N', '1', '312265264@qq.com', '');
INSERT INTO `account` VALUES ('7', 'bill', 'ad', 'E10ADC3949BA59ABBE56E057F20F883E', '13512342323', '2017-06-21 23:30:12', 'N', '15', '2422292577@qq.com', '');
INSERT INTO `account` VALUES ('8', 'lily', 'lily', 'E10ADC3949BA59ABBE56E057F20F883E', '13723423434', '2017-06-21 23:30:34', 'N', '17', '2422192577@qq.com', '');
INSERT INTO `account` VALUES ('13', 'William', 'William', 'E10ADC3949BA59ABBE56E057F20F883E', '15918703417', '2017-06-21 23:33:15', 'N', '17', '2522292577@qq.com', '');


-- ----------------------------
-- Table structure for `menu`
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `url` varchar(100) DEFAULT NULL,
  `p_id` bigint(20) DEFAULT NULL,
  `sort_num` int(11) DEFAULT NULL,
  `is_parent` enum('N','Y') DEFAULT 'N',
  `alias` varchar(50) DEFAULT NULL COMMENT '别名，必须唯一',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES ('1', '系统管理', 'system/index', null, '10', 'Y', 'system');
INSERT INTO `menu` VALUES ('2', '用户管理', 'account/list', '1', '1', 'N', 'account');
INSERT INTO `menu` VALUES ('3', '角色管理', 'role/list', '1', '2', 'N', 'role');
INSERT INTO `menu` VALUES ('10', '字典管理', 'dictionary/list', '1', '6', 'N', 'dictionary');
INSERT INTO `menu` VALUES ('11', '日志管理', 'operationlog/list', '1', '4', 'N', 'log');
INSERT INTO `menu` VALUES ('12', '区域管理', 'area/list', '1', '2', 'N', 'area');
INSERT INTO `menu` VALUES ('13', '机构管理', 'org/list', '1', '3', 'N', 'org');
INSERT INTO `menu` VALUES ('14', '菜单管理', 'menu/list', '1', '5', 'N', 'menu');
INSERT INTO `menu` VALUES ('15', '任务管理', '', null, '1', 'Y', 'info');
INSERT INTO `menu` VALUES ('16', '车型OTS阶段任务', 'ots/index?taskType=1', '15', '1', 'N', 'otsTask');
INSERT INTO `menu` VALUES ('17', '任务申请', 'ots/requireList?taskType=1', '16', '1', 'N', 'otsRequire');
INSERT INTO `menu` VALUES ('18', '信息审核', 'ots/examineList?taskType=1', '16', '2', 'N', 'otsExamine');
INSERT INTO `menu` VALUES ('19', '任务下达', 'ots/transmitList?taskType=1', '16', '3', 'N', 'otsOrder');
INSERT INTO `menu` VALUES ('20', '任务审批', 'ots/approveList?taskType=1', '16', '4', 'N', 'otsApprove');
INSERT INTO `menu` VALUES ('21', '车型PPAP阶段任务', 'ppap/index?taskType=2', '15', '2', 'N', 'ppapTask');
INSERT INTO `menu` VALUES ('22', '任务下达 ', 'ppap/transmitList?taskType=2', '21', '1', 'N', 'ppapOrder');
INSERT INTO `menu` VALUES ('23', '任务审批', 'ppap/approveList?taskType=2', '21', '2', 'N', 'ppapApprove');
INSERT INTO `menu` VALUES ('24', '结果确认', 'ppap/confirmList?taskType=2', '21', '3', 'N', 'ppapConfirm');
INSERT INTO `menu` VALUES ('25', '车型SOP阶段任务', 'ppap/index?taskType=3', '15', '3', 'N', 'sopTask');
INSERT INTO `menu` VALUES ('26', '任务下达 ', 'ppap/transmitList?taskType=3', '25', '1', 'N', 'sopOrder');
INSERT INTO `menu` VALUES ('27', '任务审批 ', 'ppap/approveList?taskType=3', '25', '2', 'N', 'sopApprove');
INSERT INTO `menu` VALUES ('28', '结果确认', 'ppap/confirmList?taskType=3', '25', '3', 'N', 'sopConfirm');
INSERT INTO `menu` VALUES ('36', '非车型材料任务', 'ots/index?taskType=4', '15', '4', 'N', 'gsTask');
INSERT INTO `menu` VALUES ('37', '任务申请', 'ots/requireList?taskType=4', '36', '1', 'N', 'gsRequire');
INSERT INTO `menu` VALUES ('38', '信息审核 ', 'ots/examineList?taskType=4', '36', '2', 'N', 'gsExamine');
INSERT INTO `menu` VALUES ('39', '任务下达 ', 'ots/transmitList?taskType=4', '36', '3', 'N', 'gsOrder');
INSERT INTO `menu` VALUES ('40', '任务审批 ', 'ots/approveList?taskType=4', '36', '4', 'N', 'gsApprove');
INSERT INTO `menu` VALUES ('41', '实验管理', null, null, '2', 'Y', '');
INSERT INTO `menu` VALUES ('42', '结果上传', null, '41', null, 'N', 'result');
INSERT INTO `menu` VALUES ('43', '型式试验结果上传', 'result/uploadList?type=1', '42', '1', 'N', 'patternUpload');
INSERT INTO `menu` VALUES ('44', '图谱结果上传', 'result/uploadList?type=2', '42', '2', 'N', 'atlasUpload');
INSERT INTO `menu` VALUES ('45', '结果比对', 'result/compareList', '41', null, 'N', 'compare');
INSERT INTO `menu` VALUES ('46', '结果发送', 'result/sendList', '41', null, 'N', 'send');
INSERT INTO `menu` VALUES ('47', '结果确认', null, '41', null, 'N', 'confirm');
INSERT INTO `menu` VALUES ('48', '待上传结果', 'result/confirmList?type=1', '47', '1', 'N', 'waitConfirm');
INSERT INTO `menu` VALUES ('49', '已上传结果', 'result/confirmList?type=2', '47', null, 'N', 'finishConfirm');
INSERT INTO `menu` VALUES ('50', '申请管理', null, null, '3', 'Y', 'apply');
INSERT INTO `menu` VALUES ('51', '修改申请', 'apply/taskList', '50', null, 'N', 'updateApply');
INSERT INTO `menu` VALUES ('52', '终止申请', 'apply/applyList', '50', null, 'N', 'endApply');
INSERT INTO `menu` VALUES ('54', '查询管理', 'query/list', null, '4', 'Y', 'query');
INSERT INTO `menu` VALUES ('55', '统计管理', null, null, '5', 'Y', 'statistic');
INSERT INTO `menu` VALUES ('57', '结果统计', 'statistic/result', '55', null, 'N', 'resultStatistic');
INSERT INTO `menu` VALUES ('58', '费用管理', '', null, '6', 'Y', 'cost');
INSERT INTO `menu` VALUES ('59', '待发送列表', 'cost/list?type=1', '58', null, 'N', 'tosend');
INSERT INTO `menu` VALUES ('60', '收费通知单列表', 'cost/list?type=2', '58', null, 'N', 'sent');
INSERT INTO `menu` VALUES ('61', '消息管理', 'message/list', null, '5', 'Y', 'message');

-- ----------------------------
-- Table structure for `operation_log`
-- ----------------------------
DROP TABLE IF EXISTS `operation_log`;
CREATE TABLE `operation_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(50) DEFAULT NULL,
  `user_agent` varchar(200) DEFAULT NULL,
  `client_ip` varchar(50) DEFAULT NULL,
  `time` timestamp NULL DEFAULT NULL,
  `detail` longtext,
  `type` varchar(100) DEFAULT NULL,
  `operation` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1546 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of operation_log
-- ----------------------------

-- ----------------------------
-- Table structure for `org`
-- ----------------------------
DROP TABLE IF EXISTS `org`;
CREATE TABLE `org` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `code` varchar(50) DEFAULT NULL,
  `areaid` bigint(20) DEFAULT NULL,
  `desc` varchar(200) DEFAULT NULL,
  `parentid` bigint(20) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `addr` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of org
-- ----------------------------
INSERT INTO `org` VALUES ('1', '机构', '001', null, null, null, null, null);
INSERT INTO `org` VALUES ('13', '通用五菱', 'sgmw', '2', '', '1', '1', null);
INSERT INTO `org` VALUES ('14', '上汽通用五菱SQE', 'SQE', '111', '', '13', '1', null);
INSERT INTO `org` VALUES ('15', '上汽通用五菱 PE', 'PE', '111', '', '13', '1', null);
INSERT INTO `org` VALUES ('16', '上汽通用五菱材料研究所', 'cl', '111', '', '13', '1', null);
INSERT INTO `org` VALUES ('17', '供应商', 'gy', '106', '', '1', '2', null);
INSERT INTO `org` VALUES ('18', '供应商1', 'g1', '111', '', '17', '2', '广州市天河区');
INSERT INTO `org` VALUES ('19', '实验室', 'sy', '111', '', '1', '3', null);
INSERT INTO `org` VALUES ('20', 'CQC华南实验室', 'cqc', '111', '', '19', '3', null);
INSERT INTO `org` VALUES ('21', '供应商2', 'g2', '111', '', '17', '2', '广州市海珠区');
INSERT INTO `org` VALUES ('22', '供应商3', 'g3', '111', '', '17', '2', '广州市白云区');
INSERT INTO `org` VALUES ('23', '其它实验室', 'qt', '2', '', '19', '3', '');



-- ----------------------------
-- Table structure for `role`
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `code` varchar(50) DEFAULT NULL,
  `grid` bigint(20) DEFAULT NULL,
  `desc` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', 'admin', 'admin', '22', '超级管理员');
INSERT INTO `role` VALUES ('14', 'PE_文员', 'pe_wy', '18', '上汽通用五菱PE-文员');
INSERT INTO `role` VALUES ('15', 'PE_工程师', 'pe_eng', '18', '上汽通用五菱PE-工程师');
INSERT INTO `role` VALUES ('16', 'PE_负责人', 'pe_leader', '18', '上汽通用五菱PE-负责人');
INSERT INTO `role` VALUES ('17', 'SQE_工程师', 'sqe_eng', '17', '');
INSERT INTO `role` VALUES ('18', 'SQE_负责人', 'sqe_leader', '17', '');
INSERT INTO `role` VALUES ('19', 'SQE_文员', 'sqe_wy', '17', '');
INSERT INTO `role` VALUES ('20', 'cqc实验室', 'cqc', '21', '');

-- ----------------------------
-- Table structure for `role_group`
-- ----------------------------
DROP TABLE IF EXISTS `role_group`;
CREATE TABLE `role_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `parentid` bigint(20) DEFAULT NULL,
  `desc` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role_group
-- ----------------------------
INSERT INTO `role_group` VALUES ('1', '角色', null, '根节点');
INSERT INTO `role_group` VALUES ('17', '上汽通用五菱SQE', '1', '上汽通用五菱SQE');
INSERT INTO `role_group` VALUES ('18', '上汽通用五菱PE', '1', '上汽通用五菱PE');
INSERT INTO `role_group` VALUES ('19', '上汽通用五菱TDC', '1', '上汽通用五菱TDC');
INSERT INTO `role_group` VALUES ('20', '上汽通用五菱供应商', '1', '上汽通用五菱供应商');
INSERT INTO `role_group` VALUES ('21', '实验室用户', '1', '实验室用户');
INSERT INTO `role_group` VALUES ('22', '系统维护人员', '1', '系统维护人员');

-- ----------------------------
-- Table structure for `role_permission`
-- ----------------------------
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NOT NULL,
  `permission` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role_permission
-- ----------------------------
INSERT INTO `role_permission` VALUES ('5', '1', 'account-1,role-2,log-2,home-2,area-2,org-2,menu-2,dictionary-2');
INSERT INTO `role_permission` VALUES ('11', '33', 'home-1,user-1,role-1');
INSERT INTO `role_permission` VALUES ('24', '14', '');
INSERT INTO `role_permission` VALUES ('26', '16', '17,18');
INSERT INTO `role_permission` VALUES ('27', '15', '17,18,19,20,42,43,45,46,48,49,51,52,2,3,12,13,11,14,10');

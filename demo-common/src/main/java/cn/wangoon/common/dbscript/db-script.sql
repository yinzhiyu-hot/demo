-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        8.0.18 - MySQL Community Server - GPL
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  11.0.0.5919
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- 导出 demo 的数据库结构
DROP DATABASE IF EXISTS `demo`;
CREATE DATABASE IF NOT EXISTS `demo` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `demo`;

-- 导出  表 demo.sync_task 结构
DROP TABLE IF EXISTS `sync_task`;
CREATE TABLE IF NOT EXISTS `sync_task` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `task_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '任务类型',
  `task_desc` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '任务描述',
  `task_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '任务状态(0 待处理 1 处理中 2 已处理 3 处理失败 99 处理异常 100 终止)',
  `process_count` int(11) NOT NULL DEFAULT '0' COMMENT '任务执行次数',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `finish_date` datetime DEFAULT NULL COMMENT '完成时间',
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间戳',
  PRIMARY KEY (`id`),
  KEY `idx_task_type` (`task_type`),
  KEY `idx_task_status` (`task_status`)
) ENGINE=InnoDB AUTO_INCREMENT=417456 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='异步任务';

-- 数据导出被取消选择。

-- 导出  表 demo.sync_task_data 结构
DROP TABLE IF EXISTS `sync_task_data`;
CREATE TABLE IF NOT EXISTS `sync_task_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `task_id` int(11) NOT NULL COMMENT '任务表主键',
  `task_data` varchar(10000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '任务数据',
  `data_index` tinyint(4) DEFAULT NULL COMMENT '截断顺序',
  PRIMARY KEY (`id`),
  KEY `idx_task_id` (`task_id`),
  KEY `idx_task_index` (`data_index`)
) ENGINE=InnoDB AUTO_INCREMENT=499215 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='同步任务数据';

-- 数据导出被取消选择。

-- 导出  表 demo.sync_task_exception 结构
DROP TABLE IF EXISTS `sync_task_exception`;
CREATE TABLE IF NOT EXISTS `sync_task_exception` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `task_id` int(11) NOT NULL COMMENT '任务表主键',
  `task_exception` varchar(10000) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '异常信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='任务异常信息';

-- 数据导出被取消选择。

-- 导出  表 demo.sys_base_config 结构
DROP TABLE IF EXISTS `sys_base_config`;
CREATE TABLE IF NOT EXISTS `sys_base_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '基础配置表主键',
  `biz_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '业务类型',
  `biz_key` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '业务配置键',
  `biz_value` varchar(500) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '业务配置值',
  `remark` varchar(1000) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '备注',
  `del_flag` tinyint(4) DEFAULT '0' COMMENT '是否删除 0 否 1 是',
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间戳',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=147 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='系统配置表';

-- 数据导出被取消选择。

-- 导出  表 demo.sys_job_config 结构
DROP TABLE IF EXISTS `sys_job_config`;
CREATE TABLE IF NOT EXISTS `sys_job_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `job_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'job名称',
  `job_class_bean_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'bean名称',
  `job_status` tinyint(4) NOT NULL COMMENT '状态(1 启动 0 停止)',
  `sharding_total_count` int(11) NOT NULL DEFAULT '1' COMMENT '分片数',
  `sharding_item_params` varchar(500) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '分配参数',
  `cron_expression` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'cron表达式',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '备注',
  `create_user` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_user` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='调度任务配置';

-- 数据导出被取消选择。

-- 导出  表 demo.sys_log 结构
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE IF NOT EXISTS `sys_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `platform` varchar(10) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '平台',
  `site` varchar(10) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '站点',
  `merchant` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '商户',
  `business_key` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '业务键(订单号等等)',
  `original_fbm_sku` varchar(300) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '平台同步到的商品编码',
  `fbm_sku` varchar(300) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '平台同步到的商品编码（清洗后缀的）',
  `plat_sku` varchar(300) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'wms对应的商品编码platsku',
  `sku` varchar(300) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'wms对应的商品编码sku',
  `upc` varchar(300) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'wms对应的商品编码upc',
  `fn_sku` varchar(300) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'wms对应的商品编码fnsku',
  `message` varchar(10000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '日志消息',
  `record_date` date DEFAULT NULL COMMENT '记录日期',
  `ts` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间戳',
  PRIMARY KEY (`id`),
  KEY `idx_business_key` (`business_key`),
  KEY `idx_platform` (`platform`),
  KEY `idx_site` (`site`),
  KEY `idx_merchant` (`merchant`),
  KEY `idx_record_date` (`record_date`)
) ENGINE=InnoDB AUTO_INCREMENT=1749346 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='日志流水表';

-- 数据导出被取消选择。

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;

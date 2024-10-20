/*
 Navicat Premium Data Transfer

 Source Server         : 本地虚拟机
 Source Server Type    : MySQL
 Source Server Version : 50738
 Source Host           : 192.168.245.128:3306
 Source Schema         : mall

 Target Server Type    : MySQL
 Target Server Version : 50738
 File Encoding         : 65001

 Date: 19/10/2024 21:53:26
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ams_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `ams_dict_type`;
CREATE TABLE `ams_dict_type`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典类型 id',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典类型',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型 code',
  `is_del` tinyint(4) NULL DEFAULT 0 COMMENT '是否删除 0：未删除，1：已删除',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ams_dict_value
-- ----------------------------
DROP TABLE IF EXISTS `ams_dict_value`;
CREATE TABLE `ams_dict_value`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典值id',
  `pid` bigint(20) NULL DEFAULT NULL COMMENT '父id',
  `value` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典名',
  `is_del` tinyint(4) NULL DEFAULT 0 COMMENT '是否删除 0：未删除，1：已删除',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ams_menu
-- ----------------------------
DROP TABLE IF EXISTS `ams_menu`;
CREATE TABLE `ams_menu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单id',
  `pid` bigint(20) NULL DEFAULT NULL COMMENT '父菜单id',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单名称',
  `path` varchar(90) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单路径',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序字段，值越小位置越靠前',
  `is_del` tinyint(4) NULL DEFAULT 0 COMMENT '是否删除 0：未删除，1：已删除',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ams_role
-- ----------------------------
DROP TABLE IF EXISTS `ams_role`;
CREATE TABLE `ams_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色名',
  `is_del` tinyint(4) NULL DEFAULT 0 COMMENT '是否删除 0：未删除，1：已删除',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ams_role_menu_rel
-- ----------------------------
DROP TABLE IF EXISTS `ams_role_menu_rel`;
CREATE TABLE `ams_role_menu_rel`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色id',
  `menu_id` bigint(20) NULL DEFAULT NULL COMMENT '菜单id',
  `is_del` tinyint(4) NULL DEFAULT 0 COMMENT '是否删除 0：未删除，1：已删除',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 147 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ams_user
-- ----------------------------
DROP TABLE IF EXISTS `ams_user`;
CREATE TABLE `ams_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `account` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户账号',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '账号密码',
  `status` int(11) NULL DEFAULT NULL COMMENT '账号状态 0：禁用，1：启用',
  `is_del` tinyint(4) NULL DEFAULT 0 COMMENT '是否删除 0：未删除，1：已删除',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ams_user_role_rel
-- ----------------------------
DROP TABLE IF EXISTS `ams_user_role_rel`;
CREATE TABLE `ams_user_role_rel`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色id',
  `is_del` tinyint(4) NULL DEFAULT 0 COMMENT '是否删除 0：未删除，1：已删除',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for home_page
-- ----------------------------
DROP TABLE IF EXISTS `home_page`;
CREATE TABLE `home_page`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '首页json字符串',
  `is_del` tinyint(4) NULL DEFAULT 0 COMMENT '是否删除 0：未删除，1：已删除',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for oms_after_sale
-- ----------------------------
DROP TABLE IF EXISTS `oms_after_sale`;
CREATE TABLE `oms_after_sale`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '售后 id',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '会员 id',
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT '订单 id',
  `order_item_id` bigint(20) NULL DEFAULT NULL COMMENT '子订单 id',
  `sn` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '售后订单号',
  `sku_name` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `sku_num` int(11) NULL DEFAULT NULL COMMENT '商品数量',
  `sku_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '商品单价',
  `sku_attrs` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '商品属性',
  `sku_img` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品图片',
  `apply_num` int(11) NULL DEFAULT NULL COMMENT '退货数量',
  `apply_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '申请退款金额',
  `actual_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '实际退款金额',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '售后类型 退款、退货退款',
  `reason` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '售后原因',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '售后状态',
  `delivery_company` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物流公司',
  `delivery_sn` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物流单号',
  `is_del` tinyint(4) NULL DEFAULT 0 COMMENT '是否删除 0：未删除，1：已删除',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for oms_order
-- ----------------------------
DROP TABLE IF EXISTS `oms_order`;
CREATE TABLE `oms_order`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单id',
  `member_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `shop_id` bigint(20) NULL DEFAULT NULL COMMENT '店铺id',
  `sn` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单号',
  `batch_sn` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '批次订单号',
  `status` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单状态',
  `pay_status` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付状态',
  `pay_type` int(11) NULL DEFAULT NULL COMMENT '支付方式 1：微信支付、2：支付宝支付',
  `batch_pay` int(11) NULL DEFAULT NULL COMMENT '是否批量支付 0：否、1：是',
  `receiver_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货人手机',
  `receiver_address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货人地址',
  `message` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '留言',
  `is_del` tinyint(4) NULL DEFAULT 0 COMMENT '是否删除 0：未删除，1：已删除',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 87 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for oms_order_item
-- ----------------------------
DROP TABLE IF EXISTS `oms_order_item`;
CREATE TABLE `oms_order_item`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '子订单id',
  `member_id` bigint(20) NULL DEFAULT NULL COMMENT '会员id',
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT '订单id',
  `shop_id` bigint(20) NULL DEFAULT NULL COMMENT '店铺id',
  `spu_id` bigint(20) NULL DEFAULT NULL COMMENT 'spu id',
  `sku_id` bigint(20) NULL DEFAULT NULL COMMENT 'sku id',
  `member_coupon_id` bigint(20) NULL DEFAULT NULL COMMENT '会员优惠券id',
  `sku_name` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `sku_num` int(11) NULL DEFAULT NULL COMMENT '商品数量',
  `sku_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '商品单价',
  `sku_attrs` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '商品属性',
  `sku_img` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品图片',
  `status` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单状态',
  `pay_status` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付状态',
  `goods_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '商品总额',
  `coupon_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '优惠金额',
  `delivery_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '运费金额',
  `delivery_company` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物流公司',
  `delivery_sn` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物流单号',
  `is_del` tinyint(4) NULL DEFAULT 0 COMMENT '是否删除 0：未删除，1：已删除',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 94 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pms_brand
-- ----------------------------
DROP TABLE IF EXISTS `pms_brand`;
CREATE TABLE `pms_brand`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '品牌id',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌名称',
  `img` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌图片',
  `detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '品牌详情',
  `is_del` tinyint(4) NULL DEFAULT 0 COMMENT '是否删除 0：未删除，1：已删除',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pms_category
-- ----------------------------
DROP TABLE IF EXISTS `pms_category`;
CREATE TABLE `pms_category`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '分类id',
  `pid` bigint(20) NULL DEFAULT NULL COMMENT '父分类id',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类名称',
  `img` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类图片',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序字段，值越小位置越靠前',
  `is_del` tinyint(4) NULL DEFAULT 0 COMMENT '是否删除 0：未删除，1：已删除',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3147 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pms_freight
-- ----------------------------
DROP TABLE IF EXISTS `pms_freight`;
CREATE TABLE `pms_freight`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '运费模板id',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模板名称',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '计费类型',
  `first_weight` double NULL DEFAULT NULL COMMENT '首重',
  `first_weight_fee` decimal(10, 2) NULL DEFAULT NULL COMMENT '首重费用',
  `continue_weight` double NULL DEFAULT NULL COMMENT '续重',
  `continue_weight_fee` decimal(10, 2) NULL DEFAULT NULL COMMENT '续重费用',
  `first_count` int(11) NULL DEFAULT NULL COMMENT '首件',
  `first_count_fee` decimal(10, 2) NULL DEFAULT NULL COMMENT '首件费用',
  `continue_count` int(11) NULL DEFAULT NULL COMMENT '续件',
  `continue_count_fee` decimal(10, 2) NULL DEFAULT NULL COMMENT '续件费用',
  `is_del` tinyint(4) NULL DEFAULT 0 COMMENT '是否删除 0：未删除，1：已删除',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pms_shop
-- ----------------------------
DROP TABLE IF EXISTS `pms_shop`;
CREATE TABLE `pms_shop`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '店铺id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '店铺名称',
  `is_del` tinyint(4) NULL DEFAULT 0 COMMENT '是否删除 0：未删除，1：已删除',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pms_sku
-- ----------------------------
DROP TABLE IF EXISTS `pms_sku`;
CREATE TABLE `pms_sku`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'skuId',
  `spu_id` bigint(20) NULL DEFAULT NULL COMMENT '商品spuId',
  `shop_id` bigint(20) NULL DEFAULT NULL COMMENT '店铺id',
  `name` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'sku名称',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT 'sku价格',
  `line_price` decimal(10, 2) NULL DEFAULT NULL COMMENT 'sku划线价',
  `img` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'sku图片',
  `attrs` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'sku属性',
  `weight` double NULL DEFAULT NULL COMMENT 'sku重量',
  `stock` int(11) NULL DEFAULT NULL COMMENT 'sku库存',
  `is_del` tinyint(4) NULL DEFAULT 0 COMMENT '是否删除 0：未删除，1：已删除',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21940 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pms_spu
-- ----------------------------
DROP TABLE IF EXISTS `pms_spu`;
CREATE TABLE `pms_spu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'spuId',
  `shop_id` bigint(20) NULL DEFAULT NULL COMMENT '店铺id',
  `brand_id` bigint(20) NULL DEFAULT NULL COMMENT '品牌id',
  `category_id` bigint(20) NULL DEFAULT NULL COMMENT '商品分类id',
  `freight_id` bigint(20) NULL DEFAULT NULL COMMENT '运费模板id',
  `name` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '商品价格',
  `line_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '商品划线格',
  `first_img` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品首图',
  `intro` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '商品简介',
  `detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '商品详情',
  `imgs` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '商品轮播图',
  `service` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '支持的服务',
  `status` int(11) NULL DEFAULT NULL COMMENT '商品状态 0：未上架，1：已上架',
  `is_del` tinyint(4) NULL DEFAULT 0 COMMENT '是否删除 0：未删除，1：已删除',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21906 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pms_spu_attr
-- ----------------------------
DROP TABLE IF EXISTS `pms_spu_attr`;
CREATE TABLE `pms_spu_attr`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `spu_id` bigint(20) NULL DEFAULT NULL COMMENT '商品spu id',
  `pid` bigint(20) NULL DEFAULT NULL COMMENT '父属性id',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '属性名',
  `is_del` tinyint(4) NULL DEFAULT 0 COMMENT '是否删除 0：未删除，1：已删除',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 43897 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sms_activity
-- ----------------------------
DROP TABLE IF EXISTS `sms_activity`;
CREATE TABLE `sms_activity`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '活动id',
  `name` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '活动名',
  `banner` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '活动 banner',
  `is_del` tinyint(4) NULL DEFAULT 0 COMMENT '是否删除 0：未删除，1：已删除',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sms_activity_section
-- ----------------------------
DROP TABLE IF EXISTS `sms_activity_section`;
CREATE TABLE `sms_activity_section`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '活动分区id',
  `activity_id` bigint(20) NULL DEFAULT NULL COMMENT '活动id',
  `title` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '活动标题',
  `is_del` tinyint(4) NULL DEFAULT 0 COMMENT '是否删除 0：未删除，1：已删除',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 86 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sms_activity_section_spu
-- ----------------------------
DROP TABLE IF EXISTS `sms_activity_section_spu`;
CREATE TABLE `sms_activity_section_spu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '活动分区商品id',
  `activity_section_id` bigint(20) NULL DEFAULT NULL COMMENT '活动分区id',
  `spu_id` bigint(20) NULL DEFAULT NULL COMMENT '商品id',
  `is_del` tinyint(4) NULL DEFAULT 0 COMMENT '是否删除 0：未删除，1：已删除',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 619 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sms_coupon
-- ----------------------------
DROP TABLE IF EXISTS `sms_coupon`;
CREATE TABLE `sms_coupon`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '优惠券id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '优惠券名称',
  `type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '优惠券类型',
  `discount` decimal(10, 2) NULL DEFAULT NULL COMMENT '折扣',
  `deduct` decimal(10, 2) NULL DEFAULT NULL COMMENT '抵扣',
  `threshold` decimal(10, 2) NULL DEFAULT NULL COMMENT '使用门槛',
  `member_range` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户范围\r\nall：全部用户\r\nspecific：指定用户',
  `goods_range` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品范围\r\nall：全部商品\r\nspecific：指定商品',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '生效时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '过期时间',
  `is_del` tinyint(4) NULL DEFAULT 0 COMMENT '是否删除 0：未删除，1：已删除',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sms_coupon_spu
-- ----------------------------
DROP TABLE IF EXISTS `sms_coupon_spu`;
CREATE TABLE `sms_coupon_spu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `coupon_id` bigint(20) NULL DEFAULT NULL COMMENT '优惠券id',
  `spu_id` bigint(20) NULL DEFAULT NULL COMMENT '商品id',
  `is_del` tinyint(4) NULL DEFAULT 0 COMMENT '是否删除 0：未删除，1：已删除',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sms_seckill
-- ----------------------------
DROP TABLE IF EXISTS `sms_seckill`;
CREATE TABLE `sms_seckill`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '秒杀主键id',
  `name` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '秒杀活动名',
  `banner` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '秒杀活动 banner',
  `is_del` tinyint(4) NULL DEFAULT 0 COMMENT '是否删除 0：未删除，1：已删除',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sms_seckill_time
-- ----------------------------
DROP TABLE IF EXISTS `sms_seckill_time`;
CREATE TABLE `sms_seckill_time`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '秒杀时间段id',
  `seckill_id` bigint(20) NULL DEFAULT NULL COMMENT '秒杀主键id',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
  `is_del` tinyint(4) NULL DEFAULT 0 COMMENT '是否删除 0：未删除，1：已删除',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sms_seckill_time_spu
-- ----------------------------
DROP TABLE IF EXISTS `sms_seckill_time_spu`;
CREATE TABLE `sms_seckill_time_spu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '秒杀时间端商品id',
  `seckill_time_id` bigint(20) NULL DEFAULT NULL COMMENT '秒杀时间段id',
  `spu_id` bigint(20) NULL DEFAULT NULL COMMENT '商品id',
  `is_del` tinyint(4) NULL DEFAULT 0 COMMENT '是否删除 0：未删除，1：已删除',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 184 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ums_address
-- ----------------------------
DROP TABLE IF EXISTS `ums_address`;
CREATE TABLE `ums_address`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '地址id',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '会员id',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电话',
  `province` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '省',
  `city` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '市',
  `county` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区',
  `province_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '省 code',
  `city_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '市 code',
  `county_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区 code',
  `detail` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '详细地址',
  `is_default` tinyint(4) NULL DEFAULT NULL COMMENT '是否为默认地址，1：是，0：否',
  `is_del` tinyint(4) NULL DEFAULT 0 COMMENT '是否删除 0：未删除，1：已删除',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ums_cart
-- ----------------------------
DROP TABLE IF EXISTS `ums_cart`;
CREATE TABLE `ums_cart`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '购物车id',
  `sku_id` bigint(20) NULL DEFAULT NULL COMMENT 'skuId',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `quantity` int(11) NULL DEFAULT NULL COMMENT '商品数量',
  `selected` int(11) NULL DEFAULT NULL COMMENT '是否选中 0：未选中，1：已选中',
  `is_del` tinyint(4) NULL DEFAULT 0 COMMENT '是否删除 0：未删除，1：已删除',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 66 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ums_member
-- ----------------------------
DROP TABLE IF EXISTS `ums_member`;
CREATE TABLE `ums_member`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '会员id',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `account` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户账号',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '账号密码',
  `is_del` tinyint(4) NULL DEFAULT 0 COMMENT '是否删除 0：未删除，1：已删除',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ums_member_coupon
-- ----------------------------
DROP TABLE IF EXISTS `ums_member_coupon`;
CREATE TABLE `ums_member_coupon`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '会员优惠券id',
  `member_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `coupon_id` bigint(20) NULL DEFAULT NULL COMMENT '优惠券id',
  `order_item_id` bigint(20) NULL DEFAULT NULL COMMENT '子订单id',
  `status` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态\r\nunUse：未使用\r\nused：已使用',
  `is_del` tinyint(4) NULL DEFAULT 0 COMMENT '是否删除 0：未删除，1：已删除',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

# 酷鸽商城后端服务

kuge-mall是一套完整的在线购物系统，包括C端和管理端，客户可在C端体验商品浏览、下单支付、申请售后、参与秒杀、使用优惠券等完整的购物流程，管理员可在后台创建商品、定制营销规则、管理订单，管理店铺、创建活动，对商品、订单、用户进行管理；技术栈为SpringBoot、SpringCloud、SpringGateway、MySQL、Redis、RabbitMQ、JWT、Nacos、OpenFeign、JSR303

![合集](/document/assets/img/h5/合集.jpg "合集")
![合集](/document/assets/img/admin/合集.jpg "合集")

## 目录
1. [项目模块](#项目模块)
1. [技术栈](#技术栈)
1. [开发工具](#开发工具)
1. [nacos配置](#nacos配置)
1. [mysql配置](#mysql配置)
1. [rabbitmq配置](#rabbitmq配置)
1. [maven配置](#maven配置)
1. [idea配置](#idea配置)
1. [启动服务](#启动服务)
1. [联系作者](#联系作者)
1. [鸣谢](#鸣谢)
1. [H5端演示图例](#H5端演示图例)
1. [管理端演示图例](#管理端演示图例)


## 项目模块
``` lua
mall
├── mall-admin        -- 后台管理服务
├── mall-auth         -- 权限认证服务
├── mall-cart         -- 购物车服务
├── mall-common       -- 公用模块
├── mall-gateway      -- 网关服务
├── mall-member       -- C端用户服务
└── mall-order        -- 订单服务
└── mall-product      -- 商品服务
└── mall-sale         -- 营销服务
└── mall-thirdaprt    -- 第三方集成服务
```

## 技术栈
| 技术                                 | 介绍               | 官网                              |
|------------------------------------|------------------|---------------------------------|
| SpringBoot                         | 简化Spring开发的脚手架框架 | https://spring.io/projects/spring-boot |
| SpringCloud                        | 微服务框架            | https://spring.io/projects/spring-cloud |
| SpringGateway                      | 微服务架构中网关         | https://spring.io/projects/spring-cloud-gateway |
| MySQL                              | 关系型数据库           | https://www.mysql.com/cn |
| Redis                              | 内存数据库            | https://redis.io  |
| Redisson                           | 实现分布式锁           | https://github.com/redisson/redisson |
| RabbitMQ                           | 消息队列             | https://www.rabbitmq.com |
| jjwt                               | 实现JWT的maven包     | https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt |
| Nacos                              | 配置服务中心           | https://nacos.io/docs/latest/what-is-nacos |
| OpenFeign                          | 访问远程服务的HTTP客户端   | https://spring.io/projects/spring-cloud-openfeign |
| Validator                         | 实现JSR303的框架      | https://spring.io/projects/spring-boot |
| Lombok                             | 简化Spring开发的脚手架框架 | https://projectlombok.org |
| Druid                              | 简化Spring开发的脚手架框架 | https://github.com/alibaba/druid/wiki/%E9%A6%96%E9%A1%B5 |
| Wechatpay | 微信支付maven包       | https://spring.io/projects/spring-boot |
| Maven                              | 项目的管理和构建工具       | https://maven.apache.org |
| 阿里云OSS                             | 文件存储服务           | https://www.aliyun.com |
| 阿里云SMS                             | 短信服务             | https://www.aliyun.com |

## 开发工具
* jdk8
* mysql5.7
* nacos2.2.0
* redis
* rabbitmq
* maven3.9.6
* intelliJ idea 2024.1
 
tips：需要先安装好上面的开发工具，再进行后面开发工具的配置


## nacos配置
1. 安装 nacos 并登录
2. 创建命名空间 mall-dev
![创建命名空间.png](/document/assets/img/nacos/创建命名空间.png)
3. 导入配置  
3.1 在配置列表界面导入配置文件
![导入配置.png](/document/assets/img/nacos/导入配置.png)
3.2 从项目的document文件夹中选中配置文件上传
![配置文件.png](/document/assets/img/nacos/配置文件.png)
3.3 生成如下的配置列表代表成功
![配置列表.png](/document/assets/img/nacos/配置列表.png)

## mysql配置
1. 安装 mysql 
2. 创建数据库 kuge-mall
   ![创建数据库.png](/document/assets/img/mysql/创建数据库.png)
3. 导入sql文件（文件位置：kuge-mall/document/mysql/mall.sql）
   ![执行sql文件.png](/document/assets/img/mysql/执行sql文件.png)
4. 展示库表
   ![库表.png](/document/assets/img/mysql/库表.png)

## rabbitmq配置
1. 安装 rabbitmq 并登录
2. 添加用户 mall
   ![添加用户.png](/document/assets/img/rabbitmq/添加用户.png)
3. 创建虚拟主机 mall
   ![添加虚拟主机.png](/document/assets/img/rabbitmq/添加虚拟主机.png)
3. 将虚拟主机mall，分配赋权给用户 mall
   ![分配虚拟主机.png](/document/assets/img/rabbitmq/分配虚拟主机.png)

## maven配置
1. 下载 maven
2. 配置阿里镜像源
   ```shell
   <mirror>
      <id>alimaven</id>
      <name>aliyun maven</name>
      <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
      <mirrorOf>central</mirrorOf>        
   </mirror>
   ```
3. 设置自定义本地仓库地址
   ```shell
   <localRepository>path/to/repository</localRepository>
   ```

## idea配置
![jdk配置](/document/assets/img/idea/jdk配置.png)
![maven配置](/document/assets/img/idea/maven配置.png)

## 拉取代码
```shell
# 拉取代码
git clone git@github.com:xiexianbao/kuge-mall.git
```

## 启动服务
![启动服务.png](/document/assets/img/idea/启动服务.png)
以上服务不需要按某种特定的顺序执行，**全部启动即可**。

后端服务启动好之后启动前端服务
* [酷鸽商城H5端](https://github.com/xiexianbao/kuge-mall-h5)
* [酷鸽商城管理端](https://github.com/xiexianbao/kuge-mall-admin)

## 联系作者
|                           微信好友                           |
|:--------------------------------------------------------:|
| <img src="/document/assets/img/微信二维码.jpg" width="200px"> |

## 鸣谢
本项目参考了以下项目，在此鸣谢~
* B站[谷粒商城](https://www.bilibili.com/video/BV1np4y1C7Yf/?spm_id_from=333.337.search-card.all.click&vd_source=11a060accac2997d6a7169be1a817da7)项目
* Github[mall](https://github.com/macrozheng/mall)项目
* B站[微信/支付宝支付](https://www.bilibili.com/video/BV1US4y1D77m/?spm_id_from=333.337.search-card.all.click&vd_source=11a060accac2997d6a7169be1a817da7)项目

## H5端演示图例
<div>

  <img src="/document/assets/img/h5/首页.png" />

  <img src="/document/assets/img/h5/分类.png" />

  <img src="/document/assets/img/h5/购物车.png" />

  <img src="/document/assets/img/h5/我的.png" />

  <img src="/document/assets/img/h5/搜索结果.png" />

  <img src="/document/assets/img/h5/营销活动.png" />

  <img src="/document/assets/img/h5/秒杀活动.png" />

  <img src="/document/assets/img/h5/商品详情.png" />

  <img src="/document/assets/img/h5/确认订单.png" />

  <img src="/document/assets/img/h5/选择地址.png" />

  <img src="/document/assets/img/h5/支付.png" />

  <img src="/document/assets/img/h5/支付二维码.png" />

  <img src="/document/assets/img/h5/支付成功.png" />

  <img src="/document/assets/img/h5/订单列表.png" />

  <img src="/document/assets/img/h5/待支付订单.png" />

  <img src="/document/assets/img/h5/待发货订单.png" />

  <img src="/document/assets/img/h5/订单详情.png" />

  <img src="/document/assets/img/h5/申请售后.png" />

  <img src="/document/assets/img/h5/售后列表.png" />

  <img src="/document/assets/img/h5/售后详情.png" />

  <img src="/document/assets/img/h5/地址列表.png" />

  <img src="/document/assets/img/h5/新增收货地址.png" />

  <img src="/document/assets/img/h5/编辑收货地址.png" />

  <img src="/document/assets/img/h5/删除收货地址.png" />

  <img src="/document/assets/img/h5/优惠券列表.png" />

  <img src="/document/assets/img/h5/个人中心.png" />

  <img src="/document/assets/img/h5/修改昵称.png" />

  <img src="/document/assets/img/h5/更换手机号.png" />

  <img src="/document/assets/img/h5/登录.png" />

</div>


## 管理端演示图例

<div>

  <img src="/document/assets/img/admin/用户管理.png" />

  <img src="/document/assets/img/admin/添加用户.png" />

  <img src="/document/assets/img/admin/编辑用户.png" />

  <img src="/document/assets/img/admin/修改密码.png" />

  <img src="/document/assets/img/admin/角色管理.png" />

  <img src="/document/assets/img/admin/添加角色.png" />

  <img src="/document/assets/img/admin/编辑角色.png" />

  <img src="/document/assets/img/admin/菜单管理.png" />

  <img src="/document/assets/img/admin/添加菜单.png" />

  <img src="/document/assets/img/admin/编辑菜单.png" />

  <img src="/document/assets/img/admin/字典管理.png" />

  <img src="/document/assets/img/admin/添加字典类型.png" />

  <img src="/document/assets/img/admin/字典值列表.png" />

  <img src="/document/assets/img/admin/添加字典值.png" />

  <img src="/document/assets/img/admin/分类管理.png" />

  <img src="/document/assets/img/admin/添加分类.png" />

  <img src="/document/assets/img/admin/编辑分类.png" />

  <img src="/document/assets/img/admin/品牌管理.png" />

  <img src="/document/assets/img/admin/添加品牌.png" />

  <img src="/document/assets/img/admin/编辑品牌.png" />

  <img src="/document/assets/img/admin/商品管理.png" />

  <img src="/document/assets/img/admin/添加商品.png" />

  <img src="/document/assets/img/admin/编辑商品.png" />

  <img src="/document/assets/img/admin/活动管理.png" />

  <img src="/document/assets/img/admin/添加活动.png" />

  <img src="/document/assets/img/admin/编辑活动.png" />

  <img src="/document/assets/img/admin/秒杀管理.png" />

  <img src="/document/assets/img/admin/添加秒杀活动.png" />

  <img src="/document/assets/img/admin/编辑秒杀活动.png" />

  <img src="/document/assets/img/admin/优惠券管理.png" />

  <img src="/document/assets/img/admin/添加优惠券.png" />

  <img src="/document/assets/img/admin/编辑优惠券.png" />

  <img src="/document/assets/img/admin/订单列表.png" />

  <img src="/document/assets/img/admin/订单详情.png" />

  <img src="/document/assets/img/admin/售后管理.png" />

  <img src="/document/assets/img/admin/售后详情.png" />

  <img src="/document/assets/img/admin/店铺管理.png" />

  <img src="/document/assets/img/admin/添加店铺.png" />

  <img src="/document/assets/img/admin/编辑店铺.png" />

  <img src="/document/assets/img/admin/运费模板.png" />

  <img src="/document/assets/img/admin/添加运费模板.png" />

  <img src="/document/assets/img/admin/编辑运费模板.png" />

  <img src="/document/assets/img/admin/首页配置.png" />

</div>
### 前提条件
nacos 依赖 java 环境来运行，需要先[安装jdk](https://github.com/xiexianbao/kuge-mall/wiki/%E5%AE%89%E8%A3%85-jdk1.8)，然后再继续后面的安装步骤

### 下载 nacos 2.2.0
* [官网下载](https://nacos.io/download/nacos-server)
* [github下载](https://github.com/alibaba/nacos/releases/tag/2.2.0)
* [百度网盘下载](https://pan.baidu.com/s/1HrCJZXz9YjMo_VN8z1ZROw?pwd=mr79)

### 开始安装
将下载的安装包上传到服务器根目录
```shell
ls /
# nacos-server-2.2.0.tar.gz bin  boot  dev  etc  home  lib  lib64  media  mnt ...
```

将安装包移动到 /usr/local 目录下
```shell
mv /nacos-server-2.2.0.tar.gz /usr/local
```

将安装包解压
```shell
# 进入 /usr/local
cd /usr/local

# 开始解压
tar -zxvf nacos-server-2.2.0.tar.gz
```

运行 nacos
```shell
sh nacos/bin/startup.sh -m standalone
```

验证是否安装成功
```shell
cat nacos/logs/start.out

# 打印内容中包含如下内容表示启动成功
# Nacos started successfully in stand alone mode. use embedded storage 
```

放行web端控制台端口：8848
```shell
firewall-cmd --zone=public --add-port=8848/tcp --permanent
firewall-cmd --reload
```

### 登录 web  控制台
控制台地址：服务器ip:8848/nacos  
用户名/密码： nacos/nacos

![image](https://github.com/user-attachments/assets/09f45d61-1d33-4302-b465-223e66fc7b4e)

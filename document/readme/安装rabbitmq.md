### 前提条件
rabbitmq 依赖 erlang 环境来运行，需要先erlang

### 下载安装包
* [rabbitmq下载地址](https://pan.baidu.com/s/1Wh0HIP0cfwleI6UgS4SpKw?pwd=ul1g)
* [erlang下载](https://pan.baidu.com/s/1MgR9_LIubhPYxAUoH2NRWw?pwd=19hc)

### 开始安装
将下载的安装包上传到服务器根目录

```shell
ls /
# rabbitmq-server-3.8.11-1.el7.noarch.rpm erlang-23.2.3-1.el7.x86_64.rpm bin  boot  dev  etc  home  lib  lib64  media  mnt ...
```

将安装包移动到 /usr/local 目录下
```shell
mv /rabbitmq-server-3.8.11-1.el7.noarch.rpm /usr/local
mv /erlang-23.2.3-1.el7.x86_64.rpm /usr/local
```

安装 erlang
```shell
# 进入 /usr/local
cd /usr/local

# 解压
rpm -Uvh erlang-23.2.3-1.el7.x86_64.rpm

# 安装
yum install -y erlang
```

安装 rabbitmq
```shell
# 先安装 socat
yum install -y socat

# 解压
rpm -Uvh rabbitmq-server-3.8.11-1.el7.noarch.rpm

# 安装
yum install -y rabbitmq-server
```

启动服务
```shell
systemctl start rabbitmq-server
```

启动 web 管理端
```shell
rabbitmq-plugins enable rabbitmq_management
```

开启 web 管理端访问端口
```shell
firewall-cmd --zone=public --add-port=15672/tcp --permanent
firewall-cmd --reload
```
登录 web 控制台  
控制台地址：服务器ip:15672

![image](https://github.com/user-attachments/assets/7f25fc45-d122-4562-95ca-b5f1f3440c04)

### 下载 mysql
* [官网下载](https://downloads.mysql.com/archives/community)
<!--
![image](https://github.com/user-attachments/assets/e16f91ba-5b10-461a-9e60-4e065207cab2)
-->


* [百度网盘下载](https://pan.baidu.com/s/1CmM73YXR2TS5K7av216jGQ?pwd=rrvj)
<!--
![image](https://github.com/user-attachments/assets/43b4cea9-6a11-4841-bb6e-3ddd59060269)
-->

### 开始安装

将下载的安装包上传到服务器根目录

```shell
ls /
# mysql-5.7.42-linux-glibc2.12-x86_64.tar.gz bin  boot  dev  etc  home  lib  lib64  media  mnt ...
```


将安装包移动到 /usr/local 目录下
```shell
mv /mysql-5.7.42-linux-glibc2.12-x86_64.tar.gz /usr/local
```


将软件包解压
```shell
tar -zxvf mysql-5.7.42-linux-glibc2.12-x86_64.tar.gz mysql
```

创建数据存储文件
```shell
mkdir /usr/local/mysql/data
```

创建 mysql 用户和用户组（初始化数据库时用）
```shell
groupadd mysql
useradd -g mysql mysql
```


将存储文件（/usr/local/mysql/data）的用户和用户组设置为新建的 mysql
```shell
chown -R mysql:mysql /usr/local/mysql
```

创建配置文件 /etc/my.cnf
```shell
vim /etc/my.cnf
```
然后将如下内容复制到 /etc/my.cnf 并保存
```shell
[mysqld]
bind-address=0.0.0.0
port=3306
user=mysql
basedir=/usr/local/mysql
datadir=/usr/local/mysql/data
socket=/tmp/mysql.sock
log-error=/usr/local/mysql/data/mysql.err
pid-file=/usr/local/mysql/data/mysql.pid
#character config
character_set_server=utf8mb4
symbolic-links=0
explicit_defaults_for_timestamp=true
```

初始化 mysql
```shell
# 进入 mysql 的二进制文件目录
cd /usr/local/mysql/bin/

# 使用 mysqld 初始化
./mysqld --defaults-file=/etc/my.cnf --basedir=/usr/local/mysql/ --datadir=/usr/local/mysql/data/ --user=mysql --initialize
```

将 mysql 添加为服务
```shell
cp /usr/local/mysql/support-files/mysql.server /etc/init.d/mysql
cp /usr/local/mysql/bin/mysql /bin/mysql
```

启动 mysql 服务
```shell
service mysql start
# Starting MySQL SUCCESS!
```

查看服务状态
```shell
service mysql status

# SUCCESS! MySQL running (1836)
```
***

查看 mysql 初始密码，并使用初始密码登录
```shell
# 初始密码在 /usr/local/mysql/data/mysql.err 文件中，使用 cat 命令查看
cat /usr/local/mysql/data/mysql.err
```
![image](https://github.com/user-attachments/assets/a275e96d-124d-4085-a0b8-5eb821cd39ea)


通过初始密码进行登录，登录之后修改密码为 root
```shell
ALTER USER 'root'@'localhost' IDENTIFIED BY 'root';
FLUSH PRIVILEGES;
```
设置完成后通过新设置的密码进行登录

### 问题汇总
无法远程登录

如果使用 Navicat、SQLyog 无法连接数据库，可尝试以下方式解决：

1. 开启防火墙3306端口
```shell
firewall-cmd --zone=public --add-port=3306/tcp --permanent
```
2. 修改 root 用户的 host 属性
```shell
use mysql
update user set host = '%' where user = 'root';
FLUSH PRIVILEGES;
```

设置完成后重新连接
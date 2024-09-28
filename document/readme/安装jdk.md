### 下载 jdk 1.8
* [阿里云下载](https://pan.baidu.com/s/1_S4LraLlH6hWZ2VpjlNgxg?pwd=cxw8 )
* [华为云下载](https://repo.huaweicloud.com/java/jdk)  版本：8u151-b12
* [官网下载](https://www.oracle.com/java/technologies/javase/javase8-archive-downloads.html)
  ![image](https://github.com/user-attachments/assets/ecb69d3f-57e7-405e-a2f6-b50eadcac97d)


### 开始安装

将下载的安装包上传到服务器根目录

```shell
ls /
# jdk-8u151-linux-x64.tar.gz  bin  boot  dev  etc  home  lib  lib64  media  mnt  opt ...
```

将安装包移动到 /usr/local 目录下
```shell
mv /jdk-8u151-linux-x64.tar.gz /usr/local
```

将安装包解压
```shell
# 进入 /usr/local
cd /usr/local

# 开始解压
tar -zxvf jdk-8u151-linux-x64.tar.gz

# 将解压出来的文件重名为 jdk
mv jdk1.8.0_151 jdk
```

配置环境变量
```shell
# 编辑 /etc/profile 文件
vim /etc/profile
```
将下面内容追加到 /etc/profile 文件末尾
```shell
export JAVA_HOME=/usr/local/jdk
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar:$JAVA_HOME/jre/lib/rt.jar
export PATH=$JAVA_HOME/bin:$PATH
```

重载 /etc/profile 文件，使新增的环境变量生效
```shell
source /etc/profile
```

验证是否安装成功
```shell
java -version

# 展示版本信息代表成功
# java version "1.8.0_151"
# Java(TM) SE Runtime Environment (build 1.8.0_151-b12)
# Java HotSpot(TM) 64-Bit Server VM (build 25.151-b12, mixed mode)
```
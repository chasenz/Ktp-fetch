# Ktp_spider
输入用户名、密码模拟登陆，根据登陆凭证下载测试页面题目。

## 部署

项目采用Maven管理，推荐使用IDEA平台开发；

在IDEA或Eclipse环境中导入项目，选择Maven项目。

如需生成release版本则需要删除./src目录下的META-INF文件，以防止出错。

## 使用

[Release版本](https://github.com/Umbrellazc/Ktp-fetch/releases)
```
java -jar fetch-ktp-test.jar
```
```
请输入您的用户名：your email or phone 
请输入您的密 码： your password
请输入您的抓取地址：complete url
测试题目已经保存在当前目录，文件名为myTest.txt

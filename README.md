# API-back-end
API大作业

# 代码结构
├─src
│  ├─main
│  │  ├─java
│  │  │  └─apiwork
│  │  │      ├─config           # 配置文件
│  │  │      ├─controller
│  │  │      ├─exception        
│  │  │      ├─interceptors     # 拦截器 
│  │  │      ├─mapper
│  │  │      ├─pojo
│  │  │      ├─service          # Service和接口
│  │  │      │  └─impl
│  │  │      └─utils            # 工具类
│  │  └─resources
│  │      └─apiwork
│  │          └─mapper
│  └─test
│      └─java
│          └─apiwork
├─Douyin_Spider                 # 抖音视频爬虫
├─sql                           # 存放数据库结构sql文件
├─Apifox                    # 存放后端文档


# 数据库
后端使用MySQL数据库和Redis数据库。
使用MySQL运行sql文件创建数据库。

## user表

## video表

# 爬虫使用
爬虫可以实现爬取单一用户的部分视频，若要爬取其他用户需要自行更换URL地址。

## 使用步骤
登录抖音 -> 查看需要爬取的用户页面 -> F12 Network页面查看https://www.douyin.com/aweme/v1/web/aweme/post/的详细URL和headers -> 替换爬虫代码中的URL和headers

## 爬虫参数
**url**：爬取视频的请求URL
**headers**：头部信息
**video_url**：视频保存的绝对路径，需自行修改。（<font color="#ff0000">注意！</font>需保持和Springboot中application.yml配置文件的视频保存路径一致）

# 后端接口链接（Apifox）
https://apifox.com/apidoc/shared-3521dde7-5c24-4365-b9f1-c8f8d479cc1f
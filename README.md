# HuiQue Online Judge

## 简介

“灰鹊”(HQOJ)是一款基于springboot的在线判题系统，判题机由java + docker实现。

和传统oj相比，灰雀的分布式架构拥有更好的水平扩展性，拥有更高的性能和可用性。

## 使用方式
- 本地安装jdk11
- 启动redis，并在application.yaml中配置redis地址
- 启动rocketmq，并在application.yaml中配置nameserver地址
- 可以在ide中直接运行HqojApplication.java
- 生产部署
    - 使用mvn package命令打包
    - 生成的target/xxxxx.jar就是打包后的可运行文件
    - 使用java -jar xxxxxx.jar来运行。
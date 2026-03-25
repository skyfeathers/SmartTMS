# TMS 运输管理系统

## 项目简介

TMS (Transportation Management System) 是一套完整的运输管理系统，由 **1024lab** 团队开发。系统采用 Spring Boot 框架，基于 Java 8 和 Maven 多模块架构设计，提供货主管理、司机管理、车辆管理、挂车管理、业务资料、油卡管理、报表统计、流程审批、固定资产等核心功能。

### 核心技术
**项目技术栈**
- 开发语言：<font color="green"> Java 1.8 </font>
- 开发框架：<font color="green"> SpringBoot 2.5.2 </font>
- 构建工具：<font color="green"> Maven </font>
- 数据存储：<font color="green"> Mysql 8.0.23 </font>
- 缓存实现：<font color="green"> Redis </font>
- 接口文档：<font color="green"> Swagger</font>
- 数据访问：<font color="green"> Mybatis Plus-  3.4.1 </font>
- 权限控制：<font color="green"> Spring Security </font>
- 工具类库：<font color="green"> Hutool 5.7.22 </font>

**第三方服务**
- ETC开票：<font color="green"> 百望、路耘 </font>
- 车辆轨迹：<font color="green"> 中交兴路 </font>
- 电子签章：<font color="green"> 尚尚签、君子签、e签宝 </font>
- 银企直连：<font color="green"> 平安银行、华夏银行、宁波银行、郑州银行等 </font>
- 地图   ：<font color="green"> 百度地图 </font>
- OCR识别：<font color="green"> 百度、阿里、华为 </font>
- 文件存储：<font color="green"> 本地存储、阿里OSS、华为OBS等 </font>
- 短信服务：<font color="green"> 阿里云短信 </font>


## 项目结构

本项目采用 Maven 多模块架构，主要包含以下子模块:

```
tms-service/
├── tms-parent/              # 父工程，依赖版本管理和公共配置
├── tms-common/              # 公共模块，通用工具类、常量定义、基础服务
├── tms-admin/               # 管理后台模块
├── tms-fixed-asset/         # 固定资产管理模块
├── tms-driver/              # 司机端应用模块
└── tms-job/                 # 定时任务调度模块
```

### 模块说明
```
| 模块名称| 包名| 描述
|-------------------|------------------------------|
| `tms-parent`      | -                            | 父工程，负责统一依赖版本管理和公共配置
| `tms-common`      | `net.lab1024.tms.common`     | 公共模块，包含通用工具类、常量定义、基础服务、DAO、Entity等
| `tms-admin`       | `net.lab1024.tms.admin`      | 管理后台
| `tms-fixed-asset` | `net.lab1024.tms.fixedasset` | 固定资产
| `tms-driver`      | `net.lab1024.tms.driver`     | 司机端
| `tms-job`         | `net.lab1024.tms.job`        | 定时任务
```
### 目录结构规范
```
src/main/java/net/lab1024/tms/{module}/
├── controller/     # 控制器层
├── service/        # 服务层
├── manager/        # 业务管理层
├── dao/           # 数据访问层
├── domain/        # 实体类
│   ├── entity/    # 数据库实体
│   └── vo/        # 视图对象
│   └── form/      # 请求表单
└── support/       # 支持类
```

## 维护团队

本项目由 **1024lab** 团队开发和维护。

## 许可证

请查看项目根目录的 LICENSE 文件获取许可信息。


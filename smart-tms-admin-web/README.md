# TMS系统 - 管理后台

基于 Vue 3 + Vite + Ant Design Vue 的现代化运输管理系统管理后台

## 📋 项目概述

TMS（Transportation Management System）是一个功能强大、可扩展的企业级运输管理系统。该管理后台采用现代化前端技术栈开发，提供直观易用的界面，帮助企业高效管理运输业务流程。

### ✨ 主要特性

- **现代化技术栈**：基于 Vue 3、Vite 构建，提供快速的开发体验
- **组件库**：使用 Ant Design Vue，提供丰富的企业级 UI 组件
- **响应式设计**：支持多种设备尺寸，确保在不同终端上的一致体验
- **国际化**：内置中英文多语言支持
- **权限管理**：灵活的权限控制机制
- **模块化架构**：清晰的代码结构，易于维护和扩展

### 🚀 技术栈

| 技术 | 版本 | 描述 |
|------|------|------|
| Vue | 3.3.4 | 渐进式 JavaScript 框架 |
| Vite | 5.4.6 | 下一代前端构建工具 |
| Ant Design Vue | 4.2.6 | 企业级 UI 组件库 |
| Pinia | 2.0.14 | Vue 官方推荐的状态管理库 |
| Vue Router | 4.0.15 | Vue.js 官方路由管理器 |
| Axios | 1.4.0 | HTTP 客户端 |
| TypeScript | - | 可选的类型检查 |

## 🛠️ 开发环境要求

- **Node.js**: >=14.0.0
- **npm** 或 **yarn** 或 **pnpm**

## 📦 快速开始

### 1. 克隆项目

```bash
git clone *
cd tms-admin-web
```

### 2. 安装依赖

```bash
npm install
# 或者使用 yarn
yarn install
# 或者使用 pnpm
pnpm install
```

### 3. 启动开发服务器

```bash
# 开发模式
npm run dev

# 或者启动本地测试环境
npm run localhost
```

### 4. 构建生产版本

```bash
# 构建生产版本
npm run build

# 构建测试环境版本
npm run test

# 构建预发布环境版本
npm run sit

# 构建正式环境版本
npm run prod
```

## 🗂️ 项目结构

```
src/
├── api/                    # API 接口定义
│   ├── business/           # 业务模块 API
│   ├── fixed-asset/        # 固定资产模块 API
│   ├── support/            # 支持模块 API
│   └── system/             # 系统模块 API
├── components/            # 公共组件
│   ├── audit-modal/       # 审核模态框
│   ├── baidu-map/         # 百度地图组件
│   ├── smart-table/       # 智能表格组件
│   └── ...                # 其他业务组件
├── config/                # 项目配置
├── constants/             # 常量定义
├── directives/            # Vue 指令
├── hook/                  # 自定义 Hook
├── i18n/                  # 国际化配置
├── layout/                # 页面布局
├── lib/                   # 工具库
├── plugins/               # Vue 插件
├── router/                # 路由配置
├── store/                 # 状态管理 (Pinia)
├── theme/                 # 主题样式
├── utils/                 # 工具函数
└── views/                 # 页面视图
    ├── business/          # 业务模块页面
    ├── fixed-asset/       # 固定资产模块页面
    ├── support/           # 支持模块页面
    └── system/            # 系统模块页面
```

## 🔧 配置说明

### 环境变量

项目使用 Vite 进行环境变量管理，可在 `.env` 文件中配置：

### 主题定制

项目支持主题定制，可通过以下方式修改：

1. 修改 [src/theme/index.less](./src/theme/index.less) 文件中的变量
2. 重新编译项目


## 🚀 部署

### 静态部署

构建后的文件位于 `dist/` 目录，可部署到任何静态服务器：


### 代码规范

- 使用 ESLint 进行代码检查
- 使用 Prettier 进行代码格式化
- 遵循 Git Commit 规范

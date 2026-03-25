/*
 * @Author: zhuoda
 * @Date: 2021-08-03 10:27:11
 * @LastEditTime: 2022-08-26
 * @LastEditors: zhuoda
 * @Description:
 * @FilePath: /smart-admin/src/config/app-config.ts
 */

import logoImg from '/@/assets/images/logo/smart-admin-logo.png';
import gongzhonghao from "/@/assets/images/1024lab/1024lab-gzh.jpg";
import zhuoda from "/@/assets/images/1024lab/zhuoda-wechat.jpg";
import loginQR from "/@/assets/images/login/login-qr.png";

/**
 * 应用默认配置
 */

export default {
  language: 'zh_CN',
  // 布局: side 或者 side-expand
  layout: 'side',
  // 侧边菜单宽度 ， 默认为200px
  sideMenuWidth: 200,
  // 菜单主题
  sideMenuTheme: 'dark',
  // 标签页
  pageTagFlag: true,
  // 面包屑
  breadCrumbFlag: true,
  //标签页位置
  pageTagLocation: 'top',
  // 页脚
  footerFlag: true,
  // 帮助文档
  helpDocFlag: false,
  // 网站图标
  faviconUrl: '',
  // 网站logo
  logoUrl: logoImg,
  // 备案号
  beiAnNo: '豫ICP备123456号-2',
  // android
  androidQr: gongzhonghao,
  // ios
  iosQr: zhuoda,
  // 登录二维码
  loginQr: loginQR,
  // 公司名称
  companyName: '1024实验室',
  // 网站名称
  websiteName: 'TMS系统',
  // 网站描述
  websiteDesc: ' ',
  dingdingLoginFlag: false,
  dingdingAppKey: '',
  dingdingRedirectUri: '',
  loginPagePath: 'common',
  compactFlag: true,
  // 百度AK
  baiduMapAk:'xxxxxxxxxxxx',
};

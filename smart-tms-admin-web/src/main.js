/*
 * @Description: 主方法
 * @Author: zhuoda
 * @Date: 2021-08-03
 * @LastEditTime: 2022-07-12
 * @LastEditors: zhuoda
 */
import * as antIcons from '@ant-design/icons-vue';
import Antd, { message } from 'ant-design-vue';
import lodash from 'lodash';
import { createApp } from 'vue';
import JsonViewer from 'vue3-json-viewer';
import 'vue3-json-viewer/dist/index.css';
import App from './App.vue';
import { loginApi } from '/@/api/system/login/login-api';
import constantsInfo from '/@/constants/index';
import { privilegeDirective } from '/@/directives/privilege';
import i18n from '/@/i18n/index';
import privilegePlugin from '/@/plugins/privilege-plugin';
import smartEnumPlugin from '/@/plugins/smart-enums-plugin';
import { buildRoutes, router } from '/@/router/index';
import { store } from '/@/store/index';
import { useUserStore } from '/@/store/modules/system/user';
import { useAppConfigStore } from '/@/store/modules/system/app-config';
import '/@/theme/index.less';
import { getTokenFromCookie } from '/@/utils/cookie-util';
import { localClear } from '/@/utils/local-util';
import { clearAllCoolies } from '/@/utils/cookie-util';
import { PAGE_PATH_LOGIN } from '/@/constants/common-const';
import SmartCopyIcon from '/@/components/smart-copy-icon/index.vue'
import { getAppConfig } from '/@/config/app-config';


/**
 * 获取企业信息
 */
async function getLoginInfo() {
  let vueInitialized = false;
  try {
    //获取登录用户信息
    const res = await loginApi.getLoginInfo();
    //构建系统的路由
    let menuRouterList = res.data.menuList.filter((e) => e.path || e.frameUrl);
    buildRoutes(menuRouterList);
    vueInitialized = true;
    initVue();
    //更新用户信息到pinia
    useUserStore().setUserLoginInfo(res.data);
  } catch (e) {
    console.log('**************************************');
    console.log(e);
    if(e.data && e.data.msg){
      message.error(e.data.msg);
    }
    if (!vueInitialized) {
      clearAllCoolies();
      localClear();
      initVue();
      location.href = window.location.pathname + '#' + PAGE_PATH_LOGIN;
    }
  }
}


function initVue() {
  let vueApp = createApp(App);
  let app = vueApp.use(router).use(store).use(i18n).use(Antd).use(smartEnumPlugin, constantsInfo).use(privilegePlugin).use(JsonViewer);
  //注入权限
  app.directive('privilege', {
    mounted(el, binding) {
      privilegeDirective(el, binding);
    },
  });
  // 注册图标组件
  Object.keys(antIcons).forEach((key) => {
    app.component(key, antIcons[key]);
  });
  app.component('SmartCopyIcon',SmartCopyIcon);
  //全局
  app.config.globalProperties.$antIcons = antIcons;
  app.config.globalProperties.$lodash = lodash;
  //挂载
  app.mount('#app');
}

//不需要获取用户信息、用户菜单、用户菜单动态路由，直接初始化vue即可
let token = getTokenFromCookie();
if (!token) {
  initVue();
  initLoginEnterprise();
} else {
  getLoginInfo();
}

/**
 * 获取登录企业信息
 */
async function initLoginEnterprise() {
  try {
    //获取登录用户信息
    const res = await loginApi.getLoginEnterprise();
    useAppConfigStore().setLoginEnterprise(res.data);
    initWebConfig();
  } catch (e) {
  }
}


/**
 * 初始化网站配置
 */
function initWebConfig() {
  // 更改网站标题
  window.document.title = useAppConfigStore().websiteName;
  // 更改网站favicon
  let link = document.querySelector("link[rel*='icon']");
  link.href = import.meta.env.BASE_URL + useAppConfigStore().faviconUrl;

}


// 初始化百度地图AK
function initBaiduMapAk() {
  // 百度地图js引入
  console.log('getAppConfig', getAppConfig())
  let baiduMapAk = getAppConfig().baiduMapAk;
  if(baiduMapAk){
    let baiduMapScript = document.createElement('script');
    baiduMapScript.type = 'text/javascript';
    baiduMapScript.src = `https://api.map.baidu.com/getscript?v=1.0&type=webgl&ak=${baiduMapAk}`
    document.getElementsByTagName('html')[0].appendChild(baiduMapScript);
  }
}

//initBaiduMapAk();

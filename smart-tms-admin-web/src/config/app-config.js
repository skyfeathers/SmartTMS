/*
 * @Author: zhuoda
 * @Date: 2021-08-03 10:27:11
 * @LastEditTime: 2022-08-26
 * @LastEditors: zhuoda
 * @Description:
 * @FilePath: /smart-admin/src/config/app-config.ts
 */
import saConfig from './sa-app-config';


export const loginPage = {
  'common': () => import('/@/views/system/login/login.vue'),
}

/**
 * 获取AppConfig
 * @returns {{layout: string, websiteName, breadCrumbFlag: boolean, footerFlag: boolean, sideMenuWidth: number, pageTagFlag: boolean, helpDocFlag: boolean, language: string, sideMenuTheme: string}}
 */
export const getAppConfig = () => {
  let project = import.meta.env.VITE_APP_PROJECT;
  switch (project) {
    default:
      return saConfig;
  }
};

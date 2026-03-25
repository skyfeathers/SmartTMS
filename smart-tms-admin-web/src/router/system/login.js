/*
 * @Description:
 * @Author: zhuoda
 * @Date: 2021-08-03
 * @LastEditTime: 2022-06-24
 * @LastEditors: zhuoda
 */

import { getAppConfig,loginPage } from '/@/config/app-config';
export const loginRouters = [
  {
    path: '/login',
    name: 'Login',
    component: loginPage[getAppConfig().loginPagePath],
    meta: {
      title: '登录',
      hideInMenu: true,
    },
  },
];

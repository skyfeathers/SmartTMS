/*
 * @Author: zhuoda
 * @Date: 2021-08-03 10:27:11
 * @LastEditTime: 2022-06-24
 * @LastEditors: zhuoda
 * @Description:
 * @FilePath: /smart-admin/src/api/system/login/login.ts
 */
import { getRequest, postRequest } from '/@/lib/axios';

export const loginApi = {
  /**
   * 登录
   * @param param
   */
  login: (param) => {
    return postRequest('/login', param);
  },

  /**
   * 登录
   * @param param
   */
  dingDingLogin: (authCode) => {
    return getRequest(`/auth?authCode=${authCode}`);
  },

  /**
   * 退出登录
   * @param param
   */
  logout: () => {
    return getRequest('/login/logout');
  },

  /**
   * 获取验证码
   * @param param
   */
  getCaptcha: () => {
    return getRequest('/login/getCaptcha');
  },

  /**
   * 获取登录信息
   * @param param
   */
  getLoginInfo: () => {
    return getRequest('/login/getLoginInfo');
  },

  /**
   * 获取登录企业信息
   * @param param
   */
  getLoginEnterprise: () => {
    return getRequest('/login/getLoginEnterprise');
  },

  /**
   * 刷新权限
   * @param param
   */
  refresh: () => {
    return getRequest('/login/refresh');
  },
};

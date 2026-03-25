/*
 * @Description:
 * @Author: zhuoda
 * @Date: 2021-08-03
 * @LastEditTime: 2022-08-25
 * @LastEditors: zhuoda
 */
import { defineStore } from 'pinia';
import { getAppConfig } from '/@/config/app-config';
import localStorageKeyConst from '/@/constants/local-storage-key-const';
import { localRead } from '/@/utils/local-util';

let appDefaultConfig = getAppConfig();
let state = { ...appDefaultConfig };

let appConfigStr = localRead(localStorageKeyConst.APP_CONFIG);
let language = appDefaultConfig.language;
if (appConfigStr) {
  try {
    state = JSON.parse(appConfigStr);
    language = state.language;
  } catch (e) {}
}

/**
 * 获取初始化的语言
 */
export const getInitializedLanguage = function () {
  return language;
};

export const useAppConfigStore = defineStore({
  id: 'appConfig',
  state: () => ({
    // 读取config下的默认配置
    ...state,
    userSpaceType:'default'
  }),
  actions: {
    reset() {
      for (const k in appDefaultConfig) {
        this[k] = appDefaultConfig[k];
      }
    },
    setLoginEnterprise(data) {
      this.companyName = data.companyName;
      this.faviconUrl = data.faviconUrl;
      this.logoUrl = data.logoUrl;
      this.websiteName = data.websiteName;
      this.websiteDesc = data.websiteDesc;
    },
    showHelpDoc() {
      this.helpDocFlag = true;
    },
    hideHelpDoc() {
      this.helpDocFlag = false;
    },
  },
});

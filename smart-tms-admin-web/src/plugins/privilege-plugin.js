/*
 * @Author: LiHaiFan
 * @Date: 2021-4-16 21:38:28
 * @LastEditTime: 2022-06-23
 * @LastEditors: zhuoda
 * @Description:
 */
import { useUserStore } from '/@/store/modules/system/user';

const privilege = (value) => {
  // 超级管理员
  if (useUserStore().administratorFlag) {
    return true;
  }
  // 获取功能点权限
  let userPointsList = useUserStore().getPointList;
  if (!userPointsList) {
    return false;
  }
  let pointNameList = userPointsList.map(e => e.permsIdentifier);
  return pointNameList && pointNameList.includes(value);
};

export default {
  install: (app) => {
    app.config.globalProperties.$privilege = privilege;
    app.provide('privilegePlugin', privilege);
  },
};

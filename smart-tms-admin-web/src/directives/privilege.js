/*
 * @Description:
 * @version:
 * @Author: zhuoda
 * @Date: 2021-09-08 21:48:40
 * @LastEditTime: 2022-06-23
 * @LastEditors: zhuoda
 */
// 页面内按钮过滤
import { useUserStore } from '/@/store/modules/system/user';

export function privilegeDirective (el, binding) {
  // 超级管理员
  if (useUserStore().administratorFlag) {
    return true;
  }
  // 获取功能点权限
  let userPointsList = useUserStore().getPointList;
  if (!userPointsList) {
    return false;
  }
  // 如果有权限，删除节点
  let pointNameList = userPointsList.map(e => e.permsIdentifier);
  let privilegeArray = binding.value.split(";")
  let find = privilegeArray.find(e=> pointNameList.includes(e));
  if (!find) {
    el.parentNode.removeChild(el);
  }
  return true;
}

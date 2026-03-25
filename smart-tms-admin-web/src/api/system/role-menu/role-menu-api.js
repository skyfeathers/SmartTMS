/*
 * @Author: zhuoda
 * @Date: 2021-08-28 14:16:46
 * @LastEditTime: 2021-08-28 14:25:36
 * @LastEditors: zhuoda
 * @Description:
 * @FilePath: /smart-admin/src/api/system/role-menu/role-menu-api.ts
 */
import { getRequest, postRequest } from '/@/lib/axios';
export const roleMenuApi = {
  /**
   * @description: 获取角色关联菜单权限
   * @param {*}
   * @return {*}
   */
  getRoleSelectedMenu: (roleId) => {
    return getRequest(`role/menu/getRoleSelectedMenu/${roleId}`);
  },
  /**
   * @description: 更新角色权限
   * @param {*}
   * @return {*}
   */
  updateRoleMenu: (data) => {
    return postRequest('role/menu/updateRoleMenu', data);
  },
};

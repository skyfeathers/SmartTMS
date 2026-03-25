/*
 * @Description: file content
 * @Author: yandy
 * @Date: 2022-07-07 09:51:43
 * @LastEditors:
 * @LastEditTime: 2022-07-07 09:51:43
*/
import { postRequest, getRequest } from '/@/lib/axios';

export const customsBrokerApi = {

  // 新建报关行 @author lihaifan
  create: (param) => {
    return postRequest('/customsBroker/create', param);
  },

  // 删除报关行 @author lihaifan
  delete: (customsBrokerId) => {
    return getRequest(`/customsBroker/delete/${customsBrokerId}`);
  },

  // 查询报关行详情 @author lihaifan
  detail: (customsBrokerId) => {
    return getRequest(`/customsBroker/get/${customsBrokerId}`);
  },

  // 分页查询报关行模块 @author lihaifan
  pageQuery: (param) => {
    return postRequest('/customsBroker/page/query', param);
  },

  // 编辑报关行 @author lihaifan
  update: (param) => {
    return postRequest('/customsBroker/update', param);
  },

};

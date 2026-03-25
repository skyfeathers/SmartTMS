/*
 * @Description: file content
 * @Author: yandy
 * @Date: 2022-07-07 09:51:43
 * @LastEditors:
 * @LastEditTime: 2022-07-07 09:51:43
*/
import { postRequest, getRequest } from '/@/lib/axios';

export const businessCompanyApi = {

  // 新建公司 @author lihaifan
  create: (param) => {
    return postRequest('/company/create', param);
  },

  // 删除公司 @author lihaifan
  delete: (companyId) => {
    return getRequest(`/company/delete/${companyId}`);
  },

  // 查询公司详情 @author lihaifan
  detail: (companyId) => {
    return getRequest(`/company/get/${companyId}`);
  },

  // 分页查询公司模块 @author lihaifan
  pageQuery: (param) => {
    return postRequest('/company/page/query', param);
  },

  // 编辑公司 @author lihaifan
  update: (param) => {
    return postRequest('/company/update', param);
  },

};

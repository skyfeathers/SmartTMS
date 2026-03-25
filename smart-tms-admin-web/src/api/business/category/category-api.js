/*
 * @Description:
 * @Author: zhuoda
 * @Date: 2021-08-12 16:46:21
 * @LastEditTime: 2022-06-16
 * @LastEditors: zhuoda
 */
import { postRequest, getRequest } from '/@/lib/fixed-asset-axios';

export const categoryApi = {
  // 添加类目 by zhuoda
  addCategory: (param) => {
    return postRequest('/category/add', param);
  },
  // GET
  // 删除类目 by zhuoda
  deleteCategoryById: (categoryId) => {
    return getRequest(`/category/del/${categoryId}`);
  },
  // 查询类目层级树 by zhuoda
  queryCategoryTree: (param) => {
    return postRequest('/category/tree', param);
  },
  // 更新类目 by zhuoda
  updateCategory: (param) => {
    return postRequest('/category/update', param);
  },
  // 查询类目详情 by zhuoda
  getCategory: (categoryId) => {
    return getRequest(`/category/${categoryId}`);
  },
};

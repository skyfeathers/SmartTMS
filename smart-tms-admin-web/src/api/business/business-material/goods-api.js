/*
 * @Description: file content
 * @Author: yandy
 * @Date: 2022-07-07 09:51:43
 * @LastEditors:
 * @LastEditTime: 2022-07-07 09:51:43
*/
import { postRequest, getRequest } from '/@/lib/axios';

export const goodsApi = {

  // 新建货物 @author yandy
  create: (param) => {
    return postRequest('/goods/create', param);
  },

  // 删除站点 @author yandy
  delete: (goodsId) => {
    return getRequest(`/goods/delete/${goodsId}`);
  },

  // 查询站点详情 @author yandy
  detail: (goodsId) => {
    return getRequest(`/goods/get/${goodsId}`);
  },

  // 分页查询站点 @author yandy
  pageQuery: (param) => {
    return postRequest('/goods/page/query', param);
  },

  // 编辑站点 @author yandy
  update: (param) => {
    return postRequest('/goods/update', param);
  },

  // 查询站点列表 @author yandy
  list: () => {
    return getRequest('/goods/list');
  },

};

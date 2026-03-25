/*
 * @Description:
 * @Author: zhuoda
 * @Date: 2021-11-05
 * @LastEditTime: 2022-06-23
 * @LastEditors: zhuoda
 */
import { postRequest } from '/@/lib/axios';

export const goodsApi = {
  // 添加商品 by zhuoda
  addGoods: (param) => {
    return postRequest('/goods/add', param);
  },
  // POST /admin/goods/del
  // 删除 by zhuoda
  deleteGoods: (param) => {
    return postRequest('/goods/del', param);
  },
  // POST /admin/goods/query
  // 分页查询 by zhuoda
  queryGoodsList: (param) => {
    return postRequest('/goods/query', param);
  },
  // POST /admin/goods/update
  // 更新商品 by zhuoda
  updateGoods: (param) => {
    return postRequest('/goods/update', param);
  },
};

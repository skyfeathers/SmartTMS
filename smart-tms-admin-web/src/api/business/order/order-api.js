/*
 * @Description:
 * @Author: zhuoda
 * @Date: 2022-07-14
 * @LastEditTime: 2022-07-22
 * @LastEditors: zhuoda
 */
import { download, getRequest, postRequest } from '/@/lib/axios';

export const orderApi = {
  // 取消订单 @author lidoudou
  cancelOrder: (orderId) => {
    return getRequest(`/order/cancel/${orderId}`);
  },
  // 新建订单 @author lidoudou
  createOrder: (param) => {
    return postRequest('/order/create', param);
  },
  // 查询订单详情 @author lidoudou
  getDetail: (orderId) => {
    return getRequest(`/order/get/${orderId}`);
  },
  // 分页查询订单列表 @author lidoudou
  queryOrder: (param) => {
    return postRequest('/order/page/query', param);
  },
  // 编辑订单 @author lidoudou
  updateOrder: (param) => {
    return postRequest('/order/update', param);
  },
  // 查询订单费用明细 by lidoudou
  getCostDetail: (orderId) => {
    return getRequest(`/order/cost/${orderId}`);
  },
  // 编辑订单费用明细 by lidoudou
  updateCost: (params) => {
    return postRequest('order/cost/update', params);
  },
  // 获取提交对账的货主信息 by lidoudou
  selectShipperCheckInfo: (params) => {
    return postRequest('/receive/order/submit/info', params);
  },
  // 导出 by lidodou
  downloadExcel: (fileName, params) => {
    download(fileName, '/order/export', params);
  },
  // 文件导入，解析数据 by lidoudou
  importDoc: (params) => {
    return postRequest(`/order/doc/import`, params);
  },
  // 确认导入 by lidoudou
  importOrder: (params) => {
    return postRequest(`/order/import`, params);
  },
  // 支付完成  by lidoudou
  payComplete: (waybillId) => {
    return getRequest(`/order/pay/complete/${waybillId}`);
  },
  // 订单更新分配状态 by lidoudou
  updateScheduleFlag: (params) => {
    return postRequest('/order/scheduleFlag/update', params);
  },
  // 修改货主 by zhaoxinyang
  updateShipper: (orderId,shipperId) => {
    return getRequest(`/order/shipper/update/${orderId}/${shipperId}`);
  },

  // 修改货物
  updateGoods: (params) => {
    return postRequest('/order/goods/update', params);
  },
};

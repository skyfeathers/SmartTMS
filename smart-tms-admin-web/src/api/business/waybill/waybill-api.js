/*
 * @Description:运单api
 * @Author: zhuoda
 * @Date: 2022-07-14
 * @LastEditTime: 2022-07-28
 * @LastEditors: zhuoda
 */
import { download, getRequest, postRequest } from '/@/lib/axios';

export const waybillApi = {
  // 根据订单id查询运单列表 @author zhuoda
  queryByOrderId: (orderId) => {
    return getRequest(`/waybill/queryByOrderId/${orderId}`);
  },
  // 查询运单列表 @author zhuoda
  query: (param) => {
    return postRequest('/waybill/query', param);
  },
  // 更新铅封号以及箱号 @author zhuoda
  updateLeadSealAndContainerNumber: (param) => {
    return postRequest('/waybill/leadSealAndContainerNumber/update', param);
  },
  // 查询运详情 @author zhuoda
  getDetail: (waybillId) => {
    return getRequest(`/waybill/getDetail/${waybillId}`);
  },
  // 查询运单凭证 @author zhuoda
  getVoucherListByWaybillId: (waybillId) => {
    return getRequest(`/waybill/voucher/getVoucherListByWaybillId/${waybillId}`);
  },
  // 上传凭证 @author zhuoda
  addVoucher: (param) => {
    return postRequest('/waybill/addVoucher', param);
  },
  // 更新运单凭证
  updateVoucher: (param) => {
    return postRequest('/waybill/updateVoucher', param);
  },
  // 删除凭证
  deleteVoucher: (waybillVoucherId) => {
    return getRequest(`/waybill/deleteVoucher/${waybillVoucherId}`);
  },
  // 分配 @author zhuoda
  schedule: (param) => {
    return postRequest('/waybill/schedule', param);
  },
  // 作废 @author zhuoda
  cancel: (waybillId) => {
    return getRequest(`/waybill/cancel/${waybillId}`);
  },
  // 运单费用维护 @author yandy
  costSubmit: (param) => {
    return postRequest('/waybill/cost', param);
  },

  // 修改 @author yandy
  update: (param) => {
    return postRequest('/waybill/update', param);
  },
  // 油卡充值 @author yandy
  oilCardRecharge: (param) => {
    return postRequest('/waybill/oilCardRecharge', param);
  },
  // 发货 @author yandy
  driverGoods: (param) => {
    return postRequest('/waybill/driverGoods', param);
  },
  // 收货 @author yandy
  receiveGoods: (param) => {
    return postRequest('/waybill/receiveGoods', param);
  },
  // 油卡充值详情 @author lidoudou
  getOilCardRechargeDetail: (rechargeApplyId) => {
    return getRequest(`/waybill/oilCard/detail/${rechargeApplyId}`);
  },
  // 更新为运输完成 @author lidoudou
  complete: (params) => {
    return postRequest('/waybill/complete', params);
  },
  // 统计数量 @author lidoudou
  count: (params) => {
    return postRequest('waybill/count', params);
  },
  // 获取油卡可充值最大金额 @author yandy
  getMaxRechargeAmount: (waybillId) => {
    return getRequest(`/waybill/oilCard/maxRechargeAmount/${waybillId}`);
  },
  // 导出 by lidodou
  downloadExcel: (fileName, params) => {
    return download(fileName, '/waybill/export', params);
  },
  // 上传回单 by lidodou
  uploadReceipt: (params) => {
    return postRequest('/waybill/receipt/upload', params);
  },
  // 导出 by lidodou
  uploadTruckOrder: (params) => {
    return postRequest('/waybill/truckOrder/upload', params);
  },
  // 统计费用金额 @author lidoudou
  queryAmountStatistic: (params) => {
    return postRequest('/waybill/statisticAmount', params);
  },
  // 网络货运运单-订单类型-修改 @author zhaoxinyang
  updateOrderType: (waybillId) => {
    return getRequest(`/waybill/update/nft/orderType/${waybillId}`);
  },
  // 分段运输删除司机信息
  splitTransportDelete: (splitTransportId) => {
    return getRequest(`/waybill/splitTransport/delete/${splitTransportId}`);
  },
  // 分段运输分配司机信息
  splitTransportDispatch: (params) => {
    return postRequest('/waybill/splitTransport/dispatch', params);
  },
  // 查询分段运输信息
  getSplitTransport: (waybillId) => {
    return getRequest(`/waybill/getSplitTransport/${waybillId}`);
  },

  // 更新运单运输路线
  waybillPathUpdate: (params) => {
    return postRequest('/waybill/path/update', params);
  },

  // 现金费用结算查询
  costCostSettleQuery: (waybillId) => {
    return getRequest(`/waybill/cashCost/settle/query/${waybillId}`);
  },

  // 油卡费用结算查询
  oilCardCostSettleQuery: (waybillId) => {
    return getRequest(`/waybill/oilCardCost/settle/query/${waybillId}`);
  },
};

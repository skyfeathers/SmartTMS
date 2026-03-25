/*
 * @Description: 付款单
 * @Author: zhuoda
 * @Date: 2022-07-20 21:51:43
 * @LastEditors: zhuoda
 * @LastEditTime: 2022-08-02
 */
import { postRequest, getRequest, download } from '/@/lib/axios';

export const payOrderApi = {

  // 查询付款单详情 @author zhuoda
  getDetail: (payOrderId) => {
    return getRequest(`/payOrder/detail/${payOrderId}`);
  },
  // 提交结算(付款单) @author yandy
  create: (param) => {
    return postRequest('/payOrder/create', param);
  },
  // 查询付款单列表 @author zhuoda
  query: (param) => {
    return postRequest('/payOrder/query', param);
  },
  // 查询付款单应付、已付合计 @author lidoudou
  queryAmountStatistic: (params) => {
    return postRequest('/payOrder/query/amount/statistics', params);
  },
  // 付款 @author zhuoda
  pay: (param) => {
    return postRequest('/payOrder/pay', param);
  },
  // 付款 @author zhuoda
  batchPay: (param) => {
    return postRequest('/payOrder/batch/pay', param);
  },
  // 核销 @author zhuoda
  verification: (param) => {
    return postRequest('/payOrder/verification', param);
  },
  // 核销 @author zhuoda
  batchVerification: (param) => {
    return postRequest('/payOrder/batch/verification', param);
  },
  // 根据运单查询付款单列表
  queryByWaybillId: (waybillId) => {
    return getRequest(`/payOrder/queryByWaybill/${waybillId}`);
  },
  // 导出 by lidoudou
  downloadExcel: (fileName, params) => {
    download(fileName, '/payOrder/export', params);
  },
  // 作废 @author lidoudou
  cancelPayOrder: (param) => {
    return postRequest('/payOrder/cancel', param);
  },
  // 提交nft付款 by lidoudou
  submitNftPay: (payOrderId) => {
    return getRequest(`/payOrder/nft/pay/${payOrderId}`);
  },
  // 提交nft批量付款 by lidoudou
  submitNftBatchPay: (param) => {
    return postRequest('/payOrder/nft/batchPay', param);
  },

  // 油卡充值
  oilCardRecharge: (param) => {
    return postRequest('/payOrder/oilCard/recharge', param);
  },

};

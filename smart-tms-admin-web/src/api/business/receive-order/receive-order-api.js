import { download, getRequest, postRequest } from '/@/lib/axios';

export const receiveOrderApi = {
  // 作废对账单 @author lidoudou
  cancelReceiveOrder: (params) => {
    return postRequest('/receive/order/cancel', params);
  },
  // 确认对账 @author lidoudou
  checkReceiveOrder: (params) => {
    return postRequest('/receive/order/check', params);
  },
  // 分页查询 @author lidoudou
  queryPage: (params) => {
    return postRequest('/receive/order/page/query', params);
  },
  // 应收金额统计 @author zhaoxinyang
  statisticAmount: (params) => {
    return postRequest('/receive/amount/statistic', params);
  },
  // 根据货主查询应收 @author lidoudou
  queryPageByShipper: (params) => {
    return postRequest('/receive/order/shipper/page/query', params);
  },
  // 提交核算 @author lidoudou
  submitReceiveOrder: (params) => {
    return postRequest('/receive/order/submit', params);
  },
  // 提交核算并开票 @author lidoudou
  submitReceiveOrderAndInvoice: (params) => {
    return postRequest('/receive/order/submit/invoice', params);
  },
  // 核销 @author lidoudou
  verificationReceiveOrder: (params) => {
    return postRequest('/receive/order/verification', params);
  },
  // 批量核销 @author lidoudou
  batchVerificationReceiveOrder: (params) => {
    return postRequest('/receive/order/batch/verification', params);
  },
  // 应收款详情 @author lidoudou
  getDetail: (receiveOrderId) => {
    return getRequest(`/receive/order/${receiveOrderId}`);
  },

  // 申请开票 @author lidoudou
  applyInvoice: (params) => {
    return postRequest('/receive/order/invoice/apply', params);
  },
  // 批量开票 @author lidoudou
  batchInvoice: (params) => {
    return postRequest('/receive/order/invoice/batch/apply', params);
  },
  // 作废开票申请 by lidoudou
  cancelInvoice: (params) => {
    return postRequest('/receive/order/invoice/cancel', params);
  },
  // 分页查询申请开票列表 @author lidoudou
  queryInvoicePage: (params) => {
    return postRequest('/receive/order/invoice/page/query', params);
  },
  // 查询首页待收款的列表 by lidoudou
  queryWaitReceiveOrder: (params) => {
    return postRequest('/receive/order/wait/query', params);
  },
  // 导出 @author lidoudou
  exportCheckList: (title, params) => {
    download(`应收核算(${title})列表.xlsx`, '/receive/order/check/export', params);
  },
  // 财务开票 @author lidoudou
  makeInvoiceExportExcel: (title, receiveOrderId) => {
    download(`财务开票(${title})列表.xlsx`, `/receive/order/make/invoice/export/${receiveOrderId}`, {});
  },
  // 应收对账 @author lidoudou
  waybillExportExcel: (title, receiveOrderId) => {
    download(`应收对账(${title})列表.xlsx`, `/receive/order/waybill/export/${receiveOrderId}`, {});
  },
  // 导出核销 @author lidoudou
  exportList: (title, params) => {
    download(`应收核算(${title})列表.xlsx`, '/receive/order/export', params);
  },
  // 上传对账单 @author lidoudou
  uploadBill: (params) => {
    return postRequest('/receive/order/invoice/bill/upload', params);
  },
  // 获取批量核销的金额 by lidoudou
  getBatchVerificationAmount: (params) => {
    return postRequest('/receive/order/batch/verification/amount', params);
  },
  // 根据运单查询付款单列表 @author lidoudou
  queryByWaybillId: (waybillId) => {
    return getRequest(`/receive/order/queryByWaybill/${waybillId}`);
  },
  // 导出 by lidodou
  downloadExcel: (fileName, params) => {
    download(fileName, '/receive/order/wait/export', params);
  },
  // 导出应收核销 by lidoudou
  exportVerificationList: (title, params) => {
    download(`应收核算(${title})列表.xlsx`, '/receive/order/verification/export', params);
  },
  // 应收核销明细导出 @author zhaikk
  exportVerificationItemList: (title, params) => {
    download(`应收核销(${title})列表.xlsx`, '/receive/order/verification/item/export', params);
  },
  // 更新发票 @author zhaikk
  updateInvoice: (params) => {
    return postRequest('/receive/order/invoice/update', params);
  },
  // 作废未核销的数据 @author lidoudou
  cancelVerification: (params) => {
    return postRequest('/receive/order/verification/cancel', params);
  }
};

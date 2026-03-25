import { getRequest, postRequest } from '/@/lib/axios';

// 自有车费用支出
export const carCostPayApi = {

  // 自有车-现金支出-新建 @author zhaoxinyang
  addCashPay: (params) => {
    return postRequest('/car/cost/cashPay/add', params);
  },
  // 自有车-现金支出-列表 @author zhaoxinyang
  queryCarPayList: (waybillId) => {
    return getRequest(`/car/cost/cashPay/list/${waybillId}`);
  },
  // 自有车-现金支出-编辑 @author zhaoxinyang
  updateCashPay: (params) => {
    return postRequest('/car/cost/cashPay/update', params);
  },
  // 自有车-现金支出-审核 @author zhaoxinyang
  auditCashPay: (params) => {
    return postRequest('/car/cost/cashPay/audit', params);
  },
  delCashPay: (cashPayId) => {
    return getRequest(`/car/cost/cashPay/del/${cashPayId}`);
  },
  // 自有车-油费支出-新建 @author zhaoxinyang
  addOilPay: (params) => {
    return postRequest('/car/cost/oilPay/add', params);
  },
  // 自有车-油费支出-列表 @author zhaoxinyang
  queryOilPayList: (waybillId) => {
    return getRequest(`/car/cost/oilPay/list/${waybillId}`);
  },
  // 自有车-油费支出-编辑 @author zhaoxinyang
  updateOilPay: (params) => {
    return postRequest('/car/cost/oilPay/update', params);
  },
  // 自有车-油费支出-审核 @author zhaoxinyang
  auditOilPay: (params) => {
    return postRequest('/car/cost/oilPay/audit', params);
  },
  // 自有车-ETC支出-新建 @author zhaoxinyang
  addEtcPay: (params) => {
    return postRequest('/car/cost/etcPay/add', params);
  },
  // 自有车-ETC支出-审核 @author zhaoxinyang
  auditEtcPay: (params) => {
    return postRequest('/car/cost/etcPay/audit', params);
  },
  // 自有车-ETC支出-列表 @author zhaoxinyang
  queryEtcPayList: (waybillId) => {
    return getRequest(`/car/cost/etcPay/list/${waybillId}`);
  },
  // 自有车-ETC支出-编辑 @author zhaoxinyang
  updateEtcPay: (params) => {
    return postRequest('/car/cost/etcPay/update', params);
  },
  // 自有车-尿素支出-新建 @author zhaoxinyang
  addUreaPay: (params) => {
    return postRequest('/car/cost/ureaPay/add', params);
  },
  // 自有车-尿素支出-审核 @author zhaoxinyang
  auditUreaPay: (params) => {
    return postRequest('/car/cost/ureaPay/audit', params);
  },
  // 自有车-尿素支出-列表 @author zhaoxinyang
  queryUreaPayList: (waybillId) => {
    return getRequest(`/car/cost/ureaPay/list/${waybillId}`);
  },
  // 自有车-尿素支出-编辑 @author zhaoxinyang
  updateUreaPay: (params) => {
    return postRequest('/car/cost/ureaPay/update', params);
  },
  // 自有车-etc支出-删除 @author zhaoxinyang
  delEtcPay: (oilPayId) => {
    return getRequest(`/car/cost/etcPay/del/${oilPayId}`);
  },
  // 自有车-etc支出-删除 @author zhaoxinyang
  delUrea: (ureaPayId) => {
    return getRequest(`/car/cost/ureaPay/del/${ureaPayId}`);
  }
};

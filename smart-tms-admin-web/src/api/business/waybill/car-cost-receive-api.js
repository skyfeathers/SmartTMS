import { getRequest, postRequest } from '/@/lib/axios';

// 自有车费用收入
export const carCostReceiveApi = {
  // 自有车-现金收入-新建 @author zhaoxinyang
  addCashReceive: (params) => {
    return postRequest('/car/cost/cashReceive/add', params);
  },
  // 自有车-现金收入-列表 @author zhaoxinyang
  queryCashReceiveList: (waybillId) => {
    return getRequest(`/car/cost/cashReceive/list/${waybillId}`);
  },
  // 自有车-现金收入-编辑 @author zhaoxinyang
  updateCashReceive: (params) => {
    return postRequest('/car/cost/cashReceive/update', params);
  },
  // 自有车-现金收入-审核 @author zhaoxinyang
  auditCashReceive: (params) => {
    return postRequest('/car/cost/cashReceive/audit', params);
  },
  delCashReceive: (cashReceiveId) => {
    return getRequest(`/car/cost/cashReceive/del/${cashReceiveId}`);
  },
  // 自有车-油卡收入-新建 @author zhaoxinyang
  addOilReceive: (params) => {
    return postRequest('/car/cost/oilCardReceive/add', params);
  },
  // 自有车-油卡收入-列表 @author zhaoxinyang
  queryOilReceiveList: (waybillId) => {
    return getRequest(`/car/cost/oilCardReceive/list/${waybillId}`);
  },
  // 自有车-油卡收入-编辑 @author zhaoxinyang
  updateOilReceive: (params) => {
    return postRequest('/car/cost/oilCardReceive/update', params);
  },
  // 自有车-油卡收入-审核 @author zhaoxinyang
  auditOilReceive: (params) => {
    return postRequest('/car/cost/oilCardReceive/audit', params);
  },
  // 自有车-油卡收入-删除 @author zhaoxinyang
  delOilCardReceive: (oilCardReceiveId) => {
    return getRequest(`/car/cost/oilCardReceive/del/${oilCardReceiveId}`);
  },
  // 自有车-油卡支出-删除 @author zhaoxinyang
  delOilPayReceive: (oilPayId) => {
    return getRequest(`/car/cost/oilPay/del/${oilPayId}`);
  }
};

import { getRequest, postRequest } from '/@/lib/axios';

export const carCostTabulationApi = {
  page: (params) => {
    return postRequest('/car/cost/tabulation/page', params);
  },
  audit: (params) => {
    return postRequest('/car/cost/tabulation/audit', params);
  },
  auditBatch: (params) => {
    return postRequest('/car/cost/tabulation/batch/audit', params);
  },
  relate: (params) => {
    return postRequest(`/car/cost/tabulation/batch/relate/waybill`, params);
  },
  cancelRelate: (params) => {
    return postRequest(`/car/cost/tabulation/batch/cancel/relate/waybill`, params);
  },
  delete: (tabulationId) => {
    return getRequest(`/car/cost/tabulation/del/${tabulationId}`);
  },
  detail: (moduleId,moduleType) => {
    return getRequest(`/car/cost/tabulation/detail/${moduleId}/${moduleType}`);
  },
  // 运单在途费用
  listByWaybillId: (waybillId) => {
    return getRequest(`/car/cost/tabulation/list/${waybillId}`);
  },
};


import { download, getRequest, postRequest } from '/@/lib/axios';

export const waybillOilCardRechargeApi = {
  // 油卡充值列表 @author lidodou
  query: (params) => {
    return postRequest('/waybill/oilCard/query', params);
  },
  // 油卡核销 @author lidoudou
  verificationOilCardRecharge: (params) => {
    return postRequest('/waybill/oilCard/verification', params);
  },
  // 批量油卡核销 @author yandy
  batchVerificationOilCardRecharge: (params) => {
    return postRequest('/waybill/oilCard/batch/verification', params);
  },
  // 作废 @author lidoudou
  cancelOilCardRecharge: (params) => {
    return postRequest('/waybill/oilCard/cancel', params);
  },
  // 导出  @author lidoudou
  downloadExcel: (params) => {
    download(`油卡充值申请.xlsx`, '/waybill/oilCard/export', params);
  },

};

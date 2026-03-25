import { postRequest, getRequest } from '/@/lib/axios';

export const capitalFlowApi = {
  // 分页查询应收的资金流水 @author lidoudou
  queryReceiveOrderFlow: (param) => {
    return postRequest('/capital/flow/receive/page/query', param);
  },

  // 分页查询应付的资金流水 @author lidoudou
  queryPayOrderFlow: (param) => {
    return postRequest('/capital/flow/pay/page/query', param);
  },

  // 统计资金流水 by lidoudou
  statistic: (param) => {
    return postRequest('/capital/flow/statistic', param);
  }

};

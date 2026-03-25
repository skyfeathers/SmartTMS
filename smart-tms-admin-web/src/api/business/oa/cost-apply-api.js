import { postRequest, getRequest } from '/@/lib/axios';

export const costApplyApi = {

  // 分页查询 @author lidoudou
  queryPage: params => {
    return postRequest('/oa/cost/apply/queryPage', params);
  },
  // 添加 @author lidoudou
  addCostApply: params => {
    return postRequest('/oa/cost/apply/add', params);
  },
  // 详情 @author lidoudou
  getDetail: applyId => {
    return getRequest(`/oa/cost/apply/getDetail/${applyId}`);
  },
  // 审核通过 @author lidoudou
  reject: (param) => {
    return postRequest('/oa/cost/apply/reject', param);
  },
  // 审核驳回 @author lidoudou
  pass: (param) => {
    return postRequest('/oa/cost/apply/pass', param);
  },
};

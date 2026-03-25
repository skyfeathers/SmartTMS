import { getRequest, postRequest } from '/@/lib/axios';

export const carCostApi = {
  // 自有车-基本信息-编辑 @author zhaoxinyang
  updateBasicInfo: (params) => {
    return postRequest('/car/cost/basicInfo/update', params);
  },
  // 自有车-基本信息-确认状态更新 @author zhaoxinyang
  updateBasicConfirmFlag: (basicInfoId, confirmFlag) => {
    return getRequest(`/car/cost/basicInfo/confirm/update/${basicInfoId}/${confirmFlag}`);
  },
  // ------------------------- 历史接口 -------------------------
  // 新建 @author yandy
  create: (params) => {
    return postRequest('/car/cost/save', params);
  },
  // 详情 @author yandy
  getDetail: (carCostId) => {
    return getRequest(`/car/cost/detail/${carCostId}`);
  },
  // 分页查询 @author yandy
  queryPage: (params) => {
    return postRequest('/car/cost/query', params);
  },
  // 编辑 @author yandy
  update: (params) => {
    return postRequest('/car/cost/update', params);
  },
  // 作废
  cancel: (carCostId) => {
    return getRequest(`/car/cost/delete/${carCostId}`);
  },
};


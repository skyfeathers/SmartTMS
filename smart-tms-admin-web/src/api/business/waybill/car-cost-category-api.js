import { getRequest, postRequest } from '/@/lib/axios';

export const carCostCategoryApi = {
  // 车辆费用-删除 @author yandy
  delete: (categoryId) => {
    return getRequest(`/car/cost/category/delete/${categoryId}`);
  },
  // 车辆费用-分页查询 @author yandy
  query: (params) => {
    return postRequest('/car/cost/category/query/page', params);
  },
  // 车辆费用-保存 @author yandy
  save: (params) => {
    return postRequest('/car/cost/category/save', params);
  },
  // 车辆费用-更新 @author yandy
  update: (params) => {
    return postRequest('/car/cost/category/update', params);
  },
  // 费用科目-查询 @author yandy
  queryByCostType: (costType) => {
    return getRequest(`/car/cost/category/query/${costType}`);
  },
  // 费用科目-查询全部 @author lidoudou
  queryList: () => {
    return getRequest('/car/cost/category/queryList');
  }
};

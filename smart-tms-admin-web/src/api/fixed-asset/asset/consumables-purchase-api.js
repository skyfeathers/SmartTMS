import { postRequest, getRequest } from '/@/lib/fixed-asset-axios';

export const consumablesPurchaseApi = {

  /**
   * 分页查询  @author  lidoudou
   */
  queryPage: (param) => {
    return postRequest('/consumables/purchase/queryPage', param);
  },

  /**
   * 增加  @author  lidoudou
   */
  add: (param) => {
    return postRequest('/consumables/purchase/add', param);
  },

  /**
   * 详情  @author  lidoudou
   */
  getDetail: (purchaseId) => {
    return getRequest(`/consumables/purchase/detail/${purchaseId}`);
  },

};

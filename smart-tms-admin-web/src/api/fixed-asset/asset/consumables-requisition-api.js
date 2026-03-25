import { postRequest, getRequest } from '/@/lib/fixed-asset-axios';

export const consumablesRequisitionApi = {

  /**
   * 分页查询  @author  lidoudou
   */
  queryPage: (param) => {
    return postRequest('/consumables/requisition/queryPage', param);
  },

  /**
   * 增加  @author  lidoudou
   */
  add: (param) => {
    return postRequest('/consumables/requisition/add', param);
  },

  /**
   * 详情  @author  lidoudou
   */
  getDetail: (requisitionId) => {
    return getRequest(`/consumables/requisition/detail/${requisitionId}`);
  },

};

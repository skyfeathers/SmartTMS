import { postRequest } from '/@/lib/fixed-asset-axios';

export const stockRecordApi = {

  /**
   * 分页查询  @author  lidoudou
   */
  queryPage: (param) => {
    return postRequest('/consumables/stock/record/queryPage', param);
  },

};

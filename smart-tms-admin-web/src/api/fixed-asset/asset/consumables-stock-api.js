import { postRequest, getRequest } from '/@/lib/fixed-asset-axios';

export const consumablesStockApi = {

  /**
   * 分页查询  @author  lidoudou
   */
  queryPage: (param) => {
    return postRequest('/consumables/stock/queryPage', param);
  },

  /**
   * 增加  @author  lidoudou
   */
  add: (param) => {
    return postRequest('/consumables/stock/add', param);
  },

  /**
   * 修改  @author  lidoudou
   */
  update: (param) => {
    return postRequest('/consumables/stock/update', param);
  },
  /**
   * 获取详情
   * @param consumablesId
   * @returns {Promise<AxiosResponse<any>>}
   */
  getDetail: (consumablesId) => {
    return getRequest(`/consumables/stock/detail/${consumablesId}`);
  }
};

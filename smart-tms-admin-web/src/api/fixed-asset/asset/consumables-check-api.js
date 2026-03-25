import { postRequest, getRequest } from '/@/lib/fixed-asset-axios';

export const consumablesCheckApi = {

  /**
   * 分页查询  @author  lidoudou
   */
  queryPage: (param) => {
    return postRequest('/consumables/check/queryPage', param);
  },

  /**
   * 增加  @author  lidoudou
   */
  add: (param) => {
    return postRequest('/consumables/check/add', param);
  },
  /**
   * 获取详情
   * @param checkId
   * @returns {Promise<AxiosResponse<any>>}
   */
  getDetail: checkId => {
    return getRequest(`/consumables/check/detail/${checkId}`);
  },
  /**
   * 盘点
   * @param transferId
   * @returns {Promise<AxiosResponse<any>>}
   */
  check: param => {
    return postRequest('/consumables/check/update', param);
  },
  /**
   * 完成盘点
   *
   * @param transferId
   * @returns {Promise<AxiosResponse<any>>}
   */
  completeCheck: params => {
    return postRequest('/consumables/check/complete', params);
  }
};

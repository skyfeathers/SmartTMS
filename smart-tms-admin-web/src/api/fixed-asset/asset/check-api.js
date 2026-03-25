/**
 * 固定资产 - 盘点 api 封装
 *
 * @Author:    lidoudou
 * @Date:      2023-03-24 14:23:14
 * @Copyright  1024创新实验室
 */
import { postRequest, getRequest } from '/@/lib/fixed-asset-axios';

export const assetCheckApi = {

  /**
   * 分页查询  @author  lidoudou
   */
  queryPage: (param) => {
    return postRequest('/asset/check/queryPage', param);
  },

  /**
   * 增加  @author  lidoudou
   */
  add: (param) => {
    return postRequest('/asset/check/add', param);
  },
  /**
   * 获取详情
   * @param transferId
   * @returns {Promise<AxiosResponse<any>>}
   */
  getDetail: transferId => {
    return getRequest(`/asset/check/detail/${transferId}`);
  },
  /**
   * 盘点
   * @param transferId
   * @returns {Promise<AxiosResponse<any>>}
   */
  check: param => {
    return postRequest('/asset/check/update', param);
  },
  /**
   * 完成盘点
   *
   * @param transferId
   * @returns {Promise<AxiosResponse<any>>}
   */
  completeCheck: params => {
    return postRequest('/asset/check/complete', params);
  }
};

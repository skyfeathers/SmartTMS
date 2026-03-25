/**
 * 固定资产 - 转移 api 封装
 *
 * @Author:    lidoudou
 * @Date:      2023-03-21 10:48:14
 * @Copyright  1024创新实验室
 */
import { postRequest, getRequest } from '/@/lib/fixed-asset-axios';

export const assetTransferApi = {

  /**
   * 分页查询  @author  lidoudou
   */
  queryPage: (param) => {
    return postRequest('/asset/transfer/queryPage', param);
  },

  /**
   * 增加  @author  lidoudou
   */
  add: (param) => {
    return postRequest('/asset/transfer/add', param);
  },
  /**
   * 通过转移申请
   * @param transferId
   * @returns {Promise<AxiosResponse<any>>}
   */
  complete: transferId => {
    return getRequest(`/asset/transfer/complete/${transferId}`);
  },
  /**
   * 驳回转移申请
   * @param transferId
   * @returns {Promise<AxiosResponse<any>>}
   */
  reject: transferId => {
    return getRequest(`/asset/transfer/reject/${transferId}`);
  },
  /**
   * 获取详情
   * @param transferId
   * @returns {Promise<AxiosResponse<any>>}
   */
  getDetail: transferId => {
    return getRequest(`/asset/transfer/detail/${transferId}`);
  }
};

/**
 * 固定资产 - 调拨 api 封装
 *
 * @Author:    lidoudou
 * @Date:      2023-03-21 10:48:14
 * @Copyright  1024创新实验室
 */
import { postRequest, getRequest } from '/@/lib/fixed-asset-axios';

export const assetAllocationApi = {

  /**
   * 分页查询  @author  lidoudou
   */
  queryPage: (param) => {
    return postRequest('/asset/allocation/queryPage', param);
  },

  /**
   * 增加  @author  lidoudou
   */
  add: (param) => {
    return postRequest('/asset/allocation/add', param);
  },
  /**
   * 通过领用申请
   * @param allocationId
   * @returns {Promise<AxiosResponse<any>>}
   */
  complete: allocationId => {
    return getRequest(`/asset/allocation/complete/${allocationId}`);
  },
  /**
   * 驳回领用申请
   * @param allocationId
   * @returns {Promise<AxiosResponse<any>>}
   */
  reject: allocationId => {
    return getRequest(`/asset/allocation/reject/${allocationId}`);
  },
  /**
   * 获取详情
   * @param transferId
   * @returns {Promise<AxiosResponse<any>>}
   */
  getDetail: allocationId => {
    return getRequest(`/asset/allocation/detail/${allocationId}`);
  }
};

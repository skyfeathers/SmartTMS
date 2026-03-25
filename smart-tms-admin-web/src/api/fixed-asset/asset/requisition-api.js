/**
 * 固定资产 - 领用 api 封装
 *
 * @Author:    lidoudou
 * @Date:      2023-03-21 10:48:14
 * @Copyright  1024创新实验室
 */
import { postRequest, getRequest } from '/@/lib/fixed-asset-axios';

export const assetRequisitionApi = {

  /**
   * 分页查询  @author  lidoudou
   */
  queryPage: (param) => {
    return postRequest('/asset/requisition/queryPage', param);
  },

  /**
   * 增加  @author  lidoudou
   */
  add: (param) => {
    return postRequest('/asset/requisition/add', param);
  },
  /**
   * 通过领用申请
   * @param requisitionId
   * @returns {Promise<AxiosResponse<any>>}
   */
  completeRequisition: requisitionId => {
    return getRequest(`/asset/requisition/complete/${requisitionId}`);
  },
  /**
   * 驳回领用申请
   * @param requisitionId
   * @returns {Promise<AxiosResponse<any>>}
   */
  rejectRequisition: requisitionId => {
    return getRequest(`/asset/requisition/reject/${requisitionId}`);
  },
  /**
   * 获取详情
   * @param requisitionId
   * @returns {Promise<AxiosResponse<any>>}
   */
  getDetail: requisitionId => {
    return getRequest(`/asset/requisitionrevert/detail/${requisitionId}`);
  }
};

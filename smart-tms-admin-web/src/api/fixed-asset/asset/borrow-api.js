/**
 * 固定资产 - 借用 api 封装
 *
 * @Author:    lidoudou
 * @Date:      2023-03-21 10:48:14
 * @Copyright  1024创新实验室
 */
import { postRequest, getRequest } from '/@/lib/fixed-asset-axios';

export const assetBorrowApi = {

  /**
   * 分页查询  @author  lidoudou
   */
  queryPage: (param) => {
    return postRequest('/asset/borrow/queryPage', param);
  },

  /**
   * 增加  @author  lidoudou
   */
  add: (param) => {
    return postRequest('/asset/borrow/add', param);
  },
  /**
   * 通过领用申请
   * @param borrowBackId
   * @returns {Promise<AxiosResponse<any>>}
   */
  completeBorrow: borrowBackId => {
    return getRequest(`/asset/borrow/complete/${borrowBackId}`);
  },
  /**
   * 驳回领用申请
   * @param borrowBackId
   * @returns {Promise<AxiosResponse<any>>}
   */
  rejectBorrow: borrowBackId => {
    return getRequest(`/asset/borrow/reject/${borrowBackId}`);
  },
  /**
   * 获取详情
   * @param borrowBackId
   * @returns {Promise<AxiosResponse<any>>}
   */
  getDetail: borrowBackId => {
    return getRequest(`/asset/borrowback/detail/${borrowBackId}`);
  }
};

/**
 * 固定资产 api 封装
 *
 * @Author:    lidoudou
 * @Date:      2023-03-15 14:15:14
 * @Copyright  1024创新实验室
 */
import { postRequest, getRequest } from '/@/lib/fixed-asset-axios';

export const assetApi = {

  /**
   * 分页查询  @author  lidoudou
   */
  queryPage: (param) => {
    return postRequest('/asset/queryPage', param);
  },

  /**
   * 增加  @author  lidoudou
   */
  add: (param) => {
    return postRequest('/asset/add', param);
  },

  /**
   * 修改  @author  lidoudou
   */
  update: (param) => {
    return postRequest('/asset/update', param);
  },

  /**
   * 删除  @author  lidoudou
   */
  delete: (id) => {
    return getRequest(`/asset/delete/${id}`);
  },

  /**
   * 批量删除  @author  lidoudou
   */
  batchDelete: (idList) => {
    return postRequest('/asset/batchDelete', idList);
  },

  /**
   * 获取详情  @author  lidoudou
   */
  getDetail: (assetId) => {
    return getRequest(`/asset/detail/${assetId}`);
  },

};

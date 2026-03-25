/**
 * 固定资产 - 退回 api 封装
 *
 * @Author:    lidoudou
 * @Date:      2023-03-21 10:48:14
 * @Copyright  1024创新实验室
 */
import { postRequest, getRequest } from '/@/lib/fixed-asset-axios';

export const assetRevertApi = {

  /**
   * 分页查询  @author  lidoudou
   */
  queryPage: (param) => {
    return postRequest('/asset/revert/queryPage', param);
  },

  /**
   * 增加  @author  lidoudou
   */
  add: (param) => {
    return postRequest('/asset/revert/add', param);
  }
};

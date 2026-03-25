/**
 * 固定资产 - 采购 api 封装
 *
 * @Author:    lidoudou
 * @Date:      2023-03-20 14:15:14
 * @Copyright  1024创新实验室
 */
import { postRequest, getRequest } from '/@/lib/fixed-asset-axios';

export const assetPurchaseApi = {

  /**
   * 分页查询  @author  lidoudou
   */
  queryPage: (param) => {
    return postRequest('/asset/purchase/queryPage', param);
  },

  /**
   * 增加  @author  lidoudou
   */
  add: (param) => {
    return postRequest('/asset/purchase/add', param);
  },
};

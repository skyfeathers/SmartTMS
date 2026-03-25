/**
 * 固定资产 - 报表
 *
 * @Author:    lidoudou
 * @Date:      2023-03-29 14:48:14
 * @Copyright  1024创新实验室
 */
import { postRequest } from '/@/lib/fixed-asset-axios';

export const reportApi = {

  /**
   * 分页查询  @author  lidoudou
   */
  queryPage: (param) => {
    return postRequest('/report/depreciation/queryPage', param);
  },
};

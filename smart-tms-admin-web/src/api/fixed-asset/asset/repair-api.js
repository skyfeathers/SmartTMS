/**
 * 固定资产-维修登记 api 封装
 *
 * @Author:    卓大
 * @Date:      2023-03-23 15:01:51
 * @Copyright  1024创新实验室 （ https://1024lab.net ）
 */
import { postRequest, getRequest } from '/@/lib/fixed-asset-axios';

export const repairApi = {
  /**
   * 分页查询  @author  卓大
   */
  queryPage: (param) => {
    return postRequest('/repair/queryPage', param);
  },

  /**
   * 增加  @author  卓大
   */
  add: (param) => {
    return postRequest('/repair/add', param);
  },


  /**
   * 审核通过  @author  卓大
   */
  pass: (param) => {
    return postRequest('/repair/pass', param);
  },

  /**
   * 审核拒绝  @author  卓大
   */
  reject: (param) => {
    return postRequest('/repair/reject', param);
  },

    /**
   * 完成维修  @author  卓大
   */
    finish: (param) => {
      return postRequest('/repair/finish', param);
    },

    /**
   * 详情  @author  卓大
   */
    getDetail: (repairId) => {
      return getRequest(`/repair/getDetail/${repairId}`);
    },
};

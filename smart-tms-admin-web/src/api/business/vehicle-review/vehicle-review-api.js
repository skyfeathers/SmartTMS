/*
 * @Description: file content
 * @Author: baijuhui
 * @Date: 2023-10-18 14:51:43
*/
import { postRequest, getRequest } from '/@/lib/axios';

export const vehicleReviewApi = {

  // 新建审车信息 @author zhaoxinyang
  create: (param) => {
    return postRequest('/review/add', param);
  },

  // 审车信息删除 @author zhaoxinyang
  delete: (reviewId) => {
    return getRequest(`/review/delete/${reviewId}`);
  },

  // 分页查询审车信息 @author zhaoxinyang
  queryPage: (param) => {
    return postRequest('/review/page/query', param);
  },

  // 编辑审车信息 @author zhaoxinyang
  update: (param) => {
    return postRequest('/review/update', param);
  },

};

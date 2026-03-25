/*
 * @Description: file content
 * @Author: yandy
 * @Date: 2022-07-21 22:51:46
 * @LastEditors:
 * @LastEditTime: 2022-07-21 22:51:46
 */
import { postRequest,getRequest } from '/@/lib/axios';

export const dataTracerApi = {

  //分页查询业务操作日志 - by listen
  queryDataTracerLogList: (param) => {
    return postRequest('/support/data/tracer/log/query', param);
  },

  //获取extData - @author 胡克
  extData: (dataTracerId) => {
    return getRequest(`/support/data/tracer/extData/${dataTracerId}`);
  },
};

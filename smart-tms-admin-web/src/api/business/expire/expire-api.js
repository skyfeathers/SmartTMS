/*
 * @Description: file content
 * @Author: yandy
 * @Date: 2022-07-07 09:51:43
 * @LastEditors:
 * @LastEditTime: 2022-07-07 09:51:43
*/
import { postRequest, getRequest } from '/@/lib/axios';

export const expireApi = {

  // 到期提醒时间设置-查询 @listen
  reminderTimeQuery: () => {
    return getRequest('/expiredCertificate/reminderTime/query');
  },

  // 分页查询 @listen
  pageQuery: (param) => {
    return postRequest('/expiredCertificate/query', param);
  },

  // 到期提醒时间设置-更新 @listen
  reminderTimeUpdate: (param) => {
    return postRequest('/expiredCertificate/reminderTime/update', param);
  },

};

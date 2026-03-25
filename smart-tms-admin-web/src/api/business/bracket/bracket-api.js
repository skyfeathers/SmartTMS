/*
 * @Author: lp
 * @Date: 2022-07-15 09:19:30
 * @LastEditors: lp
 * @LastEditTime: 2022-07-17 16:27:28
 * @Description: 挂车管理
 * @FilePath: \nft-admin-web\src\api\business\bracket\bracket-api.js
 */
import { download, getRequest, postRequest } from '/@/lib/axios';

export const bracketApi = {
  // 分页查询 @author lidoudou
  queryBracketList: (param) => {
    return postRequest('/bracket/page/query', param);
  },
  // 挂车详情 @author lidoudou
  bracketDetail: (bracketId) => {
    return getRequest(`/bracket/detail/${bracketId}`);
  },
  // 挂车删除 @author lidoudou
  bracketBatchDelete: (params) => {
    return postRequest('/bracket/batch/delete', params);
  },
  // 新建 @author lidoudou
  bracketSave: (param) => {
    return postRequest('/bracket/save', param);
  },
  // 编辑 @author lidoudou
  bracketUpdate: (param) => {
    return postRequest('/bracket/update', param);
  },
  // 批量审核 by lidoudou
  batchAudit: (param) => {
    return postRequest('/bracket/batch/audit', param);
  },

  // 查询不分页的列表 by lidoudou
  queryList: (param) => {
    return postRequest('/bracket/query/list', param);
  },
  //更新负责人 by lidoudou
  batchUpdateManager: (param) => {
    return postRequest('/bracket/manager/update', param);
  },
  businessModeUpdate: (param) => {
    return postRequest('/bracket/businessMode/update', param);
  },
  // 导出 by lidodou
  downloadExcel: (fileName, params) => {
    download(fileName, '/bracket/export', params);
  },
};

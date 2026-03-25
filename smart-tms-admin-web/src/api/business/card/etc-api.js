import { getRequest, postRequest } from '/@/lib/axios';

export const etcApi = {
  // 批量删除 by lidoudou
  batchDeleteEtc: (param) => {
    return postRequest('/etc/update/batch/delete', param);
  },
  // ETC删除 @author lidoudou
  deleteEtc: (etcId) => {
    return getRequest(`/etc/delete/${etcId}`);
  },
  // ETC详情 @author lidoudou
  getEtcDetail: (etcId) => {
    return getRequest(`/etc/detail/${etcId}`);
  },
  // 分页查询ETC交易记录 @author lidoudou
  queryEtcRecord: (param) => {
    return postRequest('/etc/balance/record/page/query', param);
  },
  // 分页查询ETC @author lidoudou
  queryEtc: (param) => {
    return postRequest('/etc/page/query', param);
  },
  // 新建ETC @author lidoudou
  saveEtc: (param) => {
    return postRequest('/etc/save', param);
  },
  // 编辑ETC @author lidoudou
  updateEtc: (param) => {
    return postRequest('/etc/update', param);
  },
  // 导入etc  by yandy
  importEtc: (params) => {
    return postRequest('/etc/excel/import', params);
  }

};

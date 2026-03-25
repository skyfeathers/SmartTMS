import {getRequest, postRequest} from '/@/lib/axios';

export const contractApi = {
  // 分页查询合同模块 @author lihaifan
  queryContract: (param) => {
    return postRequest('/contract/page/query', param);
  },
  // 新建合同 @author lihaifan
  createContract: (param) => {
    return postRequest('/contract/create', param);
  },
  // 新建合同 @author lihaifan
  updateContract: (param) => {
    return postRequest('/contract/update', param);
  },
  // 批量更新状态 @author lihaifan
  batchUpdateStatus: (param) => {
    return postRequest('/contract/batchUpdateStatus', param);
  },
  // 获取线上合同预览url @author shanyi
  getExecuteUrl: (contractId, signedFlag) => {
    return getRequest(`/contract/getExecuteUrl/${contractId}/${signedFlag}`);
  },
  // 下载合同 @author shanyi
  downloadContract: (contractId) => {
    return getRequest(`/contract/downloadContract/${contractId}`);
  },
  // 查询线上合同操作记录 @author shanyi
  getOnlineRecords: (contractId) => {
    return getRequest(`/contract/getOnlineRecords/${contractId}`);
  },
  // 合同撤销 @author shanyi
  revokeContract: (contractId) => {
    return getRequest(`/contract/revoke/${contractId}`);
  },
}

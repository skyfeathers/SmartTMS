/*
 * @Description:
 * @version:
 * @Author: lidoudou
 * @Date: 2022-07-05 16:41:39
 * @LastEditors: lidoudou
 * @LastEditTime: 2022-07-05 16:46:10
 */
import { getRequest, postRequest, download } from '/@/lib/axios';

export const shipperApi = {
  // 更新客服/调度负责人 @author lidoudou
  updatePrincipal: (param) => {
    return postRequest('/shipper/principal/update', param);
  },

  // 删除货主 @author yandy
  deleteShipper: (params) => {
    return postRequest('/shipper/batch/delete', params);
  },

  // 货主详情 @author yandy
  getShipper: (shipperId) => {
    return getRequest(`/shipper/detail/${shipperId}`);
  },

  // 所有货主 @author yandy
  queryList: () => {
    return postRequest('/shipper/list');
  },

  // 更新业务负责人 @author lidoudou
  updateManager: (param) => {
    return postRequest('/shipper/manager/update', param);
  },

  // 分页查询货主 @author yandy
  queryShipper: (param) => {
    return postRequest('/shipper/page/query', param);
  },

  // 添加货主 @author yandy
  saveShipper: (param) => {
    return postRequest('/shipper/save', param);
  },

  // 货主基本信息详情 @author yandy
  getSimpleDetail: (shipperId) => {
    return getRequest(`/shipper/simpleDetail/${shipperId}`);
  },

  // 更新货主 @author yandy
  updateShipper: (param) => {
    return postRequest('/shipper/update', param);
  },

  // 查询货主的发票信息 @author lidoudou
  queryInvoiceList: (shipperId) => {
    return getRequest(`/shipper/invoice/${shipperId}`);
  },
  // 批量审核 @author lidoudou
  batchAudit: (param) => {
    return postRequest('/shipper/batch/audit', param);
  },

  // 查询默认的联系人信息 by lidoudou
  selectDefaultContact: (shipperId) => {
    return getRequest(`/shipper/default/contact/${shipperId}`);
  },

  // 保存开票信息 by lidoudou
  saveInvoice: (params) => {
    return postRequest('/shipper/invoice/save', params);
  },

  // 更新校验状态 @author lidoudou
  batchUpdateVerifyFlag: (params) => {
    return postRequest('/shipper/principal/batch/update/verify', params);
  },

  // 查询是否企业是否有重名的货主 true存在 false不存在 by lidoudou
  queryByName: (params) => {
    return postRequest('/shipper/query/enterprise/name', params);
  },

  downloadExcel: (params) => {
    download('货主列表.xlsx','/shipper/export', params);
  }
};

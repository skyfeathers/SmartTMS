import { getRequest, postRequest, download } from '/@/lib/axios';

export const oilApi = {
  // 批量删除 by lidoudou
  batchDeleteOil: (param) => {
    return postRequest('/oil/card/update/batch/delete', param);
  },
  // 油卡删除 @author lidoudou
  deleteOil: (oilId) => {
    return getRequest(`/oil/card/delete/${oilId}`);
  },
  // 油卡详情 @author lidoudou
  getOilDetail: (oilId) => {
    return getRequest(`/oil/card/detail/${oilId}`);
  },
  // 分页查询油卡交易记录 @author lidoudou
  queryOilRecord: (param) => {
    return postRequest('/oil/card/balance/record/page/query', param);
  },
  // 查询不分页的油卡列表 @author lidoudou
  queryList: (param) => {
    return postRequest('/oil/card/query/list', param);
  },
  // 分页查询油卡 @author lidoudou
  queryOil: (param) => {
    return postRequest('/oil/card/page/query', param);
  },
  cardSummary: (param) => {
    return postRequest('/oil/card/summary', param);
  },

  // 新建油卡 @author lidoudou
  saveOil: (param) => {
    return postRequest('/oil/card/save', param);
  },
  // 手动消耗
  expendOil: (param) => {
    return postRequest('/oil/card/expend', param);
  },
  // 作废油卡
  cancelOil: (param) => {
    return postRequest('/oil/card/cancel', param);
  },
  // 编辑油卡 @author lidoudou
  updateOil: (param) => {
    return postRequest('/oil/card/update', param);
  },
  // 充值油卡
  rechargeOil: (param) => {
    return postRequest('/oil/card/recharge', param);
  },
  // 副卡圈回 by lidoudou
  circumflexOil: (param) => {
    return postRequest('/oil/card/circumflex', param);
  },
  // 变更司机/车牌信息
  changeUse: (param) => {
    return postRequest('/oil/card/use/change', param);
  },
  // 导出交易流水 by lidodou
  downloadExcel: (params) => {
    download(`油卡余额交易记录.xlsx`, '/oil/card/balance/export', params);
  },
  // 分配副卡 by lidoudou
  distributeSlaveCard: (params) => {
    return postRequest('/oil/slave/card/distribute', params);
  },
  // 导出主卡
  downloadMasterExcel: (params) => {
    download(`主卡列表.xlsx`, '/oil/card/master/export', params);
  },
  // 导入油卡副卡  by lidoudou
  importSlaveCard: (params) => {
    return postRequest('/oil/card/slave/excel/import', params);
  },
  // 根据车辆ID查询绑定油卡列表 @author lidoudou
  queryOilCardByVehicleId: (vehicleId) => {
    return getRequest(`/oil/card/vehicle/query/list/${vehicleId}`);
  },
  // 编辑计划充值金额 by lidoudou
  updatePreRechargeAmount: (params) => {
    return postRequest('/oil/card/preRechargeAmount/update', params);
  },
  // 设置计划充值金额 by lidoudou
  setPreRechargeAmount: (params) => {
    return postRequest('/oil/card/preRechargeAmount/set', params);
  },
  // 根据计划充值金额去充值 by lidoudou
  rechargeByPreRechargeAmount: (params) => {
    return postRequest('/oil/card/preRechargeAmount/recharge', params);
  }
};

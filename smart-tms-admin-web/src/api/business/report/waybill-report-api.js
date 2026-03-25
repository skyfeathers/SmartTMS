import { download, getRequest, postRequest } from '/@/lib/axios';

export const waybillReportApi = {
  // 查询运单利润表 @author lidoudou
  query: (params) => {
    return postRequest('/waybill/profit/query', params);
  },
  // 导出  @author lidoudou
  downloadExcel: (params) => {
    download(`运单利润表.xlsx`, '/waybill/profit/export', params);
  },
  // 查询货主毛利 @author lidoudou
  queryWaybillShipperProfitList: (params) => {
    return postRequest('/waybill/shipper/profit/query', params);
  },
  queryWaybillShipperProfitSummary: (params) => {
    return postRequest('/waybill/shipper/profit/summary', params);
  },
  // 查询货主毛利 @author lidoudou
  downloadWaybillShipperProfitExcel: (params) => {
    download('客户毛利表.xlsx', '/waybill/shipper/profit/export', params);
  },
  // 查询客服收入成本 by lidoudou
  queryAmountByCustomerService: (params) => {
    return postRequest('/waybill/customerService/amount/query', params);
  },
  // 导出客服收入成本 by lidoudou
  downloadAmountByCustomerService: (params) => {
    download('客服收入成本明细表.xlsx', '/waybill/customerService/amount/export', params);
  },
  // 查询销售收入成本 by lidoudou
  queryAmountBySale: (params) => {
    return postRequest('/waybill/sale/amount/query', params);
  },
  // 导出销售收入成本 by lidoudou
  downloadAmountBySale: (params) => {
    download('销售收入成本明细表.xlsx', '/waybill/sale/amount/export', params);
  },
  // 查询外调车油卡比例 by lidoudou
  queryOilCardAmountBySale: (params) => {
    return postRequest('/waybill/oilCard/rate/query', params);
  },
  // 导出查询外调车油卡比例 by lidoudou
  downloadOilCardAmountBySale: (params) => {
    download('外调车油卡比例表.xlsx', '/waybill/oilCard/rate/export', params);
  },
  // 车辆运单统计 @author lidoudou
  queryWaybillVehicleCount: (params) => {
    return postRequest('/waybill/vehicle/count/query', params);
  },
  // 导出车辆运单统计excel @author lidoudou
  downloadWaybillVehicleCount: (params) => {
    download('车辆运单统计.xlsx', '/waybill/vehicle/count/export', params);
  },
  // 查询销售应收金额统计 @author lidoudou
  queryWaybillReceiveAmount: (params) => {
    return postRequest('/waybill/sale/receiveAmount/query', params);
  },
  // 导出应收金额统计 @author yongqi.wu
  downloadWaybillReceiveAmount: (params) => {
    download('销售应收统计.xlsx', '/waybill/sale/receiveAmount/export', params);
  },
  // 客服单量统计列表 by lidoudou
  queryCustomerWaybillCount: (params) => {
    return postRequest('/waybill/count/list', params);
  }


};

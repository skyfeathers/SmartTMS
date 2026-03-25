import {download, getRequest, postRequest} from '/@/lib/axios';

export const shipperReportApi = {
  // 查询客户每月订单量统计 by lidoudou
  queryShipperOrderStatistic: (params) => {
    return postRequest('/shipper/order/month/statistic', params);
  },
  // 查询客户每月订单量统计-合计 by lidoudou
  queryShipperOrderTotal: (params) => {
    return postRequest('/shipper/order/month/statistic/total', params);
  },
  // 查询客户每月运单量统计 by lidoudou
  queryShipperWaybillStatistic: (params) => {
    return postRequest('/shipper/waybill/month/statistic', params);
  },
  // 查询客户每月运单量统计-合计 by lidoudou
  queryShipperWaybillTotal: (params) => {
    return postRequest('/shipper/waybill/month/statistic/total', params);
  },
  // 统计客户每月新增数量 @author lidoudou
  queryShipperNewStatistic: (params) => {
    return postRequest(`/shipper/new/month/statistic`, params)
  },
  // 客户每月运单量统计导出 by lidodou
  waybillMonthStatisticExport: (fileName, params) => {
    download(fileName, '/shipper/waybill/month/statistic/export', params);
  },
  // 客户每月订单量统计导出 by lidodou
  orderMonthStatisticExport: (fileName, params) => {
    download(fileName, '/shipper/order/month/statistic/export', params);
  },
};

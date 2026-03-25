import { download, getRequest, postRequest } from '/@/lib/axios';

export const dailyReportApi = {
  // 销售日报表（业务负责人） @author zhaoxinyang
  query: (params) => {
    return postRequest('/sale/day/statistic/query', params);
  },
  // 导出销售日报表 @author zhaoxinyang
  downloadSaleDayStatistic: (params) => {
    download('销售日报表.xlsx', '/sale/day/statistic/export', params);
  },
  // 客服日报表（业务负责人） @author zhaoxinyang
  queryCustomerService: (params) => {
    return postRequest('/customerService/day/statistic/query', params);
  },
  // 导出客服日报表 @author zhaoxinyang
  downloadCustomerService: (params) => {
    download('客服日报表.xlsx', '/customerService/day/statistic/export', params);
  },
  // 运营日报表（业务负责人） @author zhaoxinyang
  querySchedule: (params) => {
    return postRequest('/schedule/day/statistic/query', params);
  },
  // 导出运营日报表 @author zhaoxinyang
  downloadSchedule: (params) => {
    download('运营日报表.xlsx', '/schedule/day/statistic/export', params);
  },
  // 客户日报表（业务负责人） @author zhaoxinyang
  querySipper: (params) => {
    return postRequest('/shipper/day/statistic/query', params);
  },
  // 导出客户日报表 @author zhaoxinyang
  downShipper: (params) => {
    download('客户日报表.xlsx', '/shipper/day/statistic/export', params);
  },

};

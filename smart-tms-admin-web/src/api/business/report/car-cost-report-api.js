import { postRequest, download } from '/@/lib/axios';

export const waybillCarCostReportApi = {
  // 自有车基本信息统计查询 @author zhaoxinyang
  queryCarCostDayStatistic: (params) => {
    return postRequest('/car/cost/day/statistic/query', params);
  },
  queryCarCostDayOrMonthStatistic: (params) => {
    return postRequest('/car/cost/dayOrMonth/statistic', params);
  },
  downloadFlowList: (params) => {
    download('车辆流水表.xlsx', '/car/cost/flow/export', params);
  },
  downloadDayList: (params) => {
    download('车辆基本信息统计表.xlsx', '/car/cost/day/statistics/export', params);
  },
  // 自有车月报表统计 @author lidoudou
  queryCarCostMonthStatistic: (params) => {
    return postRequest('/car/cost/month/statistic/query', params);
  },
  downloadMonthList: (params) => {
    download('车辆月统计表.xlsx', '/car/cost/month/statistic/export', params);
  }
};

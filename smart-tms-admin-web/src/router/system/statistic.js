/*
 * @Description:
 * @Author: zhuoda
 * @Date: 2021-08-03
 * @LastEditTime: 2022-06-24
 * @LastEditors: zhuoda
 */


export const statisticRouters = [
  {
    path: '/car-cost/day-or-month-statistic',
    name: 'DayOrMonthStatistic',
    component: import('/@/views/business/report/car-cost/day-or-month-statistic.vue'),
    meta: {
      title: '车辆日/月报表',
      hideInMenu: true,
    },
  },
];

/*
 * @Description:
 * @Author: zhuoda
 * @Date: 2022-02-12
 * @LastEditTime: 2022-06-20
 */

import dayjs from 'dayjs';

export const defaultTimeRanges = [
  { label: '今日', value: [dayjs(), dayjs()]},
  { label: '昨日', value: [dayjs().subtract(1, 'days'), dayjs().subtract(1, 'days')]},
  { label: '本月', value: [dayjs().startOf('month'), dayjs().endOf('month')]},
  { label: '上月', value: [dayjs().subtract(1, 'months').startOf('month'), dayjs().subtract(1, 'months').endOf('month')]},
  { label: '本年度', value: [dayjs().startOf('year'), dayjs().endOf('year')]},
  { label: '上年度', value: [dayjs().subtract(1, 'years').startOf('year'), dayjs().subtract(1, 'years').endOf('year')]},
];

// 不可跨月
export const defaultLimitMonth = [
  { label: '今日', value: [dayjs(), dayjs()]},
  { label: '昨日', value: [dayjs().subtract(1, 'days'), dayjs().subtract(1, 'days')]},
  { label: '本月', value: [dayjs().startOf('month'), dayjs().endOf('month')]},
  { label: '上月', value: [dayjs().subtract(1, 'months').startOf('month'), dayjs().subtract(1, 'months').endOf('month')]},
  { label: '下个月', value: [dayjs().subtract(-1, 'months').startOf('month'), dayjs().subtract(-1, 'months').endOf('month')]},
];

// 线索：今日，昨日，本月，上个月
export const defaultDaysLastMonth = [
  { label: '今日', value: [dayjs(), dayjs()]},
  { label: '昨日', value: [dayjs().subtract(1, 'days'), dayjs().subtract(1, 'days')]},
  { label: '本月', value: [dayjs().startOf('month'), dayjs().endOf('month')]},
  { label: '上月', value: [dayjs().subtract(1, 'months').startOf('month'), dayjs().subtract(1, 'months').endOf('month')]},
];

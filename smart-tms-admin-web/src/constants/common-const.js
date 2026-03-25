/*
 * @Description:
 * @Author: zhuoda
 * @Date: 2021-08-03
 * @LastEditTime: 2022-07-11
 * @LastEditors: zhuoda
 */

export const PAGE_SIZE = 15;

export const PAGE_SIZE_OPTIONS = ['10', '15', '20', '30', '40', '50', '75', '100', '150', '200', '300', '500'];

//登录页面名字
export const PAGE_PATH_LOGIN = '/login';

export const PAGE_PATH_STATISTIC = '/car-cost/day-or-month-statistic';

//首页页面名字
export const PAGE_PATH_HOME = '/home';

//404页面名字
export const PAGE_PATH_404 = '/404';

export const showTableTotal = function (total) {
  return `共${total}条`;
};

export const FLAG_NUMBER_ENUM = {
  TRUE: {
    value: 1,
    desc: '是',
  },
  FALSE: {
    value: 0,
    desc: '否',
  },
};

export const FLAG_TEXT_ENUM = {
  TRUE: {
    value: 1,
    desc: '启用',
  },
  FALSE: {
    value: 0,
    desc: '禁用',
  },
};

export const FLAG_STATUS_TEXT_ENUM = {
  TRUE: {
    value: 1,
    desc: '启用',
  },
  FALSE: {
    value: 2,
    desc: '禁用',
  },
};

export const USER_TYPE_ENUM = {
  ADMIN: {
    value: 1,
    desc: '员工',
  },
  DRIVER:{
    value:2,
    desc:'司机'
  },
  SHIPPER:{
    value:3,
    desc:'货主'
  }
};

export const GENDER_ENUM = {
  UNKNOWN: {
    value: 0,
    desc: '未知',
  },
  MAN: {
    value: 1,
    desc: '男',
  },
  WOMAN: {
    value: 2,
    desc: '女',
  },
};

/**
 * 审核状态
 */
export const AUDIT_STATUS_ENUM = {
  WAIT_AUDIT: {
    value: 1,
    desc: '待审核',
    color: 'default',
  },
  AUDIT_PASS: {
    value: 2,
    desc: '审核通过',
    color: 'success',
  },
  REJECT: {
    value: 3,
    desc: '审核驳回',
    color: 'error',
  },
};

/*
 * @Description:
 * @version:
 * @Author: zhuoda
 * @Date: 2022-08-16 20:28:58
 * @LastEditors: zhuoda
 * @LastEditTime: 2022-08-16
 */

export const NOTICE_VISIBLE_RANGE_DATA_TYPE_ENUM = {
  EMPLOYEE: {
    value: 1,
    desc: '员工',
  },
  DEPARTMENT: {
    value: 2,
    desc: '部门',
  },
};

export const NOTICE_PUSH_TYPE_ENUM =
  {
    PLATFORM_PUSH: {
      value: 1,
      desc: '推送平台'
    },
    DRIVER_PUSH: {
      value: 2,
      desc: '推送司机'
    }
  };
export default {
  NOTICE_VISIBLE_RANGE_DATA_TYPE_ENUM,
  NOTICE_PUSH_TYPE_ENUM
};

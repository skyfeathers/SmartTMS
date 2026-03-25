/*
 * @Author: zhuoda
 * @Date: 2021-08-16 15:12:42
 * @LastEditTime: 2022-06-23
 * @LastEditors: zhuoda
 * @Description:
 * @FilePath: /smart-admin/src/constants/system/employee.ts
 */
export const DATA_SCOPE_LEVEL_ENUM = {
  ME: {
    value: 0,
    desc: '本人',
  },
  DEPARTMENT: {
    value: 5,
    desc: '本部门',
  },
  DEPARTMENT_AND_CHILD: {
    value: 10,
    desc: '本部门及下属子部门',
  },
  // ENTERPRISE: {
  //   value: 50,
  //   desc: '本公司',
  // },
  ALL: {
    value: 100,
    desc: '全部',
  },
}

export default {
  DATA_SCOPE_LEVEL_ENUM,
}

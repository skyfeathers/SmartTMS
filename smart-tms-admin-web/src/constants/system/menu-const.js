/*
 * @Description:
 * @Author: zhuoda
 * @Date: 2021-08-03
 * @LastEditTime: 2022-06-23
 * @LastEditors: zhuoda
 */
export const MENU_TYPE_ENUM = {
  CATALOG: {
    value: 1,
    desc: '目录',
  },
  MENU: {
    value: 2,
    desc: '菜单',
  },
  POINTS: {
    value: 3,
    desc: '按钮',
  },
}

/**
 * 默认的顶级菜单id为0
 */
export const MENU_DEFAULT_PARENT_ID = 0

export default {
  MENU_TYPE_ENUM,
}

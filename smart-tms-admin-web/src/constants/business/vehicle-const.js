/*
 * @Description:
 * @Author: zhuoda
 * @Date: 2022-07-05
 * @LastEditTime: 2022-07-05
 * @LastEditors: zhuoda
 */

/**
 * 经营方式1内管车 2挂靠车 3外派车:
 */
export const VEHICLE_BUSINESS_MODE_ENUM = {
  INNER_MANAGEMENT: {
    value: 1,
    desc: '内管',
  },
  RELY: {
    value: 2,
    desc: '挂靠',
  },
  ASSIGNMENT: {
    value: 3,
    desc: '外派',
  },
};

/**
 * 车牌颜色代码:
 */
export const VEHICLE_PLATE_COLOR_ENUM = {
  BLUE: {
    value: 1,
    desc: '蓝色',
  },
  YELLOW: {
    value: 2,
    desc: '黄色',
  },
  BLACK: {
    value: 3,
    desc: '黑色',
  },
  WHITE: {
    value: 4,
    desc: '白色',
  },
  GREEN: {
    value: 5,
    desc: '绿色',
  },
  OTHER: {
    value: 9,
    desc: '其它',
  },
  AGRICULTURAL_YELLOW: {
    value: 91,
    desc: '农黄色',
  },
  AGRICULTURAL_GREEN: {
    value: 92,
    desc: '农绿色',
  },
  YELLOW_GREEN: {
    value: 93,
    desc: '黄绿色',
  },
  GRADIENT_GREEN: {
    value: 94,
    desc: '渐变绿',
  },
};

/**
 * 所属人性质
 */
export const VEHICLE_OWNER_TYPE_ENUM = {
  PERSONAL: {
    value: 1,
    desc: '个人',
  },
  ENTERPRISE: {
    value: 2,
    desc: '企业',
  },
};

export const
QUICK_CREATE_TYPE_ENUM =
{
  DRIVER:{
    value:10,
    desc:'司机'
  },
  VEHICLE:{
    value:20,
    desc:'车辆'
  },
  BRACKET:{
    value:30,
    desc:'挂车'
  },
  ALL:{
    value:40,
    desc:'全部'
  }
}

export default {
  VEHICLE_PLATE_COLOR_ENUM,
  VEHICLE_BUSINESS_MODE_ENUM,
  VEHICLE_OWNER_TYPE_ENUM,
  QUICK_CREATE_TYPE_ENUM
};

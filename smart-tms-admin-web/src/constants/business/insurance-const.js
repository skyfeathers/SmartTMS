/*
 * @Description: 保险常量
 * @Author: zhuoda
 * @Date: 2022-07-08
 * @LastEditTime: 2022-07-08
 * @LastEditors: zhuoda
 */
/**
 * 保险类型
 */
export const INSURANCE_TYPE_ENUM = {
  COMMERCIAL_INSURANCE: {
    value: 1,
    desc: '商业险',
  },
  COMPULSORY_TRAFFIC_INSURANCE: {
    value: 2,
    desc: '交强险',
  },
  OVERPAYMENT_INSURANCE: {
    value: 3,
    desc: '超赔险'
  },
  EMPLOYER_RESPONSIBILITY_INSURANCE: {
    value: 4,
    desc: '雇主责任险'
  },
  CAR_BOAT_TAX: {
    value: 5,
    desc: '车船税'
  },
  DRIVER_INSURANCE: {
    value: 6,
    desc: '驾乘险'
  }
};

/**
 * 保险对象类型
 */
export const INSURANCE_MODULE_TYPE_ENUM = {
  VEHICLE: {
    value: 1,
    desc: '车辆',
  },
  BRACKET: {
    value: 2,
    desc: '挂车',
  },
};

export default {
  INSURANCE_TYPE_ENUM,
  INSURANCE_MODULE_TYPE_ENUM,
};

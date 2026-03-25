/*
 * @Description:费用项目
 * @Author: zhuoda
 * @Date: 2022-07-18
 * @LastEditTime: 2022-07-18
 * @LastEditors: zhuoda
 */
export const COST_ITEM_TYPE_ENUM = {
  PAY: {
    value: 1,
    desc: '应付',
  },
  RECEIVE: {
    value: 50,
    desc: '应收',
  },
};

export const COST_ITEM_CATEGORY_ENUM = {
  FREIGHT_COST: {
    value: 1,
    desc: '运费',
  },
  EXCEPTION_COST: {
    value: 10,
    desc: '异常费用',
  },
  OIL_CARD: {
    value: 15,
    desc: '油卡',
  },

  OTHER: {
    value: 100,
    desc: '其他',
  },
};

export default {
  COST_ITEM_TYPE_ENUM,
  COST_ITEM_CATEGORY_ENUM,
};

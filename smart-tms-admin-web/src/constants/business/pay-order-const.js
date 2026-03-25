/*
 * @Description: 付款常量
 * @Author: zhuoda
 * @Date: 2022-07-21
 * @LastEditTime: 2022-08-02
 * @LastEditors: zhuoda
 */
export const PAY_ORDER_STATUS_ENUM = {
  NO_APPLY: {
    value: 0,
    desc: '未申请',
    color: 'default',
  },
  NO_PAY: {
    value: 10,
    desc: '未支付',
    color: 'warning',
  },
  NFT_PAY: {
    value: 15,
    desc: '付款中',
    color: 'processing',
  },
  PAID: {
    value: 20,
    desc: '已支付',
    color: 'success',
  },

  CANCEL: {
    value: 50,
    desc: '已作废',
    color: 'error',
  },
};

export const PAY_ORDER_ACTIVE_TAB = {
  WAIT_AUDIT: {
    value: 'WAIT_AUDIT',
    desc: '待审核',
  },
  WAIT_PAY: {
    value: 'WAIT_PAY',
    desc: '待付款',
  },
  PAYMENT: {
    value: 'PAYMENT',
    desc: '已付款未核销',
    color: 'success',
  },
  VERIFICATION: {
    value: 'VERIFICATION',
    desc: '已核销',
  },
  CANCEL: {
    value: 'CANCEL',
    desc: '已作废',
  },
};

// 网络货运单据的TAB
export const PAY_ORDER_NFT_ACTIVE_TAB = {
  WAIT_AUDIT: {
    value: 'WAIT_AUDIT',
    desc: '待审核',
  },
  WAIT_PAY: {
    value: 'WAIT_PAY',
    desc: '待付款',
  },
  NFT_PAY: {
    value: 'NFT_PAY',
    desc: '货运平台付款中',
  },
  PAYMENT: {
    value: 'PAYMENT',
    desc: '已付款未核销',
    color: 'success',
  },
  VERIFICATION: {
    value: 'VERIFICATION',
    desc: '已核销',
  },
  CANCEL: {
    value: 'CANCEL',
    desc: '已作废',
  },
};

export const PAY_ORDER_TYPE_ENUM = {
  CASH: {
    value: 0,
    desc: '现金支付付款单'
  },
  OIL_CARD: {
    value: 10,
    desc: '油卡支付付款单'
  }
}

export default {
  PAY_ORDER_ACTIVE_TAB,
  PAY_ORDER_STATUS_ENUM,
  PAY_ORDER_NFT_ACTIVE_TAB,
  PAY_ORDER_TYPE_ENUM,
};

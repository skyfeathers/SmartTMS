/*
 * @Description: file content
 * @Author: yandy
 * @Date: 2022-07-24 21:43:43
 * @LastEditors:
 * @LastEditTime: 2022-07-24 21:43:43
 */
export const MSG_TYPE_ENUM =
  {
    AUDIT: {
      value: 100,
      desc: '审批'
    },
    SHIPPER: {
      value: 200,
      desc: '货主'
    },
    DRIVER: {
      value: 300,
      desc: '司机'
    },
    BRACKET: {
      value: 400,
      desc: '挂车'
    },
    CAR: {
      value: 500,
      desc: '车辆'
    },
    ORDER: {
      value: 600,
      desc: '订单'
    },
    CONTRACT: {
      value: 700,
      desc: '合同'
    },
    RECEIVE_ORDER: {
      value: 800,
      desc: '应收账款'
    }
  };
export const MSG_SUB_TYPE_ENUM = {
  AUDIT_WAY_BILL_AUDIT: {
    value: 1000,
    desc: '运单审批'
  },
  AUDIT_OIL_CARD_AUDIT: {
    value: 1001,
    desc: '油卡充值审批'
  },
  AUDIT_PAY_AUDIT: {
    value: 1002,
    desc: '应付款审批'
  },
  AUDIT_RECEIVE_AUDIT: {
    value: 1003,
    desc: '应收款审批'
  },
  SHIPPER_FA_PIAO_ZHANG_DAN_SHANG_CHUAN: {
    value: 2001,
    desc: '货主发票账单已上传'
  },
  ORDER_DUI_ZHANG_DAN_ZUO_FEI: {
    value: 6000,
    desc: '对账单作废'
  },
  ORDER_CREATE: {
    value: 6001,
    desc: '订单创建'
  },
  ORDER_NOT_SCHEDULED: {
    value: 6002,
    desc: '订单未分配司机'
  },
  CONTRACT_EXPIRE: {
    value: 7000,
    desc: '合同到期'
  },
  RECEIVE_ORDER_ACCOUNT_PERIOD_DATE: {
    value: 7001,
    desc: '应收账款到期'
  }
};

export const MSG_RECEIVE_TYPE_ENUM =
  {
    ADMIN: {
      value: 1,
      desc: '后管'
    },
    SHIPPER: {
      value: 2,
      desc: '货主'
    },
    DRIVER: {
      value: 3,
      desc: '司机'
    }
  };

export default {
  MSG_TYPE_ENUM,
  MSG_SUB_TYPE_ENUM,
  MSG_RECEIVE_TYPE_ENUM
};

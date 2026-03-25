export const OIL_CARD_TYPE_ENUM =
  {
    MASTER: {
      value: 1,
      desc: '主卡'
    },
    SLAVE: {
      value: 2,
      desc: '副卡'
    }
  };

// 油卡金额变动记录类型
export const OIL_CARD_BALANCE_RECORD_TYPE_ENUM =
  {
    RECHARGE: {
      value: 1,
      desc: '充值'
    },
    DISTRIBUTE: {
      value: 2,
      desc: '分配'
    },
    EXPEND: {
      value: 3,
      desc: '主卡消费'
    },
    CANCEL_RECHARGE: {
      value: 4,
      desc: '挂失圈回'
    },
    CIRCUMFLEX: {
      value: 5,
      desc: '圈回'
    }
  };
export default {
  OIL_CARD_TYPE_ENUM,
  OIL_CARD_BALANCE_RECORD_TYPE_ENUM
};

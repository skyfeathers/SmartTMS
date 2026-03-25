export const CAR_COST_CATEGORY_TYPE_ENUM =
{
  CASH_COST: { value: 10, desc: '现金' },

  OIL_CARD: { value: 20, desc: '油费' },

  ETC_COST: { value: 30, desc: 'ETC' },

  UREA_COST: { value: 40, desc: '尿素' },
};

// 支付方式
export const CAR_COST_PAY_MODE_ENUM =
{
  CASH_COST: {
    value: 1,
    desc: '现金'
  },
  OIL_CARD: {
    value: 2,
    desc: '油卡'
  },
  ETC_CARD: {
    value: 3,
    desc: 'ETC卡'
  }
};

export const
  CAR_COST_MODULE_TYPE_ENUM =
  {
    CASH_RECEIVE: {
      value: 1,
      desc: '现金申请'
    },
    OIL_CARD_RECEIVE: {
      value: 2,
      desc: '油卡充值'
    },
    CASH_PAY: {
      value: 3,
      desc: '现金支出'
    },
    OIL_PAY: {
      value: 4,
      desc: '油费支出'
    },
    ETC_PAY: {
      value: 5,
      desc: 'ETC支出'
    },
    UREA_PAY: {
      value: 6,
      desc: '尿素支出'
    }
  }

export const OIL_CARD_FUEL_TYPE_ENUM ={
    DIESEL_OIL_CARD: {
        value: 1,
        desc: '柴油'
    },
    GASOLINE_CARD: {
        value: 2,
        desc: '汽油'
    }
}
export default {
  CAR_COST_CATEGORY_TYPE_ENUM,
  CAR_COST_PAY_MODE_ENUM,
  CAR_COST_MODULE_TYPE_ENUM,
  OIL_CARD_FUEL_TYPE_ENUM
};


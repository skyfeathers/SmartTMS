// 订单状态
export const ORDER_STATUS_ENUM =
  {
    TRANSPORT: {
      value: 50,
      desc: '运输中',
      color: 'success'
    },
    COMPLETE: {
      value: 60,
      desc: '已完成'
    },
    CANCEL: {
      value: 70,
      desc: '已取消',
      color: 'error'
    }
  };

// 发票种类
export const INVOICE_KIND_TYPE_ENUM =
  {
    COMMON_INVOICE: {
      value: 1,
      desc: '普通发票'
    },
    SPECIAL_INVOICE: {
      value: 2,
      desc: '专项发票'
    },
    // VAT_INVOICE: {
    //   value: 3,
    //   desc: '增值税发票'
    // }
  };

// 发票类型
export const INVOICE_TYPE_ENUM =
  {
    PAPER_INVOICE: {
      value: 1,
      desc: '纸质发票'
    },
    ELECTRONIC_INVOICE: {
      value: 2,
      desc: '电子发票'
    }
  };

// 订单类型
export const ORDER_TYPE_ENUM =
  {
    ORDINARY: {
      value: 1,
      desc: '普通订单'
    },
    NETWORK_FREIGHT: {
      value: 2,
      desc: '网络货运'
    },
    // AN_LIAN: {
    //   value: 3,
    //   desc: '安联'
    // }
  };

// 货物单位
export const GOODS_UNIT_TYPE_ENUM =
  {
    UNIT_CAR: {
      value: 10,
      desc: '车'
    },
    UNIT_BOX: {
      value: 20,
      desc: '箱'
    },
    UNIT_ITEM: {
      value: 30,
      desc: '件'
    },
    UNIT_TOM: {
      value: 40,
      desc: '吨'
    }
  };
export default {
  ORDER_STATUS_ENUM,
  INVOICE_KIND_TYPE_ENUM,
  INVOICE_TYPE_ENUM,
  ORDER_TYPE_ENUM,
  GOODS_UNIT_TYPE_ENUM,
};

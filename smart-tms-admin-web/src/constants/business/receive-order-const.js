// 核算状态
export const CHECK_STATUS_ENUM =
  {
    WAIT_CHECK: {
      value: 1,
      desc: '待核算'
    },
    CHECK: {
      value: 2,
      desc: '已核算'
    },
    CANCEL: {
      value: 3,
      desc: '已作废'
    }
  };

// 开票状态
export const INVOICE_STATUS_ENUM =
  {
    WAIT_INVOICE: {
      value: 1,
      desc: '待开票'
    },
    INVOICE: {
      value: 2,
      desc: '已开票'
    },
    PORTION_INVOICE: {
      value: 3,
      desc: '部分开票'
    },
    CANCEL: {
      value: 4,
      desc: '作废'
    }
  };

export default {
  CHECK_STATUS_ENUM,
  INVOICE_STATUS_ENUM
};

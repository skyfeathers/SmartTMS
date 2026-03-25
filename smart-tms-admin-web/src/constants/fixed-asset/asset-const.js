/**
 * 固定资产 枚举
 *
 * @Author:    lidoudou
 * @Date:      2023-03-15 14:15:14
 * @Copyright  1024创新实验室
 */

import repair from './repair-const';
import scrap from './scrap-const';

export const ASSET_STATUS_ENUM = {
  USED: {
    value: 1,
    desc: '在用'
  },
  UNUSED: {
    value: 5,
    desc: '闲置'
  },
  BORROW: {
    value: 10,
    desc: '借用'
  },
  REQUISITION:{
    value:15,
    desc:'领用'
  },
  ALLOCATION: {
    value: 20,
    desc: '调拨中'
  },
  REPAIR: {
    value: 25,
    desc: '维修中'
  },
  TO_BE_REQUISITION: {
    value: 30,
    desc: '领用待确认'
  },
  TO_BE_BORROW: {
    value: 35,
    desc: '借用待确认'
  },
  CANCEL: {
    value: 40,
    desc: '作废'
  }
};

export const REQUISITION_STATUS_ENUM = {
  WAIT: {
    value: 1,
    desc: '领用待确认'
  },
  REJECT: {
    value: 2,
    desc: '已驳回'
  },
  COMPLETE: {
    value: 3,
    desc: '已完成'
  },
};



export const ALLOCATION_STATUS_ENUM = {
  WAIT: {
    value: 1,
    desc: '待审核'
  },
  REJECT: {
    value: 2,
    desc: '已驳回'
  },
  COMPLETE: {
    value: 3,
    desc: '已完成'
  },
};


export const TRANSFER_STATUS_ENUM = {
  WAIT: {
    value: 1,
    desc: '待审核'
  },
  REJECT: {
    value: 2,
    desc: '已驳回'
  },
  COMPLETE: {
    value: 3,
    desc: '已完成'
  },
}

export const ASSET_CHECK_TYPE_ENUM = {
  QR_CODE: {
    value: 1,
    desc: '扫码盘点'
  },
  NUMBER: {
    value: 2,
    desc: '数量盘点'
  }
}


export const ASSET_CHECK_STATUS_ENUM = {
  NOT_CHECK: {
    value: 5,
    desc: '未盘点'
  },
  PAN_PING: {
    value: 10,
    desc: '盘平'
  },
  PROFIT: {
    value: 15,
    desc: '盘盈'
  },
  LOSS: {
    value: 20,
    desc: '盘亏'
  }
}


export const ASSET_BORROW_STATUS_ENUM = {
  WAIT: {
    value: 1,
    desc: '已提交'
  },
  REJECT: {
    value: 2,
    desc: '已驳回'
  },
  COMPLETE: {
    value: 3,
    desc: '已完成'
  }
}

export default {
  ASSET_STATUS_ENUM,
  REQUISITION_STATUS_ENUM,
  ALLOCATION_STATUS_ENUM,
  TRANSFER_STATUS_ENUM,
  ...repair,
  ...scrap,
  ASSET_CHECK_TYPE_ENUM,
  ASSET_CHECK_STATUS_ENUM,
  ASSET_BORROW_STATUS_ENUM
};

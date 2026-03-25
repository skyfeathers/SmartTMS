/*
 * @Description:
 * @version:
 * @Author: lidoudou
 * @Date: 2022-07-05 15:44:47
 * @LastEditors: lidoudou
 * @LastEditTime: 2022-07-08 15:03:22
 */

// 货主性质
export const SHIPPER_NATURE_ENUM =
{
    ENTERPRISE: {
        value: 0,
        desc: '企业'
    },
    PERSONAL: {
        value: 1,
        desc: '个人'
    }
}
// 业务关系
export const SHIPPER_TYPE_ENUM =
{
    CUSTOMER: {
        value: 0,
        desc: '客户'
    },
    SUPPLIER: {
        value: 1,
        desc: '工厂'
    }
}
// 货主等级
export const SHIPPER_GRADE_ENUM =
{
    CORE: {
        value: 1,
        desc: '核心'
    },
    POTENTIAL: {
        value: 2,
        desc: '有潜力'
    },
    GENERAL: {
        value: 3,
        desc: '普通'
    },
    BAD: {
        value: 4,
        desc: '较差'
    }
}
// 货主标签
export const
    SHIPPER_TAG_ENUM =
    {
        POTENTIAL: {
            value: 0,
            desc: '潜在'
        },
        INTENTION: {
            value: 1,
            desc: '意向'
        },
        NEGOTIATION: {
            value: 2,
            desc: '洽谈'
        },
        DEAL: {
            value: 3,
            desc: '成交'
        },
        LOSS: {
            value: 4,
            desc: '流失'
        },
    }
// 货主联系人类型
export const PRINCIPAL_TYPE_ENUM =
{
    CUSTOMER_SERVICE: {
        value: 1,
        desc: '客服'
    },
    MANAGER: {
        value: 2,
        desc: '调度'
    }
}

// 货主付款方式
export const PAYMENT_TYPE_ENUM =
{
    BANK: {
        value: 0,
        desc: '银行卡'
    },
    WE_CHAT: {
        value: 1,
        desc: '微信'
    },
    ZHI_FU_BAO: {
        value: 2,
        desc: '支付宝'
    }
}

// 跟进方式
export const TRACK_WAY_ENUM =
  {
    WX: {
      value: 1,
      desc: '微信'
    },
    PHONE: {
      value: 2,
      desc: '电话'
    },
    FACE_TO_FACE: {
      value: 3,
      desc: '面谈'
    }
  };

export default {
  SHIPPER_NATURE_ENUM,
  SHIPPER_TYPE_ENUM,
  SHIPPER_GRADE_ENUM,
  SHIPPER_TAG_ENUM,
  PRINCIPAL_TYPE_ENUM,
  PAYMENT_TYPE_ENUM,
  TRACK_WAY_ENUM,
};

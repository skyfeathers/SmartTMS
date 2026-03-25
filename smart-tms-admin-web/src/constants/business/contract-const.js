// 合同签署人类型
export const CONTRACT_SIGNER_TYPE_ENUM =
  {
    DRIVER: {
      value: 1,
      desc: '司机'
    },
    FLEET: {
      value: 2,
      desc: '车队'
    },
    SHIPPER: {
      value: 3,
      desc: '货主'
    }
  };

// 合同状态
export const CONTRACT_STATUS_ENUM =
  {
    WAIT_SIGN: {
      value: 0,
      desc: '待签署'
    },
    SIGNED: {
      value: 1,
      desc: '已签署'
    },
    CANCEL: {
      value: 2,
      desc: '已作废'
    }
  };

// 合同签署类型
export const CONTRACT_SIGN_TYPE_ENUM =
  {
    ONLINE: {
      value: 1,
      desc: '电子签约'
    },
    OFFLINE: {
      value: 2,
      desc: '线下签约'
    }
  };

export default {
  CONTRACT_SIGNER_TYPE_ENUM,
  CONTRACT_STATUS_ENUM,
  CONTRACT_SIGN_TYPE_ENUM,
};

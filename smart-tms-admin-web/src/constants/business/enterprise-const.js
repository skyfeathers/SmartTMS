// 企业类型
export const ENTERPRISE_TYPE_ENUM =
  {
    NORMAL: {
      value: 1,
      desc: '非网络货运'
    },
    NFT: {
      value: 2,
      desc: '网络货运'
    }
  };

// 业务设置类型
export const BUSINESS_SETTING_ENUM = {
  CONTRACT_MESSAGE_RECEIVER: {
    value: 'contract_message_receiver',
    desc: '合同到期提醒人'
  },
  RECEIVE_ORDER_MESSAGE_RECEIVER: {
    value: 'receive_order_message_receiver',
    desc: '合同到期提醒人'
  }
};

export default {
  ENTERPRISE_TYPE_ENUM,
  BUSINESS_SETTING_ENUM
};

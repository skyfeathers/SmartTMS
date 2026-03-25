/*
 * @Description:
 * @version:
 * @Author: zhuoda
 * @Date: 2021-08-18 14:31:29
 * @LastEditors: zhuoda
 * @LastEditTime: 2022-08-25
 */
// 文件上传类型
export const FILE_FOLDER_TYPE_ENUM = {
  COMMON: {
    value: 1,
    desc: '通用文件夹',
  },
  SHIPPER: {
    value: 2,
    desc: '货主相关',
  },
  DRIVER: {
    value: 3,
    desc: '司机',
  },
  VEHICLE: {
    value: 4,
    desc: '车辆',
  },
  BRACKET: {
    value: 5,
    desc: '挂车',
  },
  FLEET: {
    value: 6,
    desc: '车队',
  },
  CONTRACT: {
    value: 7,
    desc: '合同',
  },
  NOTICE: {
    value: 8,
    desc: '公告/动态附件',
  },
  WAYBILL_VOUCHER: {
    value: 9,
    desc: '运单凭证',
  },
  PAY_ORDER: {
    value: 10,
    desc: '付款单',
  },
  RECEIVE_ORDER: {
    value: 11,
    desc: '收款单',
  },
  HELP_DOC: {
    value: 12,
    desc: '帮助中心',
  },
  FEEDBACK: {
    value: 13,
    desc: '意见反馈',
  },
  INSURANCE: {
    value: 14,
    desc: '保险'
  },
  VEHICLE_COST: {
    value: 15,
    desc: '车辆费用'
  },
  ORDER: {
    value: 16,
    desc: '订单'
  },
  FIXED_ASSET: {
    value: 17,
    desc: '固定资产'
  },
  CONSUMABLES: {
    value: 18,
    desc: '低值易耗品'
  },
  MOBILE_APP: {
    value: 19,
    desc: '移动APP'
  },
  CAR_COST: {
    value: 20,
    desc: '自有车'
  }
};
export default {
  FILE_FOLDER_TYPE_ENUM,
};

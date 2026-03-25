/*
 * @Description: file content
 * @Author: yandy
 * @Date: 2022-07-21 23:00:24
 * @LastEditors:
 * @LastEditTime: 2022-07-21 23:00:24
 */
// 业务类型
export const DATA_TRACER_BUSINESS_TYPE_ENUM = {
  SHIPPER: {
    value: 1,
    desc: '货主'
  },
  VEHICLE: {
    value: 2,
    desc: '车辆'
  },
  DRIVER: {
    value: 3,
    desc: '司机'
  },
  FLEET: {
    value: 4,
    desc: '车队'
  },
  BRACKET: {
    value: 5,
    desc: '挂车'
  },
  WAYBILL: {
    value: 6,
    desc: '运单'
  },
  ORDER: {
    value: 7,
    desc: '订单'
  },
  PAY_ORDER: {
    value: 8,
    desc: '付款单'
  },
  RECEIVE_ORDER: {
    value: 9,
    desc: '收款单'
  },
  OIL_CARD: {
    value: 10,
    desc: '油卡'
  },
  ETC: {
    value: 11,
    desc: 'ETC'
  },
  CONTRACT: {
    value: 12,
    desc: '合同'
  },
  ASSET: {
    value: 13,
    desc: '资产'
  },
  ASSET_PURCHASE: {
    value: 14,
    desc: '资产采购'
  },
  ASSET_REQUISITION: {
    value: 15,
    desc: '资产领用'
  },
  ASSET_REVERT: {
    value: 16,
    desc: '资产退还'
  },
  ASSET_BORROW: {
    value: 17,
    desc: '资产借用'
  },
  ASSET_BACK: {
    value: 18,
    desc: '资产归还'
  },
  ASSET_ALLOCATION: {
    value: 19,
    desc: '资产调拨'
  },
  ASSET_TRANSFER: {
    value: 20,
    desc: '资产转移'
  },
  ASSET_REPAIR: {
    value: 21,
    desc: '资产维修'
  },
  ASSET_CLEAR: {
    value: 22,
    desc: '资产报废'
  },
  ASSET_CHECK: {
    value: 23,
    desc: '资产盘点'
  },
  COST_APPLY: {
    value: 24,
    desc: '费用申请'
  },
  CAR_COST: {
    value: 25,
    desc: '自有车费用登记'
  },
  ASSET_DEPRECIATION: {
    value: 26,
    desc: '资产折旧'
  },
  CONSUMABLES_STOCK: {
    value: 27,
    desc: '易耗品清单'
  },
  CONSUMABLES_STOCK_PURCHASE: {
    value: 28,
    desc: '易耗品采购'
  },
  CONSUMABLES_STOCK_CHECK: {
    value: 29,
    desc: '易耗品盘点'
  },
  CONSUMABLES_STOCK_REQUISITION: {
    value: 30,
    desc: '易耗品领用'
  },
  CAR_PAY_WAYBILL: {
    value: 31,
    desc: '挂靠车费用登记'
  },
  CAR_PAY_COST: {
    value: 32,
    desc: '挂靠车费用报表'
  },
  VEHICLE_COST: {
    value: 33,
    desc: '车辆费用'
  }
};

export const DATA_TRACER_OPERATE_TYPE_ENUM = {

  SAVE: {
    value: 1,
    desc: '保存'
  },
  UPDATE: {
    value: 2,
    desc: '更新'
  },
  DELETE: {
    value: 3,
    desc: '删除'
  },
};

export default {
  DATA_TRACER_BUSINESS_TYPE_ENUM,
  DATA_TRACER_OPERATE_TYPE_ENUM,
};

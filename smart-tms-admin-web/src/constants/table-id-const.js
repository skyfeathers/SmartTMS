/*
 * @Description: 表格id
 * @Author: zhuoda
 * @Date: 2022-08-21
 * @LastEditTime: 2022-08-21
 * @LastEditors: zhuoda
 */

let initTableId = 1000;

const INSURANCE = initTableId + 1; //保险
const VEHICLE = initTableId + 2; //车辆
const SHIPPER = initTableId + 3; //货主
const DRIVER = initTableId + 4; //司机
const FLEET = initTableId + 5; //车队
const BRACKET = initTableId + 6; //车架


const WAYBILL = initTableId + 20; //运单

const PAY_ORDER_WAIT_AUDIT = initTableId + 39; //付款单-待审核
const PAY_ORDER_WAIT_PAY = initTableId + 40; //付款单-待付款
const PAY_ORDER_PAYMENT = initTableId + 41; //付款单-已付款
const PAY_ORDER_VERIFICATION = initTableId + 42; //付款单-已核销
const PAY_ORDER_ALL = initTableId + 42; //付款单-全部

const NFT_PAY_ORDER_WAIT_PAY = initTableId + 43; //NFT-付款单-待付款
const NFT_PAY_ORDER_PAYMENT = initTableId + 44; //NFT-付款单-已付款
const NFT_PAY_ORDER_VERIFICATION = initTableId + 45; //NFT-付款单-已核销

const ORDER = initTableId + 60; //订单

const RECEIVE_ORDER_WAIT_CHECK = initTableId + 80; //应收-待核算
const RECEIVE_ORDER_CHECK = initTableId + 81; //应收-已核算
const RECEIVE_ORDER_CANCEL = initTableId + 82; //应收-作废
const RECEIVE_ORDER_VERIFICATION = initTableId + 83; //应收核销


const INVOICE_APPLY_WAIT_INVOICE = initTableId + 101; //申请开票-待开票
const INVOICE_APPLY_ALREADY = initTableId + 102; //申请开票-已开票
const INVOICE_APPLY_CANCEL = initTableId + 103; //申请开票-已作废

const OIL_CARD_MASTER = initTableId + 120; //油卡-主卡
const OIL_CARD_SLAVE = initTableId + 121; //油卡-副卡

const ETC = initTableId + 140; //ETC

const CONTRACT = initTableId + 160; //合同

const EXPIRED_CERTIFICATE = initTableId + 180; //到期证件

const CAPITAL_FLOW_PAY = initTableId + 200; //资金流水-应付
const CAPITAL_FLOW_RECEIVE = initTableId + 201; //资金流水-应收

const REFUELING_RECORD = initTableId + 220; //车辆加油

const OIL_CARD_RECHARGE_APPLY = initTableId + 230; //油卡充值申请

const WAYBILL_PROFIT_REPORT = initTableId + 240; //运单利润表

const WAYBILL_INVOICE = initTableId + 260; //运单付款管理

const ASSET_LIST = initTableId + 300; // 资产清单
const ASSET_PURCHASE = initTableId + 301; // 资产采购
const ASSET_REQUISITION = initTableId + 302; // 资产领用
const ASSET_REVERT = initTableId + 303; // 资产退还
const ASSET_BORROW = initTableId + 304; // 资产借用
const ASSET_BACK = initTableId + 305; // 资产归还
const ASSET_ALLOCATION = initTableId + 306; // 资产调拨
const ASSET_TRANSFER = initTableId + 307; // 资产转移
const ASSET_REPAIR = initTableId + 308; // 维修登记
const ASSET_SCRAP = initTableId + 309; // 资产报废
const ASSET_CHECK = initTableId + 310; // 资产盘点
const ASSET_DEPRECIATION_REPORT = initTableId + 311; // 资产折旧明细表
const ASSET_DEPRECIATION = initTableId + 312; // 资产折旧明细表

const OWN_CAR_COST = initTableId + 330; // 自有车费用
const OWN_CAR_COST_REPORT = initTableId + 331; // 自有车费用报表
const OWN_CAR_COST_MONTH = initTableId + 332; // 自有车月报表

const CAR_PAY_COST = initTableId + 340; // 挂靠车费用登记
const CAR_PAY_COST_REPORT = initTableId + 341; // 挂靠车报表

export const TABLE_ID_CONST = {
  INSURANCE,
  VEHICLE,
  SHIPPER,
  DRIVER,
  FLEET,
  BRACKET,

  WAYBILL,

  PAY_ORDER_WAIT_AUDIT,
  PAY_ORDER_WAIT_PAY,
  PAY_ORDER_PAYMENT,
  PAY_ORDER_VERIFICATION,
  PAY_ORDER_ALL,

  NFT_PAY_ORDER_WAIT_PAY,
  NFT_PAY_ORDER_PAYMENT,
  NFT_PAY_ORDER_VERIFICATION,

  ORDER,

  RECEIVE_ORDER_WAIT_CHECK,
  RECEIVE_ORDER_CHECK,
  RECEIVE_ORDER_CANCEL,
  RECEIVE_ORDER_VERIFICATION,

  INVOICE_APPLY_WAIT_INVOICE,
  INVOICE_APPLY_ALREADY,
  INVOICE_APPLY_CANCEL,

  OIL_CARD_MASTER,
  OIL_CARD_SLAVE,

  ETC,

  CONTRACT,

  EXPIRED_CERTIFICATE,

  CAPITAL_FLOW_PAY,
  CAPITAL_FLOW_RECEIVE,

  REFUELING_RECORD,

  OIL_CARD_RECHARGE_APPLY,
  WAYBILL_PROFIT_REPORT,
  WAYBILL_INVOICE,

  // 资产相关
  ASSET_LIST,
  ASSET_PURCHASE,
  ASSET_REQUISITION,
  ASSET_REVERT,
  ASSET_BORROW,
  ASSET_BACK,
  ASSET_ALLOCATION,
  ASSET_TRANSFER,
  ASSET_REPAIR,
  ASSET_SCRAP,
  ASSET_CHECK,
  ASSET_DEPRECIATION_REPORT,
  ASSET_DEPRECIATION,

  OWN_CAR_COST,
  OWN_CAR_COST_REPORT,
  OWN_CAR_COST_MONTH,
  CAR_PAY_COST,
  CAR_PAY_COST_REPORT
};

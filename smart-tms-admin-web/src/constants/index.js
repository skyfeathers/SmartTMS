/*
 * @Description:
 * @version:
 * @Author: zhuoda
 * @Date: 2021-08-11 18:14:13
 * @LastEditors: zhuoda
 * @LastEditTime: 2022-08-16
 */
import menu from './system/menu-const';
import employeeConst from './system/employee-const';
import goods from './business/goods-const';
import category from './business/category-const';
import { LOGIN_DEVICE_ENUM } from './system/login-device-const';
import { AUDIT_STATUS_ENUM, FLAG_NUMBER_ENUM, GENDER_ENUM, USER_TYPE_ENUM, FLAG_STATUS_TEXT_ENUM } from './common-const';
import { LAYOUT_ENUM } from './layout-const';
import file from './business/file-const';
import notice from './business/notice-const';
import loginLog from './support/login-log-const';
import vehicleConst from './business/vehicle-const';
import shipperConst from './business/shipper-const';
import insurance from './business/insurance-const';
import driverConst from './business/driver-const';
import cardConst from './business/card-const';
import fleetConst from './business/fleet-const';
import flowConst from './business/flow-const';
import orderConst from './business/order-const';
import transportRouteConst from './business/transport-route-const';
import repairConst from './business/repair-const';
import waybill from './business/waybill-const';
import contract from './business/contract-const';
import cost from './business/cost-const';
import expire from './business/exipre-const';
import dataTracer from './support/data-tracer-const';
import pay from './business/pay-order-const';
import receiveOrder from './business/receive-order-const';
import message from './business/message-const';
import enterprise from './business/enterprise-const';
import nft from './business/nft-const';
import changelog from './support/change-Log-const';
import codeGenerator from './support/code-generator-const';
import asset from './fixed-asset/asset-const';
import costApply from './business/cost-apply-const';
import businessTypeConst from './business/business-type-const';
import mobileAppConst from './support/mobile-app-const';
import carCost from './business/car-cost-const';
import pic from './business/pic-const';

export default {
  FLAG_NUMBER_ENUM,
  LOGIN_DEVICE_ENUM,
  GENDER_ENUM,
  LAYOUT_ENUM,
  AUDIT_STATUS_ENUM,
  USER_TYPE_ENUM,
  FLAG_STATUS_TEXT_ENUM,
  ...employeeConst,
  ...dataTracer,
  ...loginLog,
  ...menu,
  ...goods,
  ...category,
  ...file,
  ...notice,
  ...vehicleConst,
  ...shipperConst,
  ...insurance,
  ...driverConst,
  ...fleetConst,
  ...cardConst,
  ...flowConst,
  ...orderConst,
  ...transportRouteConst,
  ...repairConst,
  ...waybill,
  ...contract,
  ...cost,
  ...expire,
  ...pay,
  ...receiveOrder,
  ...message,
  ...enterprise,
  ...nft,
  ...changelog,
  ...codeGenerator,
  ...asset,
  ...costApply,
  ...businessTypeConst,
  ...mobileAppConst,
  ...carCost,
  ...pic
};

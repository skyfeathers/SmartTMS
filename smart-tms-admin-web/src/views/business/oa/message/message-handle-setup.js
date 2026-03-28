/*
 * @Description: file content
 * @Author: yandy
 * @Date: 2022-07-13 17:38:06
 * @LastEditors: zhuoda
 * @LastEditTime: 2022-08-04
 */
import { useRouter } from 'vue-router';
import { MSG_RECEIVE_TYPE_ENUM, MSG_SUB_TYPE_ENUM, MSG_TYPE_ENUM } from '/@/constants/business/message-const';
import { messageApi } from '/@/api/business/message/message-api';

export function messageSetup () {
  // 去处理
  const router = useRouter();

  function toHandle (record) {
    //标记已读
    readHandle(record.msgId);
    // 应收账款到期
    if (record.msgSubType == MSG_SUB_TYPE_ENUM.RECEIVE_ORDER_ACCOUNT_PERIOD_DATE.value) {
      router.push({
        path: '/receive-order/detail',
        query: {
          receiveOrderId: record.dataId
        }
      });
      return;
    }
    // 合同到期
    if (record.msgSubType == MSG_SUB_TYPE_ENUM.CONTRACT_EXPIRE.value) {
      router.push({
        path: '/contract/list',
      });
      return;
    }

    // 订单创建
    if (record.msgSubType == MSG_SUB_TYPE_ENUM.ORDER_CREATE.value) {
      router.push({
        path: '/order/detail',
        query: {
          orderId: record.dataId,
        },
      });
      return;
    }
    // 运单审核
    if (record.msgSubType == MSG_SUB_TYPE_ENUM.AUDIT_WAY_BILL_AUDIT.value) {
      router.push({
        path: '/waybill/waybill-detail',
        query: {
          waybillId: record.dataId,
        },
      });
      return;
    }
    // 油卡充值审批
    if (record.msgSubType == MSG_SUB_TYPE_ENUM.AUDIT_OIL_CARD_AUDIT.value) {
      router.push({
        path: '/pay/pay-order-detail',
        query: {
          payOrderId: record.dataId,
        },
      });
      return;
    }
    // 应付款审批
    if (record.msgSubType == MSG_SUB_TYPE_ENUM.AUDIT_PAY_AUDIT.value) {
      router.push({
        path: '/pay/pay-order-detail',
        query: {
          payOrderId: record.dataId,
        },
      });
      return;
    }
    // 应收款审批、对账单作废、发票上传
    let receiveOrderTypeList = [
      MSG_SUB_TYPE_ENUM.AUDIT_RECEIVE_AUDIT.value,
      MSG_SUB_TYPE_ENUM.ORDER_DUI_ZHANG_DAN_ZUO_FEI.value,
      MSG_SUB_TYPE_ENUM.SHIPPER_FA_PIAO_ZHANG_DAN_SHANG_CHUAN.value
    ];
    if (receiveOrderTypeList.includes(record.msgSubType)) {
      router.push({
        path: '/receive-order/detail',
        query: {
          receiveOrderId: record.dataId,
        },
      });
      return;
    }

    // 司机
    if (record.msgType == MSG_TYPE_ENUM.DRIVER.value) {
      router.push({
        path: '/driver/detail',
        query: {
          driverId: record.dataId,
        },
      });
      return;
    }
    // 车辆
    if (record.msgType == MSG_TYPE_ENUM.CAR.value) {
      router.push({
        path: '/vehicle/vehicle-detail',
        query: {
          vehicleId: record.dataId,
        },
      });
      return;
    }
    // 货主
    if (record.msgType == MSG_TYPE_ENUM.SHIPPER.value) {
      router.push({
        path: '/shipper/detail',
        query: {
          shipperId: record.dataId,
        },
      });
      return;
    }
    // BRACKET
    if (record.msgType == MSG_TYPE_ENUM.SHIPPER.value) {
      router.push({
        path: '/bracket/detail',
        query: {
          bracketId: record.dataId,
        },
      });
      return;
    }
    // ORDER
    if (record.msgType == MSG_TYPE_ENUM.SHIPPER.value) {
      router.push({
        path: '/order/detail',
        query: {
          orderId: record.dataId,
        },
      });
      return;
    }
  }

  // 标记已读
  async function readHandle (msgId) {
    try {
      await messageApi.updateReadFlag(msgId);
    } catch (e) {
      console.log(e);
    } finally {
    }
  }

  return {
    toHandle,
  };
}

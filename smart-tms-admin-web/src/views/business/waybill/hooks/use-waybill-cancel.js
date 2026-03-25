/*
 * @Description:
 * @Author: zhuoda
 * @Date: 2022-07-28
 * @LastEditTime: 2022-07-28
 * @LastEditors: zhuoda
 */
import { message, Modal } from 'ant-design-vue';
import { SmartLoading } from '/@/components/smart-loading';
import { waybillApi } from '/@/api/business/waybill/waybill-api';
// 撤销
function confirmCancel(waybillNumber, waybillId, callback) {
  Modal.confirm({
    title: '作废',
    content: `您确定要作废运单【${waybillNumber}】吗？`,
    okText: '作废',
    okType: 'danger',
    onOk() {
      cancel(waybillId, callback);
    },
    cancelText: '取消',
    onCancel() {},
  });
}

async function cancel(waybillId, callback) {
  try {
    SmartLoading.show();
    await waybillApi.cancel(waybillId);
    message.success('作废成功');
    if (callback) {
      callback();
    }
  } catch (e) {
    console.log(e);
  } finally {
    SmartLoading.hide();
  }
}

export function useWaybillCancel() {
  return {
    confirmCancel,
  };
}

import { message, Modal } from 'ant-design-vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import { orderApi } from '/@/api/business/order/order-api';

export function orderCancel () {
  function confirmCancel (orderId, callBack) {
    Modal.confirm({
      title: '提示',
      content: '确定要取消该订单吗？',
      okText: '确定',
      okType: 'danger',
      onOk () {
        cancelOrder(orderId, callBack);
      },
      cancelText: '取消',
      onCancel () {
      },
    });
  }

  async function cancelOrder (orderId, callBack) {
    try {
      useSpinStore().show();
      await orderApi.cancelOrder(orderId);
      message.success('取消成功');
      callBack();
    } catch (e) {
      console.log(e);
    } finally {
      useSpinStore().hide();
    }
  }

  return {
    confirmCancel
  };
}

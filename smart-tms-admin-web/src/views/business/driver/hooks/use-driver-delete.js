import { message, Modal } from 'ant-design-vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import { driverApi } from '/@/api/business/driver/driver-api';

export function useDriverDelete () {
  function confirmDelete (driverIdList, callBack) {
    Modal.confirm({
      title: `确认要删除吗？`,
      content: '删除后，该信息将不可恢复',
      okText: '删除',
      okType: 'danger',
      onOk () {
        deleteDriver(driverIdList, callBack);
      },
      cancelText: '取消',
      onCancel () {
      },
    });
  }

  async function deleteDriver (driverIdList, callBack) {
    try {
      useSpinStore().show();
      await driverApi.batchDeleteDriver(driverIdList);
      message.success('删除成功');
      callBack();
    } catch (e) {
      console.log(e);
    } finally {
      useSpinStore().hide();
    }
  }

  return {
    confirmDelete
  };
}

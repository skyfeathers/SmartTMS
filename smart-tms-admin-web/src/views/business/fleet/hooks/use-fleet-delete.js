import { message, Modal } from 'ant-design-vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import { fleetApi } from '/@/api/business/fleet/fleet-api';

export function useFleetDelete () {
  function confirmDelete (fleetIdList, callBack) {
    Modal.confirm({
      title: `确认要删除${fleetIdList.length}个车队吗？`,
      content: '删除后，该信息将不可恢复',
      okText: '删除',
      okType: 'danger',
      onOk () {
        deleteFleet(fleetIdList, callBack);
      },
      cancelText: '取消',
      onCancel () {
      },
    });
  }

  async function deleteFleet (fleetIdList, callBack) {
    try {
      useSpinStore().show();
      await fleetApi.batchDeleteFleet(fleetIdList);
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

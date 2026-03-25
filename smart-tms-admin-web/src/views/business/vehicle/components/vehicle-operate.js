/*
 * @Description:
 * @Author: zhuoda
 * @Date: 2022-07-11
 * @LastEditTime: 2022-07-11
 * @LastEditors: zhuoda
 */
import { vehicleApi } from '/@/api/business/vehicle/vehicle-api';
import { SmartLoading } from '/@/components/smart-loading';
import { message, Modal } from 'ant-design-vue';
import { useRoute, useRouter } from 'vue-router';

export function deleteVehicle(idList, callback) {
  Modal.confirm({
    title: '提示',
    content: `确认要删除${idList.length}个车辆吗？`,
    okText: '删除',
    okType: 'danger',
    onOk: () => {
      (async () => {
        try {
          SmartLoading.show();
          await vehicleApi.batchDelete(idList);
          message.success('删除成功');
          if (callback) {
            callback();
          }
        } catch (e) {
          console.log(e);
        } finally {
          SmartLoading.hide();
        }
      })();
    },
  });
}

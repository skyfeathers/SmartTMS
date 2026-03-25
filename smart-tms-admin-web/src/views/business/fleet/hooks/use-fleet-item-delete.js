import {message, Modal} from "ant-design-vue";
import {useSpinStore} from "/@/store/modules/system/spin";
import {fleetApi} from "/@/api/business/fleet/fleet-api";

export function useFleetItemDelete() {
    function confirmDelete(fleetItemId,callBack) {
        Modal.confirm({
            title: "确定要删除吗？",
            content: "删除后，该信息将不可恢复",
            okText: "删除",
            okType: "danger",
            onOk() {
                deleteFleetItem(fleetItemId,callBack);
            },
            cancelText: "取消",
            onCancel() {
            },
        });
    }

    async function deleteFleetItem(fleetItemId,callBack) {
        try {
            useSpinStore().show();
            await fleetApi.deleteFleetItem(fleetItemId);
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
    }
}

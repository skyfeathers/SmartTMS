<!--
 * @Description:
 * @version:
 * @Author: lidoudou
 * @Date: 2022-07-07 11:32:59
 * @LastEditors: lidoudou
 * @LastEditTime: 2022-07-07 16:23:47
-->
<template>
  <a-modal title="分配负责人" v-model:open="visible" :width="720" @cancel="onClose">
    <a-form
      :label-col="{ span: 5 }"
      :wrapper-col="{ span: 12 }"
    >
      <a-form-item label="负责人">
        <role-employee-select :systemConfigKey="SYSTEM_CONFIG_KEY_ENUM.SALES_ROLE_CODE.value"
          ref="employeeSelect"
          placeholder="请选择负责人"
          width="100%"
          v-model:value="managerIdList"
          :leaveFlag="false"
          mode="multiple"
        />
      </a-form-item>
    </a-form>
    <div
      :style="{
        position: 'absolute',
        right: 0,
        bottom: 0,
        width: '100%',
        borderTop: '1px solid #e9e9e9',
        padding: '10px 16px',
        background: '#fff',
        textAlign: 'right',
        zIndex: 1,
      }"
    >
      <a-button style="margin-right: 8px" @click="onClose">取消</a-button>
      <a-button type="primary" @click="onSubmit">提交</a-button>
    </div>
  </a-modal>
</template>
<script setup>
import { ref } from "vue";
import { message } from "ant-design-vue";
import { useSpinStore } from "/@/store/modules/system/spin";
import { shipperApi } from "/@/api/business/shipper/shipper-api";
import RoleEmployeeSelect from '/@/components/role-employee-select/index.vue';
import { SYSTEM_CONFIG_KEY_ENUM } from '/@/constants/system/system-config-const';
import _ from "lodash"
// ----------------------- 以下是字段定义 emits props ------------------------

const props = defineProps({
  shipperIdList: {
    type: Array,
    default: () => {
      return [];
    },
  },
});

// emit
const emit = defineEmits("reloadList");

// 是否展示弹窗
const visible = ref(false);
const managerIdList = ref([]);

// ----------------------- 以下是方法 ------------------------
function showModal () {
  managerIdList.value = [];
  visible.value = true;
}

function onClose() {
  visible.value = false;
}

async function onSubmit() {
  if (_.isEmpty(managerIdList.value)) {
    message.error("请选择负责人");
    return;
  }
  useSpinStore().show();
  try {
    let param = {
      shipperIdList: props.shipperIdList,
      managerIdList: managerIdList.value,
    };
    await shipperApi.updateManager(param);
    message.success("修改成功");
    onClose();
    emit("reloadList");
  } catch (error) {
    console.log(error);
  } finally {
    useSpinStore().hide();
  }
}

// ----------------------- 以下是暴露的方法内容 ------------------------
defineExpose({
  showModal,
});
</script>

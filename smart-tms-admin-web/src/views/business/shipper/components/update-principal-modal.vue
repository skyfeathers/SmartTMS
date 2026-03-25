<!--
 * @Description: 分配客服负责人，多选
 * @version:
 * @Author: lidoudou
 * @Date: 2022-07-07 14:35:43
 * @LastEditors: lidoudou
 * @LastEditTime: 2022-07-07 16:24:07
-->
<template>
  <a-modal :title="'分配'+$smartEnumPlugin.getDescByValue('PRINCIPAL_TYPE_ENUM', principalType)+'负责人'"
           v-model:open="visible" :width="720" @cancel="onClose">
    <a-form
      :label-col="{ span: 5 }"
      :wrapper-col="{ span: 12 }"
    >
      <a-form-item label="负责人">
        <role-employee-select
            :systemConfigKey="SYSTEM_CONFIG_KEY_ENUM.CUSTOMER_SERVICE_ROLE_CODE.value"
            ref="managerSelectRef"
            placeholder="请选择负责人"
            mode="multiple"
            v-model:value="managerIdList"
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
import { ref, onMounted ,computed} from "vue";
import { message } from "ant-design-vue";
import { useSpinStore } from "/@/store/modules/system/spin";
import _ from "lodash";
import { shipperApi } from "/@/api/business/shipper/shipper-api";
import { employeeApi } from '/@/api/system/employee/employee-api';
import { SYSTEM_CONFIG_KEY_ENUM } from '/@/constants/system/system-config-const';
import { PRINCIPAL_TYPE_ENUM } from '/@/constants/business/shipper-const';
import RoleEmployeeSelect from '/@/components/role-employee-select/index.vue';
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
let principalType = ref();
// =========== 业务逻辑 =============

function showModal(type) {
  managerIdList.value = [];
  principalType.value = type;
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
      employeeIdList: managerIdList.value,
      principalType: principalType.value
    };
    await shipperApi.updatePrincipal(param);
    message.success("修改成功");
    onClose();
    emit("reloadList");
  } catch (error) {
    console.log(error);
  } finally {
    useSpinStore().hide();
  }
}

//员工列表数据
const employeeList = ref([]);
async function query() {
  try {
    let resp = await employeeApi.queryAll();
    employeeList.value = resp.data;
  } catch (e) {
    console.log(e);
  } finally {
  }
}
onMounted(query);
// ----------------------- 以下是暴露的方法内容 ------------------------
defineExpose({
  showModal,
});
</script>

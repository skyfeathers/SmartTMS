<template>
  <a-modal width="600px" :open="visible" :title="form.etcId ? '编辑' : '添加'" ok-text="确认" cancel-text="取消"
           @ok="onSubmit" @cancel="onClose">
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 5 }" :wrapper-col="{ span: 19 }">
      <a-form-item label="ETC卡号" name="etcNo">
        <a-input v-model:value="form.etcNo" placeholder="请输入ETC卡号"/>
      </a-form-item>
      <a-form-item label="使用司机" name="userId">
        <DriverSelect width="100%" v-model:value="form.userId"/>
      </a-form-item>
      <a-form-item label="绑定车辆" name="useVehicleId">
        <VehicleSelect width="100%" placeholder="请选择车辆" v-model:value="form.useVehicleId"/>
      </a-form-item>
      <a-form-item label="ETC密码" name="password" v-if="!form.etcId">
        <a-input v-model:value="form.password" type="password" placeholder="请输入ETC密码"/>
      </a-form-item>
      <a-form-item label="状态" name="disabledFlag">
        <a-select v-model:value="form.disabledFlag" placeholder="请选择状态">
          <a-select-option :value="1">启用</a-select-option>
          <a-select-option :value="2">禁用</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="备注" name="remark">
        <a-textarea :rows="4" v-model:value="form.remark" type="textarea" placeholder="请输入备注"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script setup>
import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';
import SmartDictSelect from '/@/components/smart-dict-select/index.vue';
import VehicleSelect from '/@/components/vehicle-select/index.vue';
import DriverSelect from '/@/components/driver-select/index.vue';
import EnterpriseSelect from '/@/components/enterprise-select/index.vue';
import { ref, reactive } from 'vue';
import { message } from 'ant-design-vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import { etcApi } from '/@/api/business/card/etc-api';

// ----------------------- 以下是字段定义 emits props ------------------------
// emit
const emit = defineEmits(['reloadList']);

//  组件
const formRef = ref();

const formDefault = {
  disabledFlag: 1,
  etcNo: '',
  etcId: '',
  password: null,
  remark: '',
  useVehicleId: null,
  userId: null,
};
let form = reactive({ ...formDefault });
const rules = {
  etcNo: [{ required: true, message: '请输入ETC卡号' }],
  password: [{ required: true, message: '请输入ETC密码' }],
};
const defaultChecked = ref(false);
const enabledChecked = ref(true);
// 是否展示
const visible = ref(false);

// ----------------------- 以下是生命周期 ------------------------

// ----------------------- 以下是方法 ------------------------

function showModal (rowData) {
  Object.assign(form, formDefault);
  if (rowData) {
    rowData.disabledFlag = rowData.disabledFlag ? 2 : 1;
    Object.assign(form, rowData);
  }
  visible.value = true;
}

function onClose () {
  Object.assign(form, formDefault);
  enabledChecked.value = true;
  visible.value = false;
}

function onSubmit () {
  formRef.value
      .validate()
      .then(async () => {
        useSpinStore().show();
        try {
          form.disabledFlag = form.disabledFlag == 1 ? false : true;
          if (form.etcId) {
            await etcApi.updateEtc(form);
          } else {
            await etcApi.saveEtc(form);
          }
          message.success(`${form.etcId ? '修改' : '添加'}成功`);
          emit('reloadList');
          onClose();
        } catch (error) {
          console.log(error);
        } finally {
          useSpinStore().hide();
        }
      })
      .catch((error) => {
        console.log('error', error);
        message.error('参数验证错误，请仔细填写表单数据!');
      });
}

// ----------------------- 以下是暴露的方法内容 ------------------------
defineExpose({
  showModal,
});
</script>

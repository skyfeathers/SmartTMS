<template>
  <a-modal :open="visible" :title="form.locationId ? '编辑' : '添加'" ok-text="确认" cancel-text="取消" @ok="onSubmit"
           @cancel="onClose">
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 6 }" :wrapper-col="{ span: 15 }">
      <a-form-item label="位置名称" name="locationName">
        <a-input v-model:value="form.locationName" placeholder="请输入位置名称" style="width: 300px"/>
      </a-form-item>
      <a-form-item label="存放类型" name="type">
        <SmartDictSelect keyCode="LOCATION-TYPE" v-model:value="form.type"  style="width: 300px"/>
      </a-form-item>
      <a-form-item label="状态" name="disabledFlag">
        <a-switch v-model:checked="enabledChecked" @change="enabledCheckedChange" checked-children="启用"
                  un-checked-children="禁用"/>
      </a-form-item>
      <a-form-item label="备注" name="disabledFlag">
        <a-input v-model:value="form.remark" placeholder="请输入备注"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script setup>
import EnterpriseSelect from '/@/components/enterprise-select/index.vue';
import SmartDictSelect from '/@/components/smart-dict-select/index.vue';

import {ref, reactive} from 'vue';
import {message} from 'ant-design-vue';
import {useSpinStore} from '/@/store/modules/system/spin';
import { locationApi } from '/@/api/fixed-asset/location/location-api';

// ----------------------- 以下是字段定义 emits props ------------------------
// emit
const emit = defineEmits(['reloadList']);

//  组件
const formRef = ref();

const formDefault = {
  locationId: null,
  locationName: '',
  disabledFlag: false,
};
let form = reactive({...formDefault});
const rules = {
  locationName: [{required: true, message: '请输入位置名称'}],
  type: [{required: true, message: '请选择存放类型'}],
};
const defaultChecked = ref(false);
const enabledChecked = ref(true);
// 是否展示
const visible = ref(false);

// ----------------------- 以下是生命周期 ------------------------

// ----------------------- 以下是方法 ------------------------

function enabledCheckedChange(checked) {
  form.disabledFlag = !checked;
}

function showModal(rowData) {
  Object.assign(form, formDefault);
  if (rowData) {
    rowData.type = rowData.type[0].valueCode;
    Object.assign(form, rowData);
    enabledChecked.value = !rowData.disabledFlag;
  }
  visible.value = true;
}

function onClose() {
  formRef.value.resetFields();
  Object.assign(form, formDefault);
  enabledChecked.value = true;
  visible.value = false;
}

function onSubmit() {
  formRef.value
      .validate()
      .then(async () => {
        useSpinStore().show();
        try {
          if (form.locationId) {
            await locationApi.update(form);
          } else {
            await locationApi.create(form);
          }
          message.success(`${form.locationId ? '修改' : '添加'}成功`);
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

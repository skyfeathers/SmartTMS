<!--
 * @Description: file content
 * @Author: yandy
 * @Date: 2022-07-07 10:18:53
 * @LastEditors:
 * @LastEditTime: 2022-07-07 10:18:53
-->
<template>
  <a-modal :open="visible" :title="form.cabinetId ? '编辑' : '添加'" ok-text="确认" cancel-text="取消" @ok="onSubmit"
           @cancel="onClose">
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 8 }" :wrapper-col="{ span: 15 }">
      <a-form-item label="柜型名称" name="cabinetName">
        <a-input v-model:value="form.cabinetName" placeholder="请输入柜型名称"/>
      </a-form-item>
      <a-form-item label="柜型皮重（千克）" name="tare">
        <a-input-number v-model:value="form.tare" :min="0" :max="999999.99" :precision="2" style="width: 100%"/>
      </a-form-item>
      <a-form-item label="是否默认" name="remark">
        <a-switch v-model:checked="defaultChecked" @change="defaultCheckedChange"/>
      </a-form-item>
      <a-form-item label="柜型状态" name="disabledFlag">
        <a-switch v-model:checked="enabledChecked" @change="enabledCheckedChange"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script setup>
import {ref, reactive} from 'vue';
import {message} from 'ant-design-vue';
import {useSpinStore} from '/@/store/modules/system/spin';
import {cabinetApi} from '/@/api/business/business-material/cabinet-api';

// ----------------------- 以下是字段定义 emits props ------------------------
// emit
const emit = defineEmits(['reloadList']);

//  组件
const formRef = ref();

const formDefault = {
  cabinetId: undefined,
  cabinetName: '',
  tare: 0,
  defaultFlag: false,
  disabledFlag: false,
};
let form = reactive({...formDefault});
const rules = {
  cabinetName: [{required: true, message: '请输入柜型名称'}],
  tare: [{required: true, message: '请输入柜型皮重'}],
};
const defaultChecked = ref(false);
const enabledChecked = ref(true);
// 是否展示
const visible = ref(false);

// ----------------------- 以下是生命周期 ------------------------

// ----------------------- 以下是方法 ------------------------
function defaultCheckedChange(checked) {
  form.defaultFlag = checked;
}

function enabledCheckedChange(checked) {
  form.disabledFlag = !checked;
}

function showModal(rowData) {
  Object.assign(form, formDefault);
  if (rowData) {
    Object.assign(form, rowData);
    enabledChecked.value = !rowData.disabledFlag;
    defaultChecked.value = rowData.defaultFlag;
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
          if (form.cabinetId) {
            await cabinetApi.update(form);
          } else {
            await cabinetApi.create(form);
          }
          message.success(`${form.cabinetId ? '修改' : '添加'}成功`);
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

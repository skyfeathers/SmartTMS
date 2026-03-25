<!--
 * @Description: file content
 * @Author: yandy
 * @Date: 2022-07-07 10:18:53
 * @LastEditors:
 * @LastEditTime: 2022-07-07 10:18:53
-->
<template>
  <a-modal :open="visible" :title="form.businessTypeId ? '编辑' : '添加'" ok-text="确认" cancel-text="取消" @ok="onSubmit"
           @cancel="onClose">
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 5 }" :wrapper-col="{ span: 12 }">
      <a-form-item label="业务类型" name="tripType">
        <SmartEnumSelect v-model:value="form.tripType" enumName="TRANSPORT_MODE_ENUM" placeholder="业务类型" />
      </a-form-item>
      <a-form-item label="业务名称" name="businessTypeName">
        <a-input v-model:value="form.businessTypeName" placeholder="请输入业务名称"/>
      </a-form-item>
      <a-form-item label="业务代码" name="businessTypeCode">
        <a-input v-model:value="form.businessTypeCode" placeholder="请输入业务代码"/>
      </a-form-item>
      <a-form-item label="是否默认" name="remark">
        <a-switch v-model:checked="defaultChecked" @change="defaultCheckedChange"/>
      </a-form-item>
      <a-form-item label="是否启用" name="disabledFlag">
        <a-switch v-model:checked="enabledChecked" @change="enabledCheckedChange"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script setup>
import { ref, reactive } from 'vue';
import { message } from 'ant-design-vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import { businessTypeApi } from '/@/api/business/business-material/business-type-api';
import SmartEnumSelect from '/@/components/framework/smart-enum-select/index.vue';

// ----------------------- 以下是字段定义 emits props ------------------------
// emit
const emit = defineEmits(['reloadList']);

//  组件
const formRef = ref();

const formDefault = {
  businessTypeId: undefined,
  businessTypeName: '',
  businessTypeCode: '',
  defaultFlag: false,
  disabledFlag: false,
};
let form = reactive({...formDefault});
const rules = {
  tripType: [{required: true, message: '请选择业务类型'}],
  businessTypeName: [{required: true, message: '请输入业务名称'}],
  businessTypeCode: [{required: true, message: '请输入业务代码'}],
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
  Object.assign(form, formDefault);
  enabledChecked.value = true;
  defaultChecked.value = false;
  visible.value = false;
}

function onSubmit() {
  formRef.value
      .validate()
      .then(async () => {
        useSpinStore().show();
        try {
          if (form.businessTypeId) {
            await businessTypeApi.update(form);
          } else {
            await businessTypeApi.create(form);
          }
          message.success(`${form.businessTypeId ? '修改' : '添加'}成功`);
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

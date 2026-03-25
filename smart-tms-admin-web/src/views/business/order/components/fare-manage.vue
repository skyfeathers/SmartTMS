<template>
  <a-card title="运价信息" class="smart-margin-top10">
    <a-form ref="formRef" :model="form" :rules="rules" labelAlign="left">
      <a-descriptions size="small" bordered :column="{ lg: 2, md: 2, sm: 2, xl: 3, xs: 2, xxl: 5 }" :labelStyle="{width: '140px'}">
        <a-descriptions-item label="单趟应收运价(元/车)" class="required">
          <a-form-item name="singleTripReceiveAmount">
            <a-input v-model:value="form.singleTripReceiveAmount"  placeholder="请输入单趟应收运价"/>
          </a-form-item>
        </a-descriptions-item>
        <a-descriptions-item label="单趟应付运价(元/车)" class="required">
          <a-form-item name="singleTripFreightAmount">
            <a-input v-model:value="form.singleTripFreightAmount " placeholder="请输入单趟应付运价"/>
          </a-form-item>
        </a-descriptions-item>
        <a-descriptions-item />
      </a-descriptions>
    </a-form>
  </a-card>
</template>
<script setup>
import { reactive, ref, watch } from 'vue';
import { message } from 'ant-design-vue';


// ------- 新增、编辑联系人 start --------

const defaultForm = {
  singleTripReceiveAmount: null,
  singleTripFreightAmount: null,
};
let form = reactive({ ...defaultForm });

const rules = {
  singleTripReceiveAmount: [{ required: true, message: '请输入单趟应收运价' }],
  singleTripFreightAmount: [{ required: true, message: '请输入单趟应付运价' }],
};
const formRef = ref();

// 保存
function onSave () {
  formRef.value.validate()
}

// 重置
function resetForm() {
  formRef.value.resetFields();
}

// 获取运价信息
function getFareManage() {
  return form;
}



defineExpose({
  formRef,
  getFareManage,
  resetForm,
});
</script>

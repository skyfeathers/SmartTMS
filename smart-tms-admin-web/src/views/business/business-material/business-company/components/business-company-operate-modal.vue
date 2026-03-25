<!--
 * @Description: file content
 * @Author: yandy
 * @Date: 2022-07-07 10:18:53
 * @LastEditors:
 * @LastEditTime: 2022-07-07 10:18:53
-->
<template>
  <a-modal :open="visible" :title="form.companyId ? '编辑' : '添加'" ok-text="确认" cancel-text="取消" @ok="onSubmit"
           @cancel="onClose">
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 5 }" :wrapper-col="{ span: 12 }">
      <a-form-item label="公司名称" name="companyName">
        <a-input v-model:value="form.companyName" placeholder="请输入公司名称"/>
      </a-form-item>
      <a-form-item label="公司编号" name="companyCode">
        <a-input v-model:value="form.companyCode" placeholder="请输入公司编号"/>
      </a-form-item>
      <a-form-item label="联系人" name="contact">
        <a-input v-model:value="form.contact" placeholder="请输入联系人"/>
      </a-form-item>
      <a-form-item label="联系人电话" name="contactPhone">
        <a-input v-model:value="form.contactPhone" placeholder="请输入联系人电话"/>
      </a-form-item>
      <a-form-item label="是否启用" name="disabledFlag">
        <a-switch v-model:checked="enabledChecked" @change="enabledCheckedChange"/>
      </a-form-item>
      <a-form-item label="备注" name="remark">
        <a-textarea v-model:value="form.remark" :rows="2"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script setup>
import {ref, reactive} from 'vue';
import {message} from 'ant-design-vue';
import {useSpinStore} from '/@/store/modules/system/spin';
import {businessCompanyApi} from '/@/api/business/business-material/business-company-api';

// ----------------------- 以下是字段定义 emits props ------------------------
// emit
const emit = defineEmits(['reloadList']);

//  组件
const formRef = ref();

const formDefault = {
  companyId: undefined,
  companyName: '',
  companyCode: '',
  contact: '',
  contactPhone: '',
  disabledFlag: false,
  remark: ''
};
let form = reactive({...formDefault});
const rules = {
  companyName: [{required: true, message: '请输入公司名称'}],
  companyCode: [{required: true, message: '请输入公司编号'}],
  contact: [{required: true, message: '请输入联系人'}],
  contactPhone: [{required: true, message: '请输入联系人电话'}],
};
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
    Object.assign(form, rowData);
    enabledChecked.value = !rowData.disabledFlag;
  }
  visible.value = true;
}

function onClose() {
  Object.assign(form, formDefault);
  formRef.value.resetFields();
  enabledChecked.value = true;
  visible.value = false;
}

function onSubmit() {
  formRef.value
      .validate()
      .then(async () => {
        useSpinStore().show();
        try {
          if (form.companyId) {
            await businessCompanyApi.update(form);
          } else {
            await businessCompanyApi.create(form);
          }
          message.success(`${form.companyId ? '修改' : '添加'}成功`);
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

<!--
 * @Description: file content
 * @Author: yandy
 * @Date: 2022-07-07 10:18:53
 * @LastEditors:
 * @LastEditTime: 2022-07-07 10:18:53
-->
<template>
  <a-modal :open="visible" :title="form.bankId ? '编辑' : '添加'" ok-text="确认" cancel-text="取消" @ok="onSubmit"
           @cancel="onClose">
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 5 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="开户银行" name="bankName">
        <a-input v-model:value="form.bankName" placeholder="请输入开户银行"/>
      </a-form-item>
      <a-form-item label="账户名称" name="accountName">
        <a-input v-model:value="form.accountName" placeholder="请输入账户名称"/>
      </a-form-item>
      <a-form-item label="账号" name="accountNumber">
        <a-input v-model:value="form.accountNumber" placeholder="请输入账号"/>
      </a-form-item>
      <a-form-item label="是否对公" name="businessFlag">
        <a-switch v-model:checked="businessFlagChecked" @change="businessFlagCheckedChange"/>
      </a-form-item>
      <a-form-item label="启用状态" name="disabledFlag">
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
import {bankApi} from '/@/api/business/oa/bank-api';

// ----------------------- 以下是字段定义 emits props ------------------------
const props = defineProps({
  enterpriseId: {
    type: Number,
    default: null,
  }
});
// emit
const emit = defineEmits(['reloadList']);

//  组件
const formRef = ref();

const formDefault = {
  bankId: undefined,
  enterpriseId: undefined,
  bankName: '',
  accountName: '',
  accountNumber: '',
  businessFlag: false,
  disabledFlag: false,
  remark: '',
};
let form = reactive({...formDefault});
const rules = {
  bankName: [{required: true, message: '请输入开户银行'}],
  accountName: [{required: true, message: '请输入账户名称'}],
  accountNumber: [{required: true, message: '请输入账号'}],
};
// 是否展示
const visible = ref(false);
const businessFlagChecked = ref(false);
const enabledChecked = ref(true);
// ----------------------- 以下是生命周期 ------------------------

// ----------------------- 以下是方法 ------------------------
function businessFlagCheckedChange(checked) {
  form.businessFlag = checked;
}
function enabledCheckedChange(checked) {
  form.disabledFlag = !checked;
}

function showModal(rowData) {
  Object.assign(form, formDefault);
  if (rowData) {
    Object.assign(form, rowData);
    businessFlagChecked.value = rowData.businessFlag;
    enabledChecked.value = !rowData.disabledFlag;
  }
  form.enterpriseId = props.enterpriseId;
  visible.value = true;
}

function onClose() {
  Object.assign(form, formDefault);
  formRef.value.resetFields();
  visible.value = false;
}

function onSubmit() {
  formRef.value
      .validate()
      .then(async () => {
        useSpinStore().show();
        try {
          if (form.bankId) {
            await bankApi.update(form);
          } else {
            await bankApi.create(form);
          }
          message.success(`${form.bankId ? '修改' : '添加'}成功`);
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

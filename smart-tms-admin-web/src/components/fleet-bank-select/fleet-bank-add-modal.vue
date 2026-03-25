<!--
 * @Description: file content
 * @Author: yandy
 * @Date: 2022-07-07 10:18:53
 * @LastEditors:
 * @LastEditTime: 2022-07-07 10:18:53
-->
<template>
  <a-modal :open="visible" title="添加车队银行账户" ok-text="确认" cancel-text="取消" @ok="onSubmit"
           @cancel="onClose">
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 5 }" :wrapper-col="{ span: 12 }">
      <a-form-item label="银行类型" name="bankType">
        <SmartDictSelect keyCode="BANK-TYPE" v-model:value="form.bankType" width="100%" />
      </a-form-item>
      <a-form-item label="开户名" name="accountName">
        <a-input v-model:value="form.accountName" class="form-width" placeholder="请输入开户名" />
      </a-form-item>
      <a-form-item label="银行账号" name="bankAccount">
        <a-input v-model:value="form.bankAccount" class="form-width" placeholder="请输入银行账号" />
      </a-form-item>
      <a-form-item label="银行名称" name="bankName">
        <a-input v-model:value="form.bankName" class="form-width" placeholder="请输入银行名称" />
      </a-form-item>
      <a-form-item label="支行名称" name="bankBranchName">
        <a-input v-model:value="form.bankBranchName" class="form-width" placeholder="请输入支行名称" />
      </a-form-item>
      <a-form-item label="是否默认" name="defaultFlag">
        <a-radio-group v-model:value="form.defaultFlag" name="radioGroup">
          <a-radio :value="FLAG_NUMBER_ENUM.TRUE.value">是</a-radio>
          <a-radio :value="FLAG_NUMBER_ENUM.FALSE.value">否</a-radio>
        </a-radio-group>
      </a-form-item>
      <a-form-item label="附件信息" name="attachment">
        <Upload
            :default-file-list="form.attachment"
            :folder="FILE_FOLDER_TYPE_ENUM.DRIVER.value"
            :maxUploadSize="5"
            buttonText="点击上传附件"
            @change="bankAttachmentChange($event, index)"
        />
      </a-form-item>
    </a-form>

  </a-modal>
</template>
<script setup>
import {ref, reactive} from 'vue';
import Upload from '/@/components/upload/index.vue';
import SmartDictSelect from '/@/components/smart-dict-select/index.vue';

import {message} from 'ant-design-vue';
import {useSpinStore} from '/@/store/modules/system/spin';
import { fleetApi } from '/@/api/business/fleet/fleet-api';
import {FLAG_NUMBER_ENUM} from "/@/constants/common-const";
import { FILE_FOLDER_TYPE_ENUM } from '/@/constants/business/file-const';

// ----------------------- 以下是字段定义 emits props ------------------------
// emit
const emit = defineEmits(['reloadList']);

//  组件
const formRef = ref();

const formDefault = {
  fleetId: null,
  accountName: '',
  bankAccount: '',
  bankType: '',
  bankName: '',
  bankBranchName: '',
  attachment: undefined,
  defaultFlag: FLAG_NUMBER_ENUM.FALSE.value
};
let form = reactive({...formDefault});
const rules = {
  accountName: [{ required: true, message: '请输入开户名' }],
  bankAccount: [{ required: true, message: '请输入银行账号' }],
  // bankName: [{ required: true, message: '请输入银行名称' }],
  bankType: [{ required: true, message: '请选择银行类型' }],
  bankBranchName: [{ required: true, message: '请输入支行名称' }],
};

// 是否展示
const visible = ref(false);

// ----------------------- 以下是生命周期 ------------------------

// ----------------------- 以下是方法 ------------------------
function bankAttachmentChange(fileList, index) {
  form.attachment = fileList;
}

function showModal(fleetId) {
  Object.assign(form, formDefault);
  form.fleetId = fleetId;
  visible.value = true;
}

function onClose() {
  Object.assign(form, formDefault);
  visible.value = false;
}

function onSubmit() {
  formRef.value
      .validate()
      .then(async () => {
        useSpinStore().show();
        try {
          await fleetApi.bankAdd(form);
          message.success('添加成功');
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

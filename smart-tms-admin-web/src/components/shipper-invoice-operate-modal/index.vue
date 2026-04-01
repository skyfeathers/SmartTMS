<!--
 * @Description:
 * @version:
 * @Author: lidoudou
 * @Date: 2022-07-07 17:26:11
 * @LastEditors: lidoudou
 * @LastEditTime: 2022-07-08 15:08:10
-->
<template>
  <a-modal title="开票信息" v-model:open="visible" :width="720" @cancel="onClose">
    <a-form
        ref="formRef"
        :model="invoiceForm"
        :rules="rules"
        :label-col="{ span: 5 }"
        :wrapper-col="{ span: 12 }"
    >
      <a-form-item label="纳税人识别号" name="invoiceNo">
        <a-input
            v-model:value="invoiceForm.invoiceNo"
            placeholder="请输入纳税人识别号"
        />
      </a-form-item>
      <a-form-item label="开票抬头" name="invoiceName">
        <a-input
            v-model:value="invoiceForm.invoiceName"
            placeholder="请输入开票抬头"
        />
      </a-form-item>
      <a-form-item label="开票银行" name="invoiceBankName">
        <a-input v-model:value="invoiceForm.invoiceBankName" placeholder="请输入开票银行"/>
      </a-form-item>
      <a-form-item label="银行账号" name="invoiceBankAccount">
        <a-input v-model:value="invoiceForm.invoiceBankAccount" placeholder="请输入银行账号"/>
      </a-form-item>
      <a-form-item label="开户行号" name="invoiceBankNo">
        <a-input v-model:value="invoiceForm.invoiceBankNo" placeholder="请输入开户行号"/>
      </a-form-item>
      <a-form-item label="开户行地址" name="invoiceBankAddress">
        <a-input v-model:value="invoiceForm.invoiceBankAddress" placeholder="请输入开户行地址"/>
      </a-form-item>
      <a-form-item label="开票电话" name="invoicePhone">
        <a-input v-model:value="invoiceForm.invoicePhone" placeholder="请输入开票电话"/>
      </a-form-item>
      <a-form-item label="开票地址" name="invoiceAddress">
        <a-input v-model:value="invoiceForm.invoiceAddress" placeholder="请输入开票地址"/>
      </a-form-item>
      <a-form-item label="中征码" name="loanCardNo">
        <a-input v-model:value="invoiceForm.loanCardNo" placeholder="请输入中征码"/>
      </a-form-item>
      <a-form-item label="禁用状态" name="disableFlag">
        <a-select v-model:value="invoiceForm.disableFlag" placeholder="请选择状态"
                  :allowClear="false">
          <a-select-option :value="1">禁用</a-select-option>
          <a-select-option :value="2">启用</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="附件" name="remark">
        <Upload
            :maxUploadSize="3"
            buttonText="点击上传附件"
            :default-file-list="invoiceForm.attachment"
            @change="changeAttachment"
            :folder="FILE_FOLDER_TYPE_ENUM.SHIPPER.value"
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
import { reactive, ref } from 'vue';
import { message } from 'ant-design-vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import { FILE_FOLDER_TYPE_ENUM } from '/@/constants/business/file-const';
import DisabledFlagSelect from '/@/components/boolean-flag-select/index.vue';
import Upload from '/@/components/upload/index.vue';
import { regular } from '/@/constants/regular-const';

import _ from 'lodash';

const emits = defineEmits(['onSubmit']);

defineExpose({
  showModal,
});

// ------- 新增、编辑发票信息 start --------
const defaultForm = {
  attachment: [],
  invoiceAddress: '',
  invoiceBankAccount: '',
  invoiceBankAddress: '',
  invoiceBankName: '',
  invoiceBankNo: '',
  invoiceId: null,
  invoiceName: '',
  invoiceNo: '',
  invoicePhone: '',
  loanCardNo: '',
  index: null,
  disableFlag: 2,
};
let invoiceForm = reactive({ ...defaultForm });

const rules = {
  invoiceNo: [{ required: true, message: '请输入纳税人识别号' },
    { pattern: regular.taxpayerId, message: '纳税人识别号格式不正确', trigger: 'blur' }],
  invoicePhone: [{ pattern: regular.isLandlineOrPhone, message: '开票电话格式不正确', trigger: 'blur' }],
  invoiceName: [{ required: true, message: '请输入发票抬头' }],
};
const formRef = ref();

function changeAttachment (fileList) {
  invoiceForm.attachment = fileList;
}

// 保存开票信息
function onSubmit () {
  formRef.value
      .validate()
      .then(async () => {
        useSpinStore().show();
        try {
          emits('onSubmit', Object.assign({}, invoiceForm, {
            disableFlag: invoiceForm.disableFlag == 1,
          }));
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

// ----------- 弹窗展示、关闭
// 是否展示弹窗
const visible = ref(false);

// 展示修改开票信息弹窗
function showModal (index, invoiceInfo) {
  // 默认设置index
  if (!invoiceInfo) {
    Object.assign(invoiceForm, defaultForm);
  } else {
    // 判断invoiceInfo是否有disableFlag属性，有则转换为1或2
    if (_.has(invoiceInfo, 'disableFlag')) {
      invoiceInfo.disableFlag = invoiceInfo.disableFlag ? 1 : 2;
    }

    Object.assign(invoiceForm, invoiceInfo);
  }
  invoiceForm.index = index;
  visible.value = true;
}

function onClose () {
  Object.assign(invoiceForm, defaultForm);
  visible.value = false;
}
</script>

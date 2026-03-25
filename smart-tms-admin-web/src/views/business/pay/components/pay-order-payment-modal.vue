<!--
 * @Description:保险表单
 * @version:
 * @Author: zhuoda
 * @Date: 2021-09-01 20:58:51
 * @LastEditors: zhuoda
 * @LastEditTime: 2022-08-02
-->
<template>
  <a-modal :open="visible" title="付款" ok-text="保存" cancel-text="取消" @ok="onSubmit" @cancel="onClose">
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 6 }">
      <a-row>
        <a-col :span="24">
          <a-form-item label="应付金额"> {{ form.payAmount }} </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="付款银行账户" name="bankId">
            <EnterpriseBankSelect v-model:value="form.bankId" width="300px" />
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="本次付款金额" name="payAmount">
            <a-input-number v-model:value="form.payAmount" style="width: 300px" disabled=""/>
          </a-form-item>
        </a-col>

        <a-col :span="24">
          <a-form-item label="资金流水号" name="sequenceCode">
            <a-input v-model:value="form.sequenceCode" style="width: 300px" placeholder="请输入资金流水号" />
          </a-form-item>
        </a-col>

        <a-col :span="24">
          <a-form-item label="付款时间" name="payTime">
            <a-date-picker v-model:value="form.payTime" valueFormat="YYYY-MM-DD HH:mm:ss" :showTime="true" style="width: 300px" />
          </a-form-item>
        </a-col>

        <a-col :span="24">
          <a-form-item label="付款凭证">
            <Upload
              buttonText="点击上传付款凭证"
              :default-file-list="form.attachment"
              @change="changeAttachment"
              :folder="FILE_FOLDER_TYPE_ENUM.PAY_ORDER.value"
            />
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>
<script setup>
import { ref, reactive, nextTick } from 'vue';
import { message } from 'ant-design-vue';
import { SmartLoading } from '/@/components/smart-loading';
import _ from 'lodash';
import Upload from '/@/components/upload/index.vue';
import { payOrderApi } from '/@/api/business/pay/pay-order-api';
import { FILE_FOLDER_TYPE_ENUM } from '/@/constants/business/file-const';
import EnterpriseBankSelect from '/@/components/enterprise-bank-select/index.vue';

// ----------------------- 对外暴露 ------------------------
const emit = defineEmits('reloadList');
defineExpose({
  showModal,
});

// ----------------------- 表单 ------------------------
const formDefault = {
  payOrderIdList: null,
  payAmount: null,
  bankId: null,
  sequenceCode: null,
  attachment: null,
  payTime: undefined,
};
let form = reactive({ ...formDefault });
const rules = {
  bankId: [{ required: true, message: '请选择付款银行账户' }],
  payTime: [{ required: true, message: '请选择付款时间' }],
};

// ----------------------- 显示/隐藏 ------------------------
const visible = ref(false);
function onClose() {
  Object.assign(form, formDefault);
  visible.value = false;
}

function showModal(payOrderList) {
  console.log(payOrderList);

  form.payOrderIdList = payOrderList.map(e=>e.payOrderId);

  let payableAmountArray = payOrderList.map(e=>e.payableTotalAmount);
  let payableTotalAmount = payableAmountArray.reduce((val, oldVal) => val + oldVal);

  form.payAmount = payableTotalAmount;
  form.attachment = null;
  form.bankId = null;
  form.payTime = null;
  visible.value = true;
  nextTick(() => {
    formRef.value.clearValidate();
  });
}

function changeAttachment(fileList) {
  form.attachment = fileList;
}
// ----------------------- 提交表单 ------------------------
const formRef = ref();
function onSubmit() {
  formRef.value
    .validate()
    .then(async () => {
      SmartLoading.show();
      try {
        let param = Object.assign({}, form);
        await payOrderApi.batchPay(param);
        message.success('操作成功');
        emit('reloadList');
        onClose();
      } catch (error) {
        console.log(error);
      } finally {
        SmartLoading.hide();
      }
    })
    .catch((error) => {
      console.log('error', error);
      message.error('参数验证错误，请仔细填写表单数据!');
    });
}
</script>

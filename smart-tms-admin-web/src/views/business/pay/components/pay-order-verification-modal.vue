<!--
 * @Description:结清和核销
 * @version:
 * @Author: zhuoda
 * @Date: 2021-09-01 20:58:51
 * @LastEditors: zhuoda
 * @LastEditTime: 2022-07-22
-->
<template>
  <a-modal
    :open="visible"
    title="核销"
    ok-text="保存"
    cancel-text="取消"
    @ok="onSubmit"
    @cancel="onClose"
  >
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 6 }">
      <a-row>
        <a-col :span="24">
          <a-form-item label="相关凭证" name="attachment">
            <Upload v-model:value="form.attachment" />
          </a-form-item>
        </a-col>

        <a-col :span="24">
          <a-form-item label="备注">
            <a-textarea v-model:value="form.remark" />
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
import Upload from '/@/components/upload/index.vue';
import { payOrderApi } from '/@/api/business/pay/pay-order-api';
// ----------------------- 对外暴露 ------------------------
const emit = defineEmits('reloadList');
defineExpose({
  showModal,
});

// ----------------------- 显示/隐藏 ------------------------
const visible = ref(false);
function onClose() {
  Object.assign(form, formDefault);
  visible.value = false;
}

function showModal(payOrderList) {
  form.payOrderIdList = payOrderList.map(e=>e.payOrderId);
  form.remark = null;
  form.attachment = null;
  visible.value = true;
  nextTick(() => {
    formRef.value.clearValidate();
  });
}

// ----------------------- 表单 ------------------------
const formDefault = {
  payOrderIdList: null,
  remark: null,
  attachment: null,
};
let form = reactive({ ...formDefault });
const rules = {};

// ----------------------- 提交表单 ------------------------
const formRef = ref();
function onSubmit() {
  formRef.value
    .validate()
    .then(async () => {
      SmartLoading.show();
      try {
        await payOrderApi.batchVerification(form);
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

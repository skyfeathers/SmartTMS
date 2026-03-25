<template>
  <a-modal
      :getContainer="getContainer"
      :open="visible"
      cancel-text="取消"
      ok-text="保存"
      title="充值"
      @cancel="onClose" @ok="onSubmit"
  >
    <a-form ref="formRef" :label-col="{ span: 6 }" :model="form" :rules="rules">
      <a-row>
        <a-col :span="24">
          <a-form-item label="充值时间" name="rechargeTime">
            <a-date-picker v-model:value="form.rechargeTime" :showTime="true" placeholder="请选择充值时间" style="width: 300px"
                           valueFormat="YYYY-MM-DD HH:mm:ss"/>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="充值凭证" name="attachment">
            <Upload
                :default-file-list="form.attachment"
                :folder="FILE_FOLDER_TYPE_ENUM.WAYBILL_VOUCHER.value"
                :maxUploadSize="3"
                accept=".jpg,.jpeg,.png,.gif"
                buttonText="点击上传充值凭证"
                listType="picture-card"
                @change="change"
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
import Upload from '/@/components/upload/index.vue';
import { payOrderApi } from '/@/api/business/pay/pay-order-api';
import { FILE_FOLDER_TYPE_ENUM } from '/@/constants/business/file-const';
import { getContainer } from '/@/utils/container-util';
// ----------------------- 对外暴露 ------------------------
const emit = defineEmits('reloadList');
defineExpose({
  showModal,
});

// ----------------------- 显示/隐藏 ------------------------
const visible = ref(false);

function onClose () {
  Object.assign(form, formDefault);
  visible.value = false;
}

function showModal (payOrderId) {
  form.payOrderId = payOrderId;
  visible.value = true;
  nextTick(() => {
    formRef.value.clearValidate();
  });
}

// ----------------------- 表单 ------------------------
const formDefault = {
  payOrderId: null,
  attachment: null,
};
let form = reactive({ ...formDefault });
const rules = {
  attachment: [{ required: true, message: '充值凭证不能为空' }],
  rechargeTime: [{ required: true, message: '请选择充值时间' }],
};

function change (fileList) {
  form.attachment = fileList;
  formRef.value.validateFields('attachment')
}

// ----------------------- 提交表单 ------------------------
const formRef = ref();

function onSubmit () {
  formRef.value
      .validate()
      .then(async () => {
        SmartLoading.show();
        try {
          await payOrderApi.oilCardRecharge(form);
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

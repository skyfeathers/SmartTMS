<template>
  <a-modal :width="700" :open="visible" title="上传回单" ok-text="保存" cancel-text="取消" @ok="onSubmit" @cancel="onClose"
           :getContainer="getContainer">
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 6 }">
      <a-form-item label="回单" name="receiptAttachment">
        <Upload
            :default-file-list="form.receiptAttachment"
            :folder="FILE_FOLDER_TYPE_ENUM.WAYBILL_VOUCHER.value"
            :maxUploadSize="15"
            listType="picture-card"
            buttonText="上传回单"
            @change="changeAttachment"
        />
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script setup>
import { ref, nextTick, reactive } from 'vue';
import _ from 'lodash';
import { waybillApi } from '/@/api/business/waybill/waybill-api';
import { SmartLoading } from '/@/components/smart-loading';
import { message } from 'ant-design-vue';
import { FILE_FOLDER_TYPE_ENUM } from '/@/constants/business/file-const';
import Upload from '/@/components/upload/index.vue';
import { getContainer } from '/@/utils/container-util';
// ----------------------- 显示/隐藏 ------------------------
const visible = ref(false);

function onClose () {
  Object.assign(form, formDefault);
  visible.value = false;
}

function showModal (waybillId, receiptAttachment) {
  Object.assign(form, formDefault, { waybillId, receiptAttachment });
  visible.value = true;
}

// ----------------------- 表单 ------------------------
const formDefault = {
  waybillId: null,
  receiptAttachment: null,
};
let form = reactive({ ...formDefault });
const rules = {
  receiptAttachment: [{ required: true, message: '请上传回单' }],
};

function changeAttachment (e) {
  form.receiptAttachment = e;
  formRef.value.validateFields('receiptAttachment');
}

// ----------------------- 提交表单 ------------------------
const emit = defineEmits('reloadList');
const formRef = ref();

function onSubmit () {
  formRef.value
      .validate()
      .then(async () => {
        SmartLoading.show();
        try {
          let param = _.cloneDeep(form);
          await waybillApi.uploadReceipt(param);
          message.success('上传成功');
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

defineExpose({
  showModal
});
</script>

<template>
  <a-modal :getContainer="getContainer" :open="visible" :width="500" cancel-text="取消" ok-text="确认" title="上传对账单"
           @cancel="onClose" @ok="onSubmit">
    <a-form ref="formRef" :model="form" class="smart-form" labelAlign="right">
      <!---基础信息-->
      <a-card size="small">
        <a-form-item label="上传对账单" name="invoiceAttachment">
          <Upload
              :default-file-list="form.billAttachment"
              :folder="FILE_FOLDER_TYPE_ENUM.WAYBILL_VOUCHER.value"
              :maxUploadSize="15"
              buttonText="点击上传对账单"
              listType="picture-card"
              @change="uploadBillAttachment"
          />
        </a-form-item>
      </a-card>
    </a-form>
  </a-modal>
</template>
<script setup>
import { ref, reactive } from 'vue';

import Upload from '/@/components/upload/index.vue';
import { FILE_FOLDER_TYPE_ENUM } from '/@/constants/business/file-const';
import { SmartLoading } from '/@/components/smart-loading';
import { message } from 'ant-design-vue';
import _ from 'lodash';
import { receiveOrderApi } from '/@/api/business/receive-order/receive-order-api';
import { getContainer } from '/@/utils/container-util';

const visible = ref(false);
const formDefault = {
  receiveOrderId: null,
  billAttachment: null
};
const formRef = ref();
const form = reactive({ ...formDefault });

// ------------------ 货物列表

function showModal (receiveOrderId) {
  form.receiveOrderId = receiveOrderId;
  visible.value = true;
}

// ------- 文件上传 start
// 上传对账单
function uploadBillAttachment (fileList) {
  form.billAttachment = fileList;
}

// 提交确认对账
async function onSubmit () {
  SmartLoading.show();
  try {
    let params = _.cloneDeep(form);
    await receiveOrderApi.uploadBill(params);
    message.success('操作成功');
    onClose();
    emit('refresh');
  } catch (error) {
    console.log(error);
  } finally {
    SmartLoading.hide();
  }
}

function onClose () {
  Object.assign(form, formDefault);
  visible.value = false;
}

const emit = defineEmits('refresh');
defineExpose({
  showModal
});
</script>

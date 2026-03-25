<template>
  <a-modal :width="500" :open="visible" title="确认核算" ok-text="确认" cancel-text="取消" @ok="onSubmit"
           @cancel="onClose">
    <a-form class="smart-form" labelAlign="right" ref="formRef" :model="form" :rules="rules">
      <!---基础信息-->
      <a-card size="small">
        <a-form-item label="核算日期" name="checkTime">
          <a-date-picker v-model:value="form.checkTime" valueFormat="YYYY-MM-DD" style="width: 100%"
                         placeholder="请输入核算日期" />
        </a-form-item>
        <a-form-item label="核算凭证" name="checkAttachment">
          <Upload
              accept=".jpg,.jpeg,.png,.gif"
              listType="picture-card"
              :maxUploadSize="1"
              buttonText="点击上传核算凭证"
              @change="change"
              :default-file-list="form.checkAttachment"
              :folder="FILE_FOLDER_TYPE_ENUM.SHIPPER.value"
          />
        </a-form-item>
      </a-card>
    </a-form>
  </a-modal>
</template>
<script setup>
import { ref, computed, reactive } from 'vue';

import Upload from '/@/components/upload/index.vue';
import { FILE_FOLDER_TYPE_ENUM } from '/@/constants/business/file-const';
import { SmartLoading } from '/@/components/smart-loading';
import { message } from 'ant-design-vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import _ from 'lodash';
import { receiveOrderApi } from '/@/api/business/receive-order/receive-order-api';

const visible = ref(false);
const formDefault = {
  receiveOrderId: null,
  checkTime: null,
  checkAttachment: null,
};
const formRef = ref();
const form = reactive({ ...formDefault });

const rules = {
  checkTime: [{ required: true, message: '请选择核算日期' }],
};

// ------------------ 货物列表

function showModal (receiveOrderId) {
  Object.assign(form, formDefault);
  form.receiveOrderId = receiveOrderId;
  visible.value = true;
}

// ------- 文件上传 start
function change (fileList) {
  form.checkAttachment = fileList;
}

// 提交确认对账
async function onSubmit () {
  formRef.value
      .validate()
      .then(async () => {
        SmartLoading.show();
        try {
          let params = _.cloneDeep(form);
          await receiveOrderApi.checkReceiveOrder(params);
          message.success('已确认对账')
          onClose();
          emit('refresh');
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

function onClose () {
  visible.value = false;
}

const emit = defineEmits('refresh');
defineExpose({
  showModal
});
</script>

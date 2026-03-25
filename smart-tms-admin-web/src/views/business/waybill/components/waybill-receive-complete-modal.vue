<template>
  <a-modal :open="visible" cancel-text="取消" ok-text="保存" title="运输完成" @cancel="onClose" @ok="onSubmit">
    <a-form ref="formRef" :label-col="{ span: 6 }" :model="form" :rules="rules">
      <a-row>
        <a-col :span="24">
          <a-form-item label="落重/还空时间" name="completeTime">
            <a-date-picker v-model:value="form.completeTime" :showTime="true" style="width: 300px" valueFormat="YYYY-MM-DD HH:mm:ss" />
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>
<script setup>
import { ref, reactive, nextTick } from 'vue';
import { message } from 'ant-design-vue';
import { waybillApi } from '/@/api/business/waybill/waybill-api';
import { SmartLoading } from '/@/components/smart-loading';
import dayjs from "dayjs";

// ----------------------- 显示/隐藏 ------------------------
const visible = ref(false);
function onClose() {
  Object.assign(form, formDefault);
  visible.value = false;
}

function showModal(waybillIdList) {
  form.waybillIdList = waybillIdList;
  visible.value = true;
  nextTick(() => {
    formRef.value.clearValidate();
  });
}

// ----------------------- 表单 ------------------------
const formDefault = {
  waybillIdList: [],
  completeTime: dayjs().format('YYYY-MM-DD HH:mm:ss')
};
let form = reactive({ ...formDefault });
const rules = {
  completeTime: [{ required: true, message: '请填写完成时间' }],
};

// ----------------------- 提交表单 ------------------------
const emit = defineEmits('reloadList');


const formRef = ref();
function onSubmit() {
  formRef.value
    .validate()
    .then(async () => {
      SmartLoading.show();
      try {
        console.log(form)
        await waybillApi.complete(form);
        message.success('更新成功');
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
  showModal,
});
</script>

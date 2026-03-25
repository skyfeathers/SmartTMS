<template>
  <a-modal :open="visible" title="作废" ok-text="作废" cancel-text="取消" @ok="onSubmit" @cancel="onClose">
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 5 }">

      <a-form-item label="备注" name="remark">
        <a-textarea v-model:value="form.remark" :rows="2"></a-textarea>
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script setup>
import { message } from 'ant-design-vue';
import { reactive, ref } from 'vue';
import { payOrderApi } from '/@/api/business/pay/pay-order-api';
import { useSpinStore } from '/@/store/modules/system/spin';

// ----------------------- 以下是字段定义 emits props ------------------------
// emit
const emit = defineEmits('reloadList');

//  组件
const formRef = ref();

const formDefault = {
  payOrderId: undefined,
  remark: '',
};
let form = reactive({ ...formDefault });
const rules = {

};
// 是否展示
const visible = ref(false);

// ----------------------- 以下是生命周期 ------------------------

// ----------------------- 以下是方法 ------------------------
function showModal(payOrderId) {
  Object.assign(form, formDefault);
  form.payOrderId = payOrderId;
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
        await payOrderApi.cancelPayOrder(form);
        message.success("作废成功");
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

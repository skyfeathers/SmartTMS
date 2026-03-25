
<!-- 分配副卡 -->
<template>
  <a-modal :open="visible" cancel-text="取消" ok-text="确认" title="分配副卡" width="600px" @cancel="onClose" @ok="onSubmit">
    <a-form ref="formRef" :label-col="{ span: 5 }" :model="form" :rules="rules" :wrapper-col="{ span: 19 }">
      <a-form-item label="主卡卡号" name="oilCardNo">
        {{form.masterOilCardNo}}
      </a-form-item>
      <a-form-item label="分配金额" name="changeAmount">
        <a-input-number v-model:value="form.changeAmount" :max="999999.99" :min="0" :precision="2"
          placeholder="请输入分配金额" style="width: 100% !important" />
      </a-form-item>
      <a-form-item label="备注" name="remark">
        <a-textarea v-model:value="form.remark" :rows="4" placeholder="请输入备注" type="textarea" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script setup>
import { ref, reactive } from 'vue';
import { message } from 'ant-design-vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import { oilApi } from '/@/api/business/card/oil-api';

// ----------------------- 以下是字段定义 emits props ------------------------
// emit
const emit = defineEmits(['reloadList']);

//  组件
const formRef = ref();

const formDefault = {
  oilCardId: null,
  changeAmount: null,
  remark: '',
};
let form = reactive({ ...formDefault });
const rules = {
  changeAmount: [{ required: true, message: '请输入金额' }],
};
const defaultChecked = ref(false);
const enabledChecked = ref(true);
// 是否展示
const visible = ref(false);

// ----------------------- 以下是生命周期 ------------------------


function showModal(rowData) {
  Object.assign(form, formDefault);
  if (rowData) {
    const { masterOilCardNo, oilCardId } = rowData;
    Object.assign(form, { masterOilCardNo, oilCardId });
  }
  visible.value = true;
}

function onClose() {
  Object.assign(form, formDefault);
  enabledChecked.value = true;
  visible.value = false;
}

function onSubmit() {
  formRef.value
    .validate()
    .then(async () => {
      useSpinStore().show();
      try {
        await oilApi.distributeSlaveCard({
          oilCardId: form.oilCardId,
          changeAmount: form.changeAmount,
          remark: form.remark,
        });
        message.success('圈回成功');
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

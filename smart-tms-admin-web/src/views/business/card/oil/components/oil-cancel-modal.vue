<template>
  <a-modal width="600px" :open="visible" title="挂失圈回" ok-text="确认" cancel-text="取消" @ok="onSubmit" @cancel="onClose">
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 5 }" :wrapper-col="{ span: 19 }">
      <a-form-item label="挂失卡号" name="oilCardNo">
        {{form.oilCardNo}}
      </a-form-item>
      <a-form-item label="圈回金额" name="changeAmount">
        <a-input-number style="width: 100% !important" v-model:value="form.changeAmount" :min="0" :max="999999.99"
          :precision="2" placeholder="请输入圈回金额" />
      </a-form-item>
      <a-form-item label="备注" name="remark">
        <a-textarea :rows="4" v-model:value="form.remark" type="textarea" placeholder="请输入备注" />
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
    const { oilCardNo, oilCardId } = rowData;
    Object.assign(form, { oilCardNo, oilCardId });
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
        await oilApi.cancelOil({
          oilCardId: form.oilCardId,
          changeAmount: form.changeAmount,
          remark: form.remark,
        });
        message.success('挂失圈回成功');
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

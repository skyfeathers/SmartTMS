<template>
  <a-modal :open="visible" cancel-text="取消" ok-text="确认" :title="updateFlagRef ? '更新计划充值金额' : '设置计划充值金额'" width="600px" @cancel="onClose"
           @ok="onSubmit">
    <a-form ref="formRef" :label-col="{ span: 5 }" :model="form" :rules="rules" :wrapper-col="{ span: 19 }">
      <a-form-item label="油卡卡号" name="oilCardNo">
        {{ form.oilCardNo }}
      </a-form-item>
      <a-form-item label="计划充值金额" name="preRechargeAmount">
        <a-input-number v-model:value="form.preRechargeAmount" :max="999999.99" :min="0" :precision="2"
                        placeholder="请输入计划充值金额" style="width: 100% !important"/>
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

// ----------------------- 弹窗展示关闭 ------------------------
// 是否展示
const visible = ref(false);
let updateFlagRef =  ref(false);;
function showModal (rowData, updateFlag) {
  updateFlagRef.value = updateFlag;
  Object.assign(form, formDefault);
  if (rowData) {
    const { oilCardNo, oilCardId } = rowData;
    Object.assign(form, { oilCardNo, oilCardId });
  }
  visible.value = true;
}

function onClose () {
  Object.assign(form, formDefault);
  visible.value = false;
}

// ----------------------- 表单操作 ------------------------
//  组件
const formRef = ref();

const formDefault = {
  oilCardId: null,
  oilCardNo: null,
  preRechargeAmount: null,
};
let form = reactive({ ...formDefault });
const rules = {
  oilCardNo: [{ required: true, message: '请输入油卡卡号' }],
  preRechargeAmount: [{ required: true, message: '请输入充值金额' }],
};

function onSubmit () {
  formRef.value
      .validate()
      .then(async () => {
        useSpinStore().show();
        try {
          let params = {
            oilCardId: form.oilCardId,
            preRechargeAmount: form.preRechargeAmount,
          };
          if (updateFlagRef.value) {
            await oilApi.updatePreRechargeAmount(params);
          }else {
            await oilApi.setPreRechargeAmount(params);
          }
          message.success('录入成功');
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

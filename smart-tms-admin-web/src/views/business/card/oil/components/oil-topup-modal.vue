<template>
  <a-modal width="600px" :open="visible" title="充值" ok-text="确认" cancel-text="取消" @ok="onSubmit" @cancel="onClose">
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 5 }" :wrapper-col="{ span: 19 }">
      <a-form-item label="油卡卡号" name="oilCardNo">
        {{ form.oilCardNo }}
      </a-form-item>
      <a-form-item label="充值金额" name="changeAmount">
        <a-input-number style="width: 100% !important" v-model:value="form.changeAmount" :min="0" :max="999999.99"
                        :precision="2" placeholder="请输入充值金额"/>
      </a-form-item>
      <a-form-item label="充值时间" name="transactionTime">
        <a-date-picker v-model:value="form.transactionTime" :showTime="true" format="YYYY-MM-DD HH:mm:ss"
                       style="width: 300px" valueFormat="YYYY-MM-DD HH:mm:ss"/>
      </a-form-item>

      <a-form-item label="备注" name="remark">
        <a-textarea :rows="4" v-model:value="form.remark" type="textarea" placeholder="请输入备注"/>
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
let rechargeFlag = false;
function showModal (rowData, changeAmount, customRechargeFlag) {
  rechargeFlag = customRechargeFlag;
  Object.assign(form, formDefault, { changeAmount});
  if (rowData) {
    const { oilCardNo, oilCardId } = rowData;
    Object.assign(form, { oilCardNo, oilCardId});
  }
  visible.value = true;
}

function onClose () {
  Object.assign(form, formDefault);
  formRef.value.resetFields();
  visible.value = false;
}

// ----------------------- 表单操作 ------------------------
//  组件
const formRef = ref();

const formDefault = {
  oilCardId: null,
  changeAmount: null,
  rechargeUserName: '',
  rechargeTime: '',
  remark: '',
  transactionTime: null
};
let form = reactive({ ...formDefault });
const rules = {
  oilCardNo: [{ required: true, message: '请输入油卡卡号' }],
  changeAmount: [{ required: true, message: '请输入充值金额' }],
  transactionTime: [{ required: true, message: '充值时间不能为空' }],
};

function onSubmit () {
  formRef.value
      .validate()
      .then(async () => {
        useSpinStore().show();
        try {
          console.log(rechargeFlag)
          if (rechargeFlag) {
            await oilApi.rechargeOil({
              oilCardId: form.oilCardId,
              changeAmount: form.changeAmount,
              remark: form.remark,
              transactionTime: form.transactionTime
            });
          } else {
            await oilApi.rechargeByPreRechargeAmount({
              oilCardId: form.oilCardId,
              changeAmount: form.changeAmount,
              remark: form.remark,
              transactionTime: form.transactionTime
            });
          }
          message.success('充值成功');
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

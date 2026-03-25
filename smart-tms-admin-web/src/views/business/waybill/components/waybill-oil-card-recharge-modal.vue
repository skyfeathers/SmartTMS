<template>
  <a-modal :open="visible" title="提交油卡充值" ok-text="保存" cancel-text="取消" @ok="onSubmit" @cancel="onClose"
           :getContainer="getContainer">
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 6 }">
      <a-form-item label="司机">
        {{ waybill.driverName }}
      </a-form-item>
      <a-form-item label="车辆">
        {{ waybill.vehicleNumber }}
      </a-form-item>
      <a-form-item label="油卡" name="oilCardId">
        <OilCardSelect :type="OIL_CARD_TYPE_ENUM.SLAVE.value" width="200px" v-model:value="form.oilCardId" :disabledFlag="false"
                       @change="oilCardSelectChange"/>
      </a-form-item>
<!--      <a-form-item label="油卡所属公司">
        {{oilCardEnterpriseName}}
      </a-form-item>-->
      <a-form-item label="充值金额" name="amount">
        <a-input-number v-model:value="form.amount" :precision="4" :min="0" :max="form.maxAmount" placeholder="充值金额" style="width: 200px"/>
      </a-form-item>
      <a-form-item label="备注" name="remark">
        <a-textarea :rows="3" v-model:value="form.remark" style="width: 200px" type="textarea" placeholder="请输入备注"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script setup>
import { ref, reactive, nextTick, computed } from 'vue';
import OilCardSelect from '/@/components/oil-card-select/index.vue';
import { OIL_CARD_TYPE_ENUM } from '/@/constants/business/card-const';
import { message } from 'ant-design-vue';
import { waybillApi } from '/@/api/business/waybill/waybill-api';
import { SmartLoading } from '/@/components/smart-loading';
import { getContainer } from '/@/utils/container-util';

// ----------------------- 对外暴露 ------------------------
const emit = defineEmits('reloadList');
defineExpose({
  showModal,
});


const formDefault = {
  waybillId: null,
  oilCardId: null,
  amount: 0,
  remark: null
};

const form = reactive({...formDefault});
const formRef = ref();

// ----------------------- 表单 ------------------------
const rules = {
  oilCardId: [{required: true, message: '请选择油卡'}],
  amount: [{required: true, message: '请输入运单合同金额'}],
};

// ----------------------- 显示/隐藏 ------------------------
const visible = ref(false);

function onClose() {
  Object.assign(form, formDefault);
  visible.value = false;
}

const waybill = ref({});

function showModal(rowData) {
  waybill.value = rowData;
  form.waybillId = rowData.waybillId
  getMaxRechargeAmount();
  visible.value = true;
  nextTick(() => {
    formRef.value.clearValidate();
  });
}

// 油卡企业
let oilCardEnterpriseName = ref('');

function oilCardSelectChange(oilCardId, selected) {
  if (selected) {
    oilCardEnterpriseName.value = selected.enterpriseName;
  } else {
    oilCardEnterpriseName.value = '';
  }
}

async function getMaxRechargeAmount () {
  try {
    let result = await waybillApi.getMaxRechargeAmount(form.waybillId);
    form.amount = result.data;
    form.maxAmount = result.data;
  } catch (error) {
    console.log(error);
  } finally {
    SmartLoading.hide();
  }
}

// ----------------------- 提交表单 ------------------------
function onSubmit() {
  formRef.value
      .validate()
      .then(async () => {
        SmartLoading.show();
        try {
          await waybillApi.oilCardRecharge(form);
          message.success('提交充值申请成功');
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

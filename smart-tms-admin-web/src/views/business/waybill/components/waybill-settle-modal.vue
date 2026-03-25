<!--
 * @Description:运单结算
 * @version:
 * @Author: zhuoda
 * @Date: 2021-09-01 20:58:51
 * @LastEditors: zhuoda
 * @LastEditTime: 2022-08-02
-->
<template>
  <a-modal width="750px" :open="visible" :title="title" ok-text="提交" cancel-text="取消" @ok="onSubmit"
           @cancel="onClose" :getContainer="getContainer">
    <a-descriptions :column="3" size="small" v-if="$lodash.isEmpty(costSettleDetail.splitTransportList)">
      <a-descriptions-item label="结算类型">
        {{ $smartEnumPlugin.getDescByValue('SETTLE_TYPE_ENUM', costSettleDetail.settleType) }}
      </a-descriptions-item>
      <a-descriptions-item label="结算对象">{{ costSettleDetail.settleObjectName }}</a-descriptions-item>
      <a-descriptions-item label="联系电话">{{ costSettleDetail.settleObjectPhone }}</a-descriptions-item>
    </a-descriptions>

    <a-descriptions :column="3" size="small" v-else>
      <a-descriptions-item label="结算类型">
        {{ $smartEnumPlugin.getDescByValue('SETTLE_TYPE_ENUM', costSettleDetail.settleType) }}
        <a-descriptions-item v-if="SETTLE_TYPE_ENUM.FLEET.value == costSettleDetail.settleType" label="结算对象">{{ costSettleDetail.settleObjectName }}</a-descriptions-item>
        <a-descriptions-item v-if="SETTLE_TYPE_ENUM.FLEET.value == costSettleDetail.settleType" label="联系电话">{{ costSettleDetail.settleObjectPhone }}</a-descriptions-item>
      </a-descriptions-item>
    </a-descriptions>



    <template v-if="form.payOrderType === PAY_ORDER_TYPE_ENUM.CASH.value">

      <a-form-item label="收款银行" v-if="SETTLE_TYPE_ENUM.FLEET.value == costSettleDetail.settleType">
        <FleetBankSelect width="250px" v-model:value="form.bankId" :fleetId="costSettleDetail.settleObjectId" @change="bankChange"/>
      </a-form-item>

      <a-form-item label="收款银行" v-if="SETTLE_TYPE_ENUM.DRIVER.value == costSettleDetail.settleType">
        <DriverBankSelect width="250px"  v-model:value="form.bankId" :driverId="costSettleDetail.settleObjectId" @change="bankChange"/>
      </a-form-item>
    </template>
    <template v-else>
      <a-form-item label="油卡" name="oilCardId">
        <OilCardSelect :type="OIL_CARD_TYPE_ENUM.SLAVE.value" width="200px" v-model:value="form.oilCardId" :disabledFlag="false"
      />
      </a-form-item>
    </template>

    <a-form-item label="备注">
      <a-textarea :rows="2" v-model:value="form.remark" style="width: 100%" type="textarea" placeholder="请输入备注"/>
    </a-form-item>

    <a-alert v-if="!$lodash.isEmpty(costSettleDetail.existSettleWaybillNumberList)"
             :message="costSettleDetail.existSettleWaybillNumberList.join(',') + '存在已提交的付款记录，请仔细确认！如若对应运单已付金额为0，请查看对应付款单是否审核通过并付款。'"
             type="warning" show-icon class="smart-margin-bottom5"/>

    <a-table
        :columns="columns"
        :dataSource="tableData"
        bordered
        :pagination="false"
        size="small"
    >
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'orderType'">
          {{ $smartEnumPlugin.getDescByValue('ORDER_TYPE_ENUM', text) }}
        </template>
        <template v-if="column.dataIndex === 'costItemType'">
          {{ $smartEnumPlugin.getDescByValue('COST_ITEM_TYPE_ENUM', record.costItemType) }}
        </template>
      </template>
    </a-table>
  </a-modal>
</template>
<script setup>
import { ref, reactive, getCurrentInstance, computed} from 'vue';
import {SmartLoading} from '/@/components/smart-loading';
import {waybillApi} from '/@/api/business/waybill/waybill-api';
import {payOrderApi} from '/@/api/business/pay/pay-order-api';
import {SETTLE_TYPE_ENUM} from '/@/constants/business/waybill-const';
import { ORDER_TYPE_ENUM } from '/@/constants/business/order-const';
import { PAY_ORDER_TYPE_ENUM } from '/@/constants/business/pay-order-const';
import {message} from "ant-design-vue";
import FleetBankSelect from '/@/components/fleet-bank-select/index.vue';
import DriverBankSelect from '/@/components/driver-bank-select/index.vue';
import { getContainer } from '/@/utils/container-util';
import OilCardSelect from '/@/components/oil-card-select/index.vue';
import { OIL_CARD_TYPE_ENUM } from "/@/constants/business/card-const.js";

// ----------------------- 对外暴露 ------------------------
const emit = defineEmits('reloadList');
defineExpose({
  showModal,
});

let title = computed(() => {
  return form.payOrderType == PAY_ORDER_TYPE_ENUM.CASH.value ? '现金费用结算' : '油卡费用结算';
});

// ----------------------- 提交表单 ------------------------
const formDefault = {
  payOrderType: undefined,
  waybillId: undefined,
  bankId: undefined,
  oilCardId: undefined,
  remark: undefined,

};
let form = reactive({...formDefault});


// ----------------------- 显示/隐藏 ------------------------
const visible = ref(false);

function onClose() {
  Object.assign(form, formDefault);
  selectedBank.value = {};
  visible.value = false;
}

let costSettleDetail = ref({});
const internalInstance = getCurrentInstance();
const smartEnumPlugin = internalInstance.appContext.config.globalProperties.$smartEnumPlugin;
async function showModal(param) {
  SmartLoading.show();
  Object.assign(form, formDefault);
  form.payOrderType = param.payOrderType;
  form.waybillId = param.waybillId;
  try {
    let api = form.payOrderType == PAY_ORDER_TYPE_ENUM.CASH.value ? waybillApi.costCostSettleQuery : waybillApi.oilCardCostSettleQuery;

    let responseModel = await api(param.waybillId);
    let data = responseModel.data;
    costSettleDetail.value = data;
    tableData.value = data.settleCostList;

    selectedBank.value = {};
    visible.value = true;
  } catch (error) {
    console.log('error', error);
  } finally {
    SmartLoading.hide();
  }
}

let selectedBank = ref({});
function bankChange(bankId, bankObj){
  if(!bankObj){
    selectedBank.value = {};
    return;
  }
  selectedBank.value = bankObj;
}


// ----------------------- 应付表格 ------------------------
const tableData = ref([]);
const columns = reactive([
  {
    title: '费用名称',
    width: 80,
    dataIndex: 'costItemName',
    fixed: 'left'
  },
  {
    title: '应付金额',
    width: 80,
    dataIndex: 'costAmount',
  },
  {
    title: '费用类型',
    width: 80,
    dataIndex: 'costItemType'
  },

]);



// -----------------------提交 ------------------------
async function onSubmit() {
  try {
    SmartLoading.show();
    if(form.payOrderType == PAY_ORDER_TYPE_ENUM.CASH.value && !form.bankId){
      message.error('请选择收款银行信息');
      return;
    }
    if(form.payOrderType == PAY_ORDER_TYPE_ENUM.OIL_CARD.value && !form.oilCardId){
      message.error('请选择充值油卡');
      return;
    }
    await payOrderApi.create(form);

    emit('reloadList');
    onClose();
  } catch (error) {
    console.log(error);
  } finally {
    SmartLoading.hide();
  }
}

</script>
<style scoped lang="less">
.left-item {
  border: solid 1px rgba(0, 0, 0, 0.2);
  padding: 5px;
  margin-top: 10px
}

.right-item {
  margin-left: 15px;
  border: solid 1px rgba(0, 0, 0, 0.2);
  padding: 10px;
  margin-top: 10px
}
</style>

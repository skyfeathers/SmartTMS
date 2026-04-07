<!--
 * @Description: 运单列表
 * @version:
 * @Author: zhuoda
 * @Date: 2022-07-07 15:41:39
 * @LastEditors: zhuoda
 * @LastEditTime: 2022-08-06
-->
<template>
  <div>
    <WaybillList ref="waybillListRef" :columns="columnsData" :showQueryForm="showQueryFormTag" :tableWidth="tableWidth" :tabParams="tabParams" @changeSelect="onSelectChange" >
      <template #actionList>
        <div class="smart-table-operate-block">
          <a-button v-privilege="'waybill:edit'" :disabled="disableOperateFlag" size="small" type="primary"
            @click="waybillUpdate"> 编辑
          </a-button>
          <a-button v-privilege="'waybill:updateLeadSealAndContainerNumber'" :disabled="disableOperateFlag" size="small"
            type="primary" @click="validate(showUpdateLeadSealNumber)"> 填写箱号/铅封号
          </a-button>
          <a-button v-privilege="'waybill:driverGoods'" :disabled="disableOperateFlag" size="small" type="primary"
            @click="showWaybillVoucherUploadModal(WAYBILL_VOUCHER_TYPE_ENUM.LOAD.value)"> 装货
          </a-button>
          <a-button v-privilege="'waybill:receiveGoods'" :disabled="disableOperateFlag" size="small" type="primary"
            @click="showWaybillVoucherUploadModal(WAYBILL_VOUCHER_TYPE_ENUM.UNLOAD.value)"> 卸货
          </a-button>
          <a-button v-privilege="'waybill:createContract'" :disabled="disableOperateFlag" size="small" type="primary"
            @click="validate(createContract)"> 生成合同
          </a-button>
          <a-button v-privilege="'waybill:updateCost'" :disabled="disableOperateFlag" size="small" type="primary"
            @click="validate(showCostModal)"> 费用
          </a-button>
          <a-button v-privilege="'waybill:submitSettle'" :disabled="disableOperateFlag" size="small" type="primary"
            @click="showSettleModal(PAY_ORDER_TYPE_ENUM.CASH.value)"> 提交应付结算
          </a-button>
          <a-button v-privilege="'waybill:updateOilCard'" :disabled="disableOperateFlag" size="small" type="primary"
            @click="showSettleModal(PAY_ORDER_TYPE_ENUM.OIL_CARD.value)"> 油卡充值
          </a-button>
          <a-button v-privilege="'waybill:cancel'" :disabled="disableOperateFlag" size="small" type="primary" danger
            @click="cancel"> 作废
          </a-button>
          <a-button v-privilege="'waybill:complete'" :disabled="disableOperateFlag" size="small" type="primary"
            @click="complete"> 运输完成
          </a-button>
          <a-button v-privilege="'waybill:export'" size="small" type="primary" @click="exportExcel"> 导出
          </a-button>

          <a-button v-privilege="'waybill:uploadReceipt'" :disabled="disableOperateFlag" size="small" type="primary"
            @click="validate(showReceiptModal)">上传回单
          </a-button>
          <a-button size="small" type="primary" :disabled="disableOperateFlag"
                    @click="handlePathUpdate" v-privilege="'waybill:updatePath'">
                    修改路线
          </a-button>
            <a-button size="small" type="primary"
                    @click="showQueryFormTag = !showQueryFormTag">
                    {{ showQueryFormTag ? '收起查询' : '显示查询' }}
            </a-button>
        </div>
        <div class="smart-table-setting-block"></div>
        <div>
          <SmartTableOperator v-model="columnsData" :tableId="TABLE_ID_CONST.WAYBILL" :refresh="refresh" />
        </div>
      </template>

      <template #tabList>
        <a-tabs v-model:activeKey="activeKey" @tabClick="changeTab">
          <a-tab-pane :key="''" tab="全部" />
          <a-tab-pane :key="item.value" :tab="item.desc" v-for="item in tabList"  />
        </a-tabs>
      </template>
    </WaybillList>

    <WaybillLeadSealNumberModal ref="waybillContainerLeadSealModalRef" @reloadList="refresh" />
    <ContractCreateModal ref="contractCreateModal" @refresh="refresh" />
    <WaybillSettleModal ref="waybillSettleModal" @reloadList="refresh" />
    <WaybillCostModal ref="waybillCostModal" @reloadList="refresh" />
    <WaybillUpdateModal ref="waybillUpdateModal" @reloadList="refresh" />
    <WaybillOilCardRechargeModal ref="oilCardRechargeModal" />
    <!-- 运输完成 -->
    <WaybillCompleteModal ref="completeModal" @reloadList="refresh" />
    <!-- 上传回单凭证 -->
    <WaybillReceiptModal ref="receiptRef" @reloadList="refresh" />
    <!-- 更新运单订单类型 -->
    <!--  <WaybillOrderTypeUpdateModal ref="orderTypeUpdateRef" @reloadList="refresh"/>-->

    <!-- 上传凭证  装货/卸货 -->
    <WaybillVoucherUploadModal ref="waybillVoucherUploadModalRef" @success="refresh" />
    <!-- 修改路线 -->
    <WaybillPathUpdateModal ref="waybillPathUpdateModalRef" @reloadList="refresh" />
  </div>
</template>
<script setup>
import WaybillList from './components/waybill-table-list.vue';
import SmartTableOperator from '/@/components/smart-table-operator/index.vue';
import WaybillOilCardRechargeModal from './components/waybill-oil-card-recharge-modal.vue';
import WaybillLeadSealNumberModal from './components/waybill-lead-seal-and-container-number-modal.vue';
import WaybillSettleModal from './components/waybill-settle-modal.vue';
import WaybillCostModal from './components/waybill-cost-modal.vue';
import WaybillUpdateModal from './components/waybill-update-modal.vue';

import ContractCreateModal from '/@/views/business/contract/contract-create-modal.vue';
import WaybillCompleteModal from './components/waybill-receive-complete-modal.vue';
import WaybillReceiptModal from './components/waybill-receipt-modal.vue';
import { useWaybillCancel } from './hooks/use-waybill-cancel';
import { columns } from './components/waybill-list-table-column';
import { ORDER_TYPE_ENUM } from '/@/constants/business/order-const';

import { TABLE_ID_CONST } from '/@/constants/table-id-const';
import { ref, onMounted, computed, inject } from 'vue';
import { message, Modal } from 'ant-design-vue';
import { useRoute } from 'vue-router';
import { WAYBILL_STATUS_ENUM, WAYBILL_VOUCHER_TYPE_ENUM } from '/@/constants/business/waybill-const';
import { PAY_ORDER_STATUS_ENUM } from '/@/constants/business/pay-order-const';
import { FLOW_AUDIT_STATUS_ENUM } from '/@/constants/business/flow-const';
import { PAY_ORDER_TYPE_ENUM } from '/@/constants/business/pay-order-const';
import _ from 'lodash';
import useDragTable from '/@/hook/use-drag-table/index';
import WaybillVoucherUploadModal from './components/waybill-voucher-upload-modal.vue';
import WaybillPathUpdateModal from './components/waybill-path-update-modal.vue';

const smartEnumPlugin = inject('smartEnumPlugin');

let { columnsData,tableWidth} = useDragTable(columns,TABLE_ID_CONST.WAYBILL);

// -------------查询表单展开收起------------------
let showQueryFormTag = ref(false);

// -------------表单字段------------------
const disableOperateFlag = computed(() => {
  return selectedRowKeyList.value.length == 0;
});

// ----------- table 批量操作 start -----------
const selectedRowKeyList = ref([]);
const selectedDataList = ref([]);

function onSelectChange(keyArray, dataArray) {
  selectedRowKeyList.value = keyArray;
  selectedDataList.value = dataArray;
}

let waybillListRef = ref();

const activeKey = ref('');
let waybillStatusList = ref([]);

let tabParams = ref({});

let route = useRoute();


onMounted(() => {
  let orderNo = route.query.orderNo;
  let waybillNumber = route.query.waybillNumber;
  refresh({ keywords: orderNo || waybillNumber });
});

function changeTab(value) {
  activeKey.value = value;
  // tab项参数
  let findTabItem = tabList.value.find(e => e.value === activeKey.value);
  if (findTabItem && findTabItem.params) {
    tabParams.value = findTabItem.params;
  }else{
    tabParams.value = {};
  }
  refresh();
}

function refresh(params = {}) {
  waybillListRef.value.search(params);
}


// ----------- 填写箱号-----------
function validate(callback) {
  if (selectedRowKeyList.value.length !== 1) {
    message.error('请选择一个运单');
    return;
  }
  for (const item of waybillListRef.value.tableData) {
    if (_.includes(selectedRowKeyList.value, item.waybillId)) {
      if (item.auditStatus !== FLOW_AUDIT_STATUS_ENUM.PASS.value) {
        message.warning('请选择审核通过的运单！请移除【' + item.waybillNumber + '】');
        return;
      }
      if (item.waybillStatus == WAYBILL_STATUS_ENUM.CANCEL.value) {
        message.warning('请选择未作废的运单！请移除【' + item.waybillNumber + '】');
        return;
      }
    }
  }
  callback();
}

// ----------- 填写铅封号-----------
const waybillContainerLeadSealModalRef = ref();

async function showUpdateLeadSealNumber() {
  let selectedList = _.cloneDeep(selectedDataList.value);
  let waybill = selectedDataList.value[0];
  waybillContainerLeadSealModalRef.value.showModal(waybill);
}

// ----------- 作废-----------
function cancel() {
  if (selectedRowKeyList.value.length !== 1) {
    message.error('请选择一个运单');
    return;
  }
  let waybill = selectedDataList.value[0];
  if (waybill.waybillStatus == WAYBILL_STATUS_ENUM.CANCEL.value) {
    message.warning('运单已被作废');
    return;
  }
  useWaybillCancel().confirmCancel(waybill.waybillNumber, waybill.waybillId, refresh);
}


// ----------- 费用-----------
const waybillCostModal = ref();

function showCostModal() {
  let waybill = selectedDataList.value[0];
  waybillCostModal.value.showModal(waybill.waybillId);
}

// ----------- 结算-----------
const waybillSettleModal = ref();

function showSettleModal(payOrderType) {
  if (selectedRowKeyList.value.length > 1) {
    message.error('只能选择一个运单');
    return;
  }
  waybillSettleModal.value.showModal({
    waybillId: selectedRowKeyList.value[0],
    payOrderType,
  });
}

// ----------------- 合同 --------------------
let contractCreateModal = ref();

function createContract() {
  let waybill = selectedDataList.value[0];
  contractCreateModal.value.showModal(waybill.waybillId, waybill.waybillNumber);
}

// 修改
let waybillUpdateModal = ref();

function waybillUpdate() {
  if (selectedDataList.value.length !== 1) {
    message.error('请选择一个运单');
    return;
  }
  let waybill = selectedDataList.value[0];
  if (waybill.waybillStatus == WAYBILL_STATUS_ENUM.CANCEL.value) {
    message.error('运单已作废');
    return;
  }
  waybillUpdateModal.value.showModal(waybill);
}



// 更新运单为运输完成
let completeModal = ref();

function complete() {
  if (selectedRowKeyList.value.length == 0) {
    message.error('请至少选择一个运单');
    return;
  }
  let selectedList = _.cloneDeep(selectedDataList.value);
  let orderTypeList = selectedList.map(e => e.orderType);
  // 如果是网络货运运单、只允许选择一个
  if (_.includes(orderTypeList, ORDER_TYPE_ENUM.NETWORK_FREIGHT.value)) {
    message.error('该运单属于网络货运订单，不能运输完成');
    return;
  }
  for (const item of waybillListRef.value.tableData) {
    if (_.includes(selectedRowKeyList.value, item.waybillId)) {
      if (item.waybillStatus !== WAYBILL_STATUS_ENUM.TRANSPORT.value) {
        message.warning('请选择运输中的运单！请移除【' + item.waybillNumber + '】');
        return;
      }
      if (item.waybillStatus == WAYBILL_STATUS_ENUM.CANCEL.value) {
        message.warning('请选择未作废的运单！请移除【' + item.waybillNumber + '】');
        return;
      }
    }
  }
  completeModal.value.showModal(selectedRowKeyList.value);
}


// 显示上传回单
const receiptRef = ref();
function showReceiptModal() {
  receiptRef.value.showModal(selectedRowKeyList.value[0], selectedDataList.value[0].receiptAttachment || []);
}





// ------------ 导出 Excel -----------
function exportExcel() {
  waybillListRef.value.exportExcel();
}


// tab状态
let noPayStatus = [ PAY_ORDER_STATUS_ENUM.CANCEL.value];
let tabList = ref([
  ...smartEnumPlugin.getValueDescList('WAYBILL_STATUS_ENUM').filter(e => e.value !== WAYBILL_STATUS_ENUM.CANCEL.value).map(e => (
      {
        value: e.value,
        desc: e.desc,
        params: {
          payStatus: null,
          waybillStatus: e.value,
        }
      })),
  {
    value: 100 + PAY_ORDER_STATUS_ENUM.NO_APPLY.value,
    desc: PAY_ORDER_STATUS_ENUM.NO_APPLY.desc,
    params: {
      payStatus: PAY_ORDER_STATUS_ENUM.NO_APPLY.value,
      waybillStatus: WAYBILL_STATUS_ENUM.COMPLETE.value,
    }
  },
  {
    value: 100 + PAY_ORDER_STATUS_ENUM.PAID.value,
    desc: PAY_ORDER_STATUS_ENUM.PAID.desc,
    params: {
      payStatus: PAY_ORDER_STATUS_ENUM.PAID.value,
      waybillStatus: WAYBILL_STATUS_ENUM.COMPLETE.value,
    }
  },
  {
    value: WAYBILL_STATUS_ENUM.CANCEL.value,
    desc: WAYBILL_STATUS_ENUM.CANCEL.desc,
    params: {
      payStatus: null,
      waybillStatus: WAYBILL_STATUS_ENUM.CANCEL.value,
    }
  }
]);


// ------------ 上传凭证  装货/卸货 -----------
const waybillVoucherUploadModalRef = ref(null);
function showWaybillVoucherUploadModal(type) {
  if (selectedRowKeyList.value.length > 1) {
    message.error('只能选择一个运单');
    return;
  }
  waybillVoucherUploadModalRef.value.showModal({
    waybillId: selectedRowKeyList.value[0],
    type
  });
}

// ------------ 修改路线 -----------
const waybillPathUpdateModalRef = ref(null);
function handlePathUpdate() {
  if (selectedRowKeyList.value.length > 1) {
    message.error('只能选择一个运单');
    return;
  }
  waybillPathUpdateModalRef.value.showModal(selectedRowKeyList.value[0]);
}

</script>
<style scoped lang="less">
.smart-query-form {
  padding: 5px 10px;
  background-color: #FFFFFF;
  margin-bottom: 0px;
}
</style>

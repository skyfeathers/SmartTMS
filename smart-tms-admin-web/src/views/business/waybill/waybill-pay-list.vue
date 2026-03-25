<!--
 * @Description: 运单列表
 * @version:
 * @Author: zhuoda
 * @Date: 2022-07-07 15:41:39
 * @LastEditors: zhuoda
 * @LastEditTime: 2022-08-06
-->
<template>
  <WaybillList ref="waybillListRef" :tableWidth="tableWidth" :columns="columnsData" privilegePrefix="waybillReceive" :tabParams="tabParams" @changeSelect="onSelectChange">
    <template #actionList>
      <div class="smart-table-operate-block" style="display: flex;">
        <a-button v-privilege="'waybillReceive:submitCheck'" :disabled="disableOperateFlag" size="small"
                  @click="createReceiveOrder()">提交核算
        </a-button>
        <a-button v-privilege="'waybillReceive:submitInvoice'" :disabled="disableOperateFlag" size="small"
                  @click="createReceiveOrderAndInvoice()">
          提交核算并开票
        </a-button>

        <a-button v-privilege="'waybillReceive:export'" size="small"
                  @click="exportExcel()">
          导出
        </a-button>

        <a-alert show-icon type="error">
          <template #message>
            还有{{ waybillCount }}个运输完成的运单未提交财务核算！
          </template>
        </a-alert>
      </div>
      <div class="smart-table-setting-block"></div>
      <div>
        <SmartTableOperator v-model="columnsData" :tableId="TABLE_ID_CONST.WAYBILL_INVOICE" :refresh="refresh"/>
      </div>
    </template>

    <template #tabList>
      <a-tabs v-model:activeKey="activeKey" @tabClick="changeTab">
        <a-tab-pane :key="FLAG_NUMBER_ENUM.FALSE.value" tab="未提交">
        </a-tab-pane>
        <a-tab-pane :key="FLAG_NUMBER_ENUM.TRUE.value" tab="已提交">
        </a-tab-pane>
        <a-tab-pane :key="null" tab="全部">
        </a-tab-pane>
      </a-tabs>
    </template>
  </WaybillList>

  <!-- 提交结算 -->
  <ReceiveOrderModal ref="receiveOrderRef" @refresh="refresh"/>
  <ReceiveOrderInvoiceModal ref="receiveOrderInvoiceRef" @refresh="refresh"/>
</template>
<script setup>
import WaybillList from './components/waybill-table-list.vue';
import SmartTableOperator from '/@/components/smart-table-operator/index.vue';
import ReceiveOrderInvoiceModal from './components/receive-order-invoice-modal.vue';
import ReceiveOrderModal from './components/receive-order-modal.vue';
import { columns } from './components/waybill-list-table-column';

import { TABLE_ID_CONST } from '/@/constants/table-id-const';
import { ref, computed, onMounted } from 'vue';
import { message } from 'ant-design-vue';
import { WAYBILL_STATUS_ENUM } from '/@/constants/business/waybill-const';
import { FLAG_NUMBER_ENUM } from '/@/constants/common-const';
import { useRoute } from 'vue-router';
import { waybillApi } from '/@/api/business/waybill/waybill-api';
import { SmartLoading } from '/@/components/smart-loading';
import _ from 'lodash';
import useDragTable from '/@/hook/use-drag-table/index';

let { columnsData, tableWidth } = useDragTable(columns,TABLE_ID_CONST.WAYBILL_INVOICE);
// -------------查询运单未提交结算的总数量------------------
let waybillCount = ref(0);

async function queryCount () {
  try {
    let defaultParams = {
      submitReceiveFlag: FLAG_NUMBER_ENUM.FALSE.value,
      waybillStatusList: [WAYBILL_STATUS_ENUM.COMPLETE.value]
    };
    let params = Object.assign(waybillListRef.value.queryFormState, defaultParams);
    let responseModel = await waybillApi.count(params);
    waybillCount.value = responseModel.data;
  } catch (e) {
    console.log(e);
  }
}

// -------------表单字段------------------
const disableOperateFlag = computed(() => {
  return selectedRowKeyList.value.length == 0;
});


// ----------- table 批量操作 start -----------
const selectedRowKeyList = ref([]);
const selectedDataList = ref([]);

function onSelectChange (keyArray, dataArray) {
  selectedRowKeyList.value = keyArray;
  selectedDataList.value = dataArray;
}

let waybillListRef = ref();

const activeKey = ref(FLAG_NUMBER_ENUM.FALSE.value);
let tabParams = ref({submitReceiveFlag:FLAG_NUMBER_ENUM.FALSE.value, waybillStatus: WAYBILL_STATUS_ENUM.COMPLETE.value});
function changeTab (value) {
  tabParams.value.submitReceiveFlag = value;
  refresh();
}

function refresh () {
  waybillListRef.value.ajaxQuery();
  queryCount();
}

// ----------------- 提交结算 、并开票 --------------------
const receiveOrderRef = ref();

function createReceiveOrder () {
  for (const item of selectedDataList.value) {
    if (item.waybillStatus != WAYBILL_STATUS_ENUM.COMPLETE.value) {
      message.warning('运单【' + item.waybillNumber + '】未运输完成，不能提交财务核算');
      return;
    }
  }
  receiveOrderRef.value.showModal(selectedRowKeyList.value, selectedDataList.value[0].shipperId);
}

const receiveOrderInvoiceRef = ref();

function createReceiveOrderAndInvoice () {
  for (const item of selectedDataList.value) {
    if (item.waybillStatus != WAYBILL_STATUS_ENUM.COMPLETE.value) {
      message.warning('运单【' + item.waybillNumber + '】未运输完成，不能提交财务核算');
      return;
    }
  }
  receiveOrderInvoiceRef.value.showModal(selectedRowKeyList.value, selectedDataList.value[0].shipperId);
}

let route = useRoute();
onMounted(() => {
  refresh();
});

// ------------ 导出 Excel -----------
function exportExcel () {
  waybillListRef.value.exportExcel();
}

</script>
<style lang="less" scoped>
:deep(.ant-alert) {
  padding: 4px 15px;
}
</style>

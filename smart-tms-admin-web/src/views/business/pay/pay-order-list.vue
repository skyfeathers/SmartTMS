<!--
 * @Description: 应付列表
 * @version:
 * @Author: zhuoda
 * @Date: 2022-07-07 15:41:39
 * @LastEditors: zhuoda
 * @LastEditTime: 2022-08-06
-->
<template>
  <a-form class="smart-query-form" v-privilege="'payOrder:query'">
    <a-row class="smart-query-form-row">

      <a-form-item label="司机" class="smart-query-form-item">
        <DriverSelect width="150px" @change="driverId=>changeSettleObjectId(driverId,SETTLE_TYPE_ENUM.DRIVER.value)"
                      :value="queryForm.driverId" placeholder="请选择司机"/>
      </a-form-item>
      <a-form-item class="smart-query-form-item" label="车辆">
          <VehicleSelect v-model:value="queryForm.vehicleId" width="150px"/>
        </a-form-item>
      <a-form-item label="车队" class="smart-query-form-item">
        <FleetSelect width="150px" @change="fleetId=>changeSettleObjectId(fleetId,SETTLE_TYPE_ENUM.FLEET.value)"
                     :value="queryForm.fleetId" placeholder="请选择车队"/>
      </a-form-item>
      <a-form-item class="query-item" label="货主">
        <ShipperSelect v-model:value="queryForm.shipperId" placeholder="请选择货主" width="150px" />
      </a-form-item>
      <a-form-item label="关键字" class="smart-query-form-item">
        <a-input v-model:value="queryForm.keywords" placeholder="付款单号/运单号/箱号/备注" style="width: 200px"/>
      </a-form-item>
      <a-form-item class="smart-query-form-item" label="运单号">
          <a-textarea v-model:value="queryForm.waybillNumbers" placeholder="运单号，多个以,进行拼接" style="width:100%"
                      :row="4"/>
      </a-form-item>
      <a-form-item class="smart-query-form-item" label="创建人">
        <EmployeeSelect v-model:value="queryForm.createUserId" :leaveFlag="false" placeholder="请选择创建人" width="100%"/>
      </a-form-item>

      <a-form-item label="创建时间" class="smart-query-form-item">
        <a-range-picker @change="changeCreateDate" v-model:value="createTimeDateRange" :presets="defaultTimeRanges"
                        style="width: 220px"/>
      </a-form-item>
      <a-form-item label="支付时间" class="smart-query-form-item">
        <a-range-picker @change="changePayDate" v-model:value="payTimeDateRange" :presets="defaultTimeRanges"
                        style="width: 220px"/>
      </a-form-item>
      <a-form-item class="smart-query-form-item">
        <a-checkbox v-model:checked="queryForm.auditByMeFlag" @change="search">我审过的</a-checkbox>
      </a-form-item>
      <a-form-item class="smart-query-form-item">
        <a-checkbox v-model:checked="queryForm.waitAuditByMeFlag" @change="search">待我审核</a-checkbox>
      </a-form-item>
      <a-form-item class="smart-query-form-item smart-margin-left10">
        <a-button type="primary" @click="search">
          <template #icon>
            <SearchOutlined/>
          </template>
          查询
        </a-button>
        <a-button @click="resetQuery">
          <template #icon>
            <ReloadOutlined/>
          </template>
          重置
        </a-button>
      </a-form-item>
    </a-row>
  </a-form>

  <a-card size="small" :bordered="false" :hoverable="true">
    <a-row class="smart-table-btn-block">
      <div class="smart-table-operate-block">
        <a-button v-if="activeTab == PAY_ORDER_ACTIVE_TAB.WAIT_PAY.value && $privilege('payOrder:pay')" @click="showPayModal"
                  type="primary" size="small" :disabled="selectedRowKeyList.length == 0">
          批量支付
        </a-button>
        <a-button v-show="activeTab == PAY_ORDER_ACTIVE_TAB.PAYMENT.value && $privilege('payOrder:verification')" @click="showVerificationModal"
                  type="primary" size="small"
                  :disabled="selectedRowKeyList.length == 0">
          批量核销
        </a-button>

        <a-button @click="exportExcel()" v-privilege="'payOrder:export'" size="small">导出</a-button>

        <span class="smart-statistics">
          合计：<a-tooltip>
              <template #title>合计不包含已作废、审核驳回的单据金额</template>
              <question-circle-outlined/>
            </a-tooltip>
          应付金额：<span class="amount">{{ formatMoneyStr(amountStatistics?.payableTotalAmount) }}</span>元，已付金额：<span
            class="amount">{{ formatMoneyStr(amountStatistics?.paidTotalAmount) }}</span>元。


          <template v-if="selectedRowKeyList.length !== 0">
            选中行的应付金额：<span class="amount">{{ formatMoneyStr(selectedStatisticInfo?.payableTotalAmount) }}</span>元，已付金额：<span
              class="amount">{{ formatMoneyStr(selectedStatisticInfo?.paidTotalAmount) }}</span>元。
          </template>
        </span>
      </div>
      <div class="smart-table-setting-block"></div>
      <div>
        <SmartTableOperator v-model="columns" :tableId="tableId" :refresh="init"/>
      </div>
    </a-row>

    <a-tabs v-model:activeKey="activeTab" @change="changeActiveTab">
      <a-tab-pane v-for="item in $smartEnumPlugin.getValueDescList('PAY_ORDER_ACTIVE_TAB')"
                  :key="item.value"
                  :tab="item.desc"/>
      <a-tab-pane key="ALL"
                  tab="全部"/>
    </a-tabs>

    <a-table
        :scroll="{ x: '100%' }"
        size="small"
        bordered
        :dataSource="tableData"
        :columns="columns"
        rowKey="payOrderId"
        :pagination="false"
        :row-selection="{ selectedRowKeys: selectedRowKeyList, onChange: onSelectChange}"
    >
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'payOrderNumber'">
          <a @click="payOrderDetail(record.payOrderId)">{{ text }}</a><SmartCopyIcon :value="text" />
        </template>
        <template v-if="column.dataIndex === 'waybillNumber'">
          {{ getWaybillNumber(record) }}<SmartCopyIcon :value="text" />
        </template>

        <template v-if="column.dataIndex === 'driverId'">
          {{ record.driverName }}/{{ record.driverTelephone  }}
        </template>
        <template v-if="column.dataIndex === 'vehicleNumber'">
          {{ record.vehicleNumber }}
        </template>
        <template v-if="column.dataIndex === 'fleetId' && record.fleetId" >
          {{ record.fleetName }}/{{ record.fleetCaptainName }}/{{ record.fleetCaptainPhone }}
        </template>

        <template v-if="column.dataIndex === 'payOrderStatus'">
          <a-tag :color="$smartEnumPlugin.getFieldByValue('PAY_ORDER_STATUS_ENUM', record.payOrderStatus, 'color')">{{
              $smartEnumPlugin.getDescByValue('PAY_ORDER_STATUS_ENUM', record.payOrderStatus)
            }}
          </a-tag>
        </template>
        <template v-if="column.dataIndex === 'auditStatus'">
          <a-tag :color="$smartEnumPlugin.getFieldByValue('FLOW_AUDIT_STATUS_ENUM', record.auditStatus, 'color')">{{
              $smartEnumPlugin.getDescByValue('FLOW_AUDIT_STATUS_ENUM', record.auditStatus)
            }}
          </a-tag>
        </template>

        <template v-if="column.dataIndex === 'payBankAccount' && record.paymentVO">
          {{
            record.paymentVO ? record.paymentVO.payAccountName : ''
          }}/{{ record.paymentVO ? record.paymentVO.payBankAccount : '' }}
        </template>
        <template v-if="column.dataIndex === 'receiveBankAccount'">
          {{
            record.receiveVO ? record.receiveVO.receiveAccountName : ''
          }}/{{ record.receiveVO ? record.receiveVO.receiveBankAccount : '' }}
        </template>
        <template v-if="column.dataIndex === 'payUserName'">
          {{ record.paymentVO ? record.paymentVO.createUserName : '' }}
        </template>
        <template v-if="column.dataIndex === 'payTime'">
          {{ record.paymentVO ? record.paymentVO.createTime : '' }}
        </template>
        <template v-if="column.dataIndex === 'verificationUserName'">
          {{ record.verificationVO ? record.verificationVO.createUserName : '' }}
        </template>
        <template v-if="column.dataIndex === 'verificationTime'">
          {{ record.verificationVO ? record.verificationVO.createTime : '' }}
        </template>
        <template v-if="column.dataIndex === 'receiptAttachment'">
          <a @click="showReceiptAttachmentModal(record)" v-if="!_.isEmpty(record.receiptAttachment)">查看附件</a>
        </template>
        <template v-if="column.dataIndex === 'remark'">
          {{formatRemark(record.remark) }}
        </template>
<!--        <template v-if="column.dataIndex === 'truckOrderAttachment'">
          <file-preview :fileList="record.truckOrderAttachment" type="picture" />
        </template>-->
        <template v-if="column.dataIndex === 'action'">
          <div class="smart-table-operate">
            <a-button v-show="record.auditStatus == FLOW_AUDIT_STATUS_ENUM.WAIT.value && record.auditFlag && $privilege('payOrder:audit')" size="small" type="link"
                      @click="showAuditModal(record)">
              审核
            </a-button>
            <!-- 审核通过、未支付、未核销且有权限 -->
            <a-button v-show="record.auditStatus == FLOW_AUDIT_STATUS_ENUM.PASS.value && (record.payOrderStatus == PAY_ORDER_STATUS_ENUM.NO_PAY.value || record.payOrderStatus == PAY_ORDER_STATUS_ENUM.PAID.value)  && !record.verificationFlag && $privilege('payOrder:cancel')" size="small" type="link"
                      @click="showCancelModal(record.payOrderId)">
              作废
            </a-button>
          </div>
        </template>
      </template>

    </a-table>

    <div class="smart-query-table-page">
      <a-pagination
          showSizeChanger
          showQuickJumper
          show-less-items
          :pageSizeOptions="PAGE_SIZE_OPTIONS"
          :defaultPageSize="queryForm.pageSize"
          v-model:current="queryForm.pageNum"
          v-model:pageSize="queryForm.pageSize"
          :total="total"
          @change="init"
          @showSizeChange="init"
          :show-total="(total) => `共${total}条`"
      />
    </div>
    <PayOrderPaymentModal ref="paymentModalRef" @reloadList="init"/>
    <PayOrderVerificationModal ref="verificationRef" @reloadList="init"/>
    <PayOrderCancelModal ref="payOrderCancelModalRef" @reloadList="init"/>
    <!-- 审核 -->
    <flow-audit-modal ref="flowAuditRef" @refresh="ajaxQuery"/>
    <a-modal v-model:open="showReceiptAttachmentFlag" title="附件预览" :footer="null" width="500px">
      <a-card>
        <Upload
            :default-file-list="receiptAttachmentList"
            listType="picture-card"
            :showUploadBtn="false"
        />
      </a-card>
    </a-modal>
  </a-card>
</template>
<script setup>
import FleetSelect from '/@/components/fleet-select/index.vue';
import DriverSelect from '/@/components/driver-select/index.vue';
import PayOrderPaymentModal from './components/pay-order-payment-modal.vue';
import PayOrderVerificationModal from './components/pay-order-verification-modal.vue';
import SmartTableOperator from '/@/components/smart-table-operator/index.vue';
import EnterpriseSelect from '/@/components/enterprise-select/index.vue';
import EmployeeSelect from '/@/components/employee-select/index.vue';
import FlowAuditModal from '/@/components/flow-audit/index.vue';
import PayOrderCancelModal from './components/pay-order-cancel-form-modal.vue';
import Upload from '/@/components/upload/index.vue';
import { TABLE_ID_CONST } from '/@/constants/table-id-const';
import { defaultTimeRanges } from '/@/lib/default-time-ranges';
import { reactive, ref, onMounted, computed } from 'vue';
import { message, Modal } from 'ant-design-vue';
import { useRouter } from 'vue-router';
import { PAGE_SIZE, PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import { baseColumns, payColumns, verificationColumns, actionColumns } from './components/pay-order-list-table-column';
import { payOrderApi } from '/@/api/business/pay/pay-order-api';
import { PAY_ORDER_STATUS_ENUM, PAY_ORDER_ACTIVE_TAB, PAY_ORDER_TYPE_ENUM } from '/@/constants/business/pay-order-const';
import { SETTLE_TYPE_ENUM } from '/@/constants/business/waybill-const';
import { ORDER_TYPE_ENUM } from '/@/constants/business/order-const';
import { FLOW_AUDIT_STATUS_ENUM } from '/@/constants/business/flow-const';

import _ from 'lodash';
import { SmartLoading } from '/@/components/smart-loading';
import { formatMoney } from '/@/utils/money-util';
import VehicleSelect from '/@/components/vehicle-select/index.vue';
import ShipperSelect from '/@/components/shipper-select/index.vue';

const queryFormState = {
  keywords: '',
  orderTypeList: [ORDER_TYPE_ENUM.ORDINARY.value],
  payOrderStatus: PAY_ORDER_STATUS_ENUM.NO_PAY.value,
  verificationFlag: false,
  endTime: null,
  startTime: null,
  payEndTime: null,
  payStartTime: null,
  pageNum: 1,
  pageSize: PAGE_SIZE,
  searchCount: true,
  driverId: null,
  fleetId: null,
  enterpriseId: null,
  createUserId: null,
  auditStatus: FLOW_AUDIT_STATUS_ENUM.WAIT.value,
  auditByMeFlag: false,
  waitAuditByMeFlag: false,

  waybillNumbers: null,
  vehicleId: null,
  shipperId: null,
  payOrderType: PAY_ORDER_TYPE_ENUM.CASH.value,
};
const queryForm = reactive({ ...queryFormState });
const tableData = ref([]);
const total = ref(0);
let activeTab = ref(PAY_ORDER_ACTIVE_TAB.WAIT_AUDIT.value);

const tableId = computed(() => {
  if (activeTab.value == PAY_ORDER_ACTIVE_TAB.WAIT_AUDIT.value) {
    return TABLE_ID_CONST.PAY_ORDER_WAIT_AUDIT;
  }
  if (activeTab.value == PAY_ORDER_ACTIVE_TAB.WAIT_PAY.value) {
    return TABLE_ID_CONST.PAY_ORDER_WAIT_PAY;
  }
  if (activeTab.value == PAY_ORDER_ACTIVE_TAB.PAYMENT.value) {
    return TABLE_ID_CONST.PAY_ORDER_PAYMENT;
  }
  if (activeTab.value == PAY_ORDER_ACTIVE_TAB.VERIFICATION.value) {
    return TABLE_ID_CONST.PAY_ORDER_VERIFICATION;
  }
  if (activeTab.value == PAY_ORDER_ACTIVE_TAB.CANCEL.value) {
    return TABLE_ID_CONST.PAY_ORDER_VERIFICATION;
  }
  if (activeTab.value == 'ALL') {
    return TABLE_ID_CONST.PAY_ORDER_ALL;
  }
});

const selectedStatisticInfo = computed(() => {
  let payAmountList = selectedDataList.value.map(e => e.payableTotalAmount);
  let paidAmountList = selectedDataList.value.map(e => e.paidTotalAmount);

  let payableTotalAmount = payAmountList.reduce((a, b) => {
    return a + b;
  });
  payableTotalAmount = Number(payableTotalAmount.toFixed(2));
  let paidTotalAmount = paidAmountList.reduce((a, b) => {
    return a + b;
  });
  paidTotalAmount = Number(paidTotalAmount.toFixed(2));
  return {
    payableTotalAmount,
    paidTotalAmount
  };
})

let columns = ref([...baseColumns, ...actionColumns]);
let scrollX = ref(1400);

function changeActiveTab () {
  if (activeTab.value == PAY_ORDER_ACTIVE_TAB.WAIT_AUDIT.value) {
    columns.value = [...baseColumns, ...actionColumns];
    scrollX.value = 1400;
    queryForm.payOrderStatus = PAY_ORDER_STATUS_ENUM.NO_PAY.value;
    queryForm.verificationFlag = null;
    queryForm.auditStatus = FLOW_AUDIT_STATUS_ENUM.WAIT.value;
  }
  if (activeTab.value == PAY_ORDER_ACTIVE_TAB.WAIT_PAY.value) {
    columns.value = [...baseColumns, ...actionColumns];
    scrollX.value = 1400;
    queryForm.auditStatus = FLOW_AUDIT_STATUS_ENUM.PASS.value;
    queryForm.payOrderStatus = PAY_ORDER_STATUS_ENUM.NO_PAY.value;
    queryForm.verificationFlag = null;
    queryForm.auditByMeFlag = false;
    queryForm.waitAuditByMeFlag = false;
  }
  if (activeTab.value == PAY_ORDER_ACTIVE_TAB.PAYMENT.value) {
    let filterColumns = [...baseColumns, ...payColumns, ...actionColumns].filter(e => e.dataIndex != 'currentAuditor');
    columns.value = filterColumns;
    scrollX.value = 2800;
    queryForm.auditStatus = FLOW_AUDIT_STATUS_ENUM.PASS.value;
    queryForm.payOrderStatus = PAY_ORDER_STATUS_ENUM.PAID.value;
    queryForm.verificationFlag = false;
    queryForm.auditByMeFlag = false;
    queryForm.waitAuditByMeFlag = false;
  }
  if (activeTab.value == PAY_ORDER_ACTIVE_TAB.VERIFICATION.value) {
    let filterColumns = [...baseColumns, ...payColumns, ...verificationColumns].filter(e => e.dataIndex != 'currentAuditor');
    columns.value = filterColumns;
    scrollX.value = 3400;
    queryForm.auditStatus = FLOW_AUDIT_STATUS_ENUM.PASS.value;
    queryForm.payOrderStatus = PAY_ORDER_STATUS_ENUM.PAID.value;
    queryForm.verificationFlag = true;
    queryForm.auditByMeFlag = false;
    queryForm.waitAuditByMeFlag = false;
  }
  if (activeTab.value == PAY_ORDER_ACTIVE_TAB.CANCEL.value) {
    let filterColumns = [...baseColumns, ...payColumns, ...verificationColumns].filter(e => e.dataIndex != 'currentAuditor');
    columns.value = filterColumns;
    scrollX.value = 3400;
    queryForm.auditStatus = null;
    queryForm.payOrderStatus = PAY_ORDER_STATUS_ENUM.CANCEL.value;
    queryForm.auditStatus = null;
    queryForm.verificationFlag = null;
    queryForm.auditByMeFlag = false;
    queryForm.waitAuditByMeFlag = false;
  }
  if (activeTab.value == 'ALL') {
    let filterColumns = [...baseColumns, ...payColumns, ...verificationColumns];
    columns.value = filterColumns;
    scrollX.value = 3400;
    queryForm.payOrderStatus = null;
    queryForm.auditStatus = null;
    queryForm.verificationFlag = null;
    queryForm.auditByMeFlag = false;
    queryForm.waitAuditByMeFlag = false;
  }
  queryForm.pageNum = 1;
  init();
}

onMounted(init);

function resetQuery () {
  createTimeDateRange.value = [];
  Object.assign(queryForm, queryFormState);
  activeTab.value = PAY_ORDER_ACTIVE_TAB.WAIT_AUDIT.value;
  init();
}

function search () {
  queryForm.pageNum = 1;
  init();
}

function init () {
  ajaxQuery();
  queryAmountStatistics();
}

async function ajaxQuery () {
  try {
    SmartLoading.show();
    let responseModel = await payOrderApi.query(queryForm);
    const list = responseModel.data.list;
    total.value = responseModel.data.total;
    tableData.value = list;
    clearSelected();
  } catch (e) {
    console.log(e);
  } finally {
    SmartLoading.hide();
  }
}

let amountStatistics = ref({
  payableTotalAmount: 0,
  paidTotalAmount: 0
});

// 统计应付、已付合计
async function queryAmountStatistics () {
  try {
    let params = Object.assign({}, queryForm);
    // params.payOrderStatus = null;
    // params.verificationFlag = null;
    let responseModel = await payOrderApi.queryAmountStatistic(params);
    amountStatistics.value = responseModel.data;
  } catch (e) {
    console.log(e);
  } finally {
  }
}

function formatMoneyStr(value) {
  return formatMoney(value);
}

function formatRemark(remark){
  return (remark || '').replaceAll('\n', '');
}

//创建时间
const createTimeDateRange = ref([]);

function changeCreateDate (dates, dateStrings) {
  queryForm.startTime = dateStrings[0];
  queryForm.endTime = dateStrings[1];
}

const payTimeDateRange = ref([]);
function changePayDate (dates, dateStrings) {
  queryForm.payStartTime = dateStrings[0];
  queryForm.payEndTime = dateStrings[1];
}


function changeSettleObjectId (settleObjectId, settleType) {
  if (settleType == SETTLE_TYPE_ENUM.DRIVER.value) {
    queryForm.driverId = settleObjectId;
  }
  if (settleType == SETTLE_TYPE_ENUM.FLEET.value) {
    queryForm.fleetId = settleObjectId;
  }
}

// ----------- 详情页-----------
let router = useRouter();

function payOrderDetail (payOrderId) {
  router.push({ path: '/pay/pay-order-detail', query: { payOrderId } });
}

function getWaybillNumber (record) {
  if (!record.waybillNumber) {
    return '';
  }
  let waybillNumberArray = record.waybillNumber.split(',');
  let size = waybillNumberArray.length;
  if (size > 1) {
    return waybillNumberArray[0] + '等' + size + '条';
  }
  return waybillNumberArray[0];
}

// ----------- 支付-----------
const paymentModalRef = ref();

function showPayModal () {
  if (selectedRowKeyList.value.length == 0) {
    message.error('请选择一个付款单');
    return;
  }

  for (const item of tableData.value) {
    if (_.includes(selectedRowKeyList.value, item.payOrderId)) {
      if (item.auditStatus !== FLOW_AUDIT_STATUS_ENUM.PASS.value) {
        message.warning('请选择审核通过的应付账款！请移除【' + item.payOrderNumber + '】');
        return;
      }
    }
  }
  let enterpriseIdSet = new Set(selectedDataList.value.map(e => e.enterpriseId));
  if (enterpriseIdSet.size > 1) {
    message.error('请选择统一企业数据进行付款');
    return;
  }

  paymentModalRef.value.showModal(selectedDataList.value);
}

// ----------- 核销-----------

const verificationRef = ref();

function showVerificationModal () {
  if (selectedRowKeyList.value.length == 0) {
    message.error('请选择一个付款单');
    return;
  }
  for (const item of tableData.value) {
    if (_.includes(selectedRowKeyList.value, item.payOrderId)) {
      if (item.verificationFlag) {
        message.warning('请选择一个待核销状态的单据！请移除【' + item.payOrderNumber + '】');
        return;
      }
    }
  }
  let enterpriseIdSet = new Set(selectedDataList.value.map(e => e.enterpriseId));
  if (enterpriseIdSet.size > 1) {
    message.error('请选择统一企业数据进行付款');
    return;
  }
  verificationRef.value.showModal(selectedDataList.value);
}

// ------------- 作废 -------------
const payOrderCancelModalRef = ref();
function showCancelModal (payOrderId) {
  payOrderCancelModalRef.value.showModal(payOrderId);
}
// ------------- 审核 -------
const flowAuditRef = ref();
function showAuditModal(record) {
  const flowId = record.flowInstanceId;
  flowAuditRef.value.showModal(flowId);
}


// ----------- table 批量操作 start -----------
const selectedRowKeyList = ref([]);
const selectedDataList = ref([]);

function onSelectChange (keyArray, dataArray) {
  selectedRowKeyList.value = keyArray;
  selectedDataList.value = dataArray;
}

function clearSelected () {
  selectedRowKeyList.value = [];
}

// ----------------- 显示附件弹窗 --------------------
let showReceiptAttachmentFlag = ref(false);
let receiptAttachmentList = ref([])

function showReceiptAttachmentModal (record) {
  showReceiptAttachmentFlag.value = true;
  receiptAttachmentList.value = record.receiptAttachment || [];
}
// ------------ 导出 Excel -----------
function exportExcel () {
  SmartLoading.show();
  let params = _.cloneDeep(queryForm);
  params.orderTypeList = params.orderTypeList.join(',');
  payOrderApi.downloadExcel('应付账款明细表.xlsx', params);
  SmartLoading.hide();
}

</script>
<style scoped lang="less">
</style>

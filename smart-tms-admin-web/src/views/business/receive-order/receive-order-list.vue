<template>
  <a-form class="smart-query-form" v-privilege="'receiveOrderVerification:query'">
    <a-row class="smart-query-form-row">
      <a-form-item label="关键字" class="smart-query-form-item">
        <a-input
            style="width: 300px"
            v-model:value="queryForm.keywords"
            placeholder="收款单号/创建人/运单号"
        />
      </a-form-item>

      <a-form-item class="smart-query-form-item" label="货主">
        <ShipperSelect v-model:value="queryForm.shipperIdList" :multiple="true" placeholder="请选择货主"/>
      </a-form-item>

      <a-form-item label="销售" name="managerId">
        <employee-select
            ref="managerSelectRef"
            v-model:value="queryForm.saleIdList"
            :systemConfigKey="SYSTEM_CONFIG_KEY_ENUM.SALES_ROLE_CODE.value"
            mode="multiple"
            placeholder="请选择业务负责人"
            width="200px"
        />
      </a-form-item>

      <a-form-item class="smart-query-form-item" label="客服">
        <employee-select
            ref="customerServiceRef"
            v-model:value="queryForm.employeeIdList"
            :systemConfigKey="SYSTEM_CONFIG_KEY_ENUM.CUSTOMER_SERVICE_ROLE_CODE.value"
            mode="multiple"
            placeholder="请选择客服负责人"
            width="200px"
        />
      </a-form-item>

      <a-form-item label="是否需要开票" class="smart-query-form-item">
        <SmartBooleanSelect v-model:value="queryForm.makeInvoiceFlag"/>
      </a-form-item>

      <a-form-item label="是否逾期" class="smart-query-form-item">
        <SmartBooleanSelect v-model:value="queryForm.overdue"/>
      </a-form-item>

      <a-form-item label="账期">
        <a-range-picker
            v-model:value="accountPeriodDateRange"
            :presets="defaultTimeRanges"
            style="width: 100%"
            @change="changeAccountPeriod"
        />
      </a-form-item>

      <a-form-item label="创建时间">
        <a-range-picker
            v-model:value="createDateRange"
            :presets="defaultTimeRanges"
            style="width: 100%"
            @change="changeDate"
        />
      </a-form-item>

      <a-form-item class="smart-query-form-item" label="业务时间">
        <a-range-picker v-model:value="businessDateRange" picker="month" @change="changeBusinessDate"/>
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
        <a-button @click="confirmVerification()" v-privilege="'receiveOrderVerification:verification'"
                  :disabled="selectedRowKeyList.length == 0"
                  size="small" type="primary">确认核销
        </a-button>
        <a-button v-privilege="'receiveOrderVerification:batchVerification'" :disabled="selectedRowKeyList.length == 0"
                  size="small"
                  type="primary" @click="showBatchVerificationModal()">批量核销
        </a-button>
<!--        <a-button v-privilege="'receiveOrder:export'" size="small" @click="exportExcel()">开票明细导出</a-button>-->
        <a-button v-privilege="'receiveOrder:export'" size="small" @click="exportVerificationExcel()">核销明细导出</a-button>

        <a-button v-show="activeKey==FLAG_NUMBER_ENUM.FALSE.value" v-privilege="'receiveOrderVerification:cancel'" :disabled="selectedRowKeyList.length == 0"
                  size="small"
                  @click="cancel" >作废
        </a-button>
      </div>
      <div class="smart-table-setting-block"></div>
      <div>
        <SmartTableOperator v-model="columns" :tableId="TABLE_ID_CONST.RECEIVE_ORDER_VERIFICATION" :refresh="ajaxQuery" />
      </div>
    </a-row>
    <div class="smart-statistics smart-margin-top5">
      应收金额：<span class="amount">{{ formatMoneyStr(amountStatistics.receivableTotalAmount) }}</span>，
      已销金额：<span class="amount">{{ formatMoneyStr(amountStatistics.verificationTotalAmount) }}</span>元，
      未销金额：<span class="amount">{{ formatMoneyStr(amountStatistics.unpaidTotalAmount) }}</span>元，
      逾期金额：<span class="amount">{{ formatMoneyStr(amountStatistics.overdueTotalAmount) }}</span>元。
    </div>
    <a-modal v-model:open="visible" :width="400" title="作废" @close="onClose" @ok="onSubmit">
      <a-form>
        <a-form-item label="作废原因">
          <a-textarea v-model:value="remark" :rows="4" placeholder="请输入作废原因"/>
        </a-form-item>
      </a-form>
    </a-modal>

    <a-tabs v-model:activeKey="activeKey" @tabClick="changeTab">
      <a-tab-pane :key="FLAG_NUMBER_ENUM.FALSE.value" tab="未核销">
      </a-tab-pane>
      <a-tab-pane :key="FLAG_NUMBER_ENUM.TRUE.value" tab="已核销">
      </a-tab-pane>
      <a-tab-pane :key="null" tab="全部">
      </a-tab-pane>
    </a-tabs>
    <a-table
        :scroll="{ x: 3000,y:500 }"
        size="small"
        :dataSource="tableData"
        :columns="columns"
        :row-selection="{ selectedRowKeys: selectedRowKeyList, onChange: onSelectChange}"
        rowKey="receiveOrderId"
        :pagination="false"
        :loading="tableLoading"
        bordered
    >
      <template #bodyCell="{ column, record, text }">
        <template v-if="column.dataIndex === 'receiveOrderNumber'">
          <a @click="detailReceiveOrder(record.receiveOrderId)" type="link">{{ text }}</a><SmartCopyIcon :value="text" />
        </template>

        <template v-if="column.dataIndex === 'shortName'">
          <TextEllipsis :text="text" classKey="shortName"><a @click="detailConsignor(record.shipperId)" type="link">{{ text }}</a></TextEllipsis>
        </template>
        <template v-if="column.dataIndex === 'consignor'">
          <TextEllipsis :text="text" classKey="consignor"><a @click="detailConsignor(record.shipperId)" type="link">{{ text }}</a></TextEllipsis>
        </template>
        <template v-if="column.dataIndex === 'makeInvoiceFlag'">
          {{ text ? '是' : '否' }}
        </template>

        <template v-if="column.dataIndex === 'overdueDays'">
          {{ text==0 ? '未逾期':text}}
        </template>

        <template v-if="column.dataIndex === 'invoiceStatus'">
          {{ record.makeInvoiceFlag ? $smartEnumPlugin.getDescByValue('INVOICE_STATUS_ENUM', text): '无需开票' }}
        </template>
        <template v-if="column.dataIndex === 'businessDate'">
          {{ (record.businessDate || '').substring(0,7) }}
        </template>

        <template v-if="column.dataIndex === 'checkAttachment'">
          <file-preview :fileList="text"/>
        </template>
        <template v-if="column.dataIndex === 'billAttachment'">
          <file-preview :fileList="text"/>
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
          @change="ajaxQuery"
          @showSizeChange="ajaxQuery"
          :show-total="(total) => `共${total}条`"
      />
    </div>
    <VerificationModal ref="verificationRef" @refresh="refresh"/>

    <BatchVerificationModal ref="batchVerificationRef" @refresh="refresh"/>
  </a-card>
</template>
<script setup>
import VerificationModal from './components/verification-modal.vue';
import BatchVerificationModal from './components/batch-verification-modal.vue';
import FilePreview from '/@/components/file-preview/index.vue'
import EnterpriseSelect from '/@/components/enterprise-select/index.vue';
import SmartBooleanSelect from '/@/components/smart-boolean-select/index.vue';
import SmartTableOperator from '/@/components/smart-table-operator/index.vue';
import ShipperSelect from '/@/components/shipper-select/index.vue';
import EmployeeSelect from '/@/components/role-employee-select/index.vue';
import { TABLE_ID_CONST } from '/@/constants/table-id-const';
import { defaultTimeRanges } from '/@/lib/default-time-ranges';
import { reactive, ref, inject, onMounted } from 'vue';
import { receiveOrderApi } from '/@/api/business/receive-order/receive-order-api';
import { CHECK_STATUS_ENUM, INVOICE_STATUS_ENUM } from '/@/constants/business/receive-order-const';
import { PAGE_SIZE, PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import { router } from '/@/router/index';
import { FLAG_NUMBER_ENUM } from '/@/constants/common-const';
import { SYSTEM_CONFIG_KEY_ENUM } from '/@/constants/system/system-config-const';
import _ from 'lodash';
import dayjs from 'dayjs';
import { formatMoney } from '/@/utils/money-util';
import { message } from 'ant-design-vue';
import TextEllipsis from '/@/components/text-ellipsis/index.vue'
let smartEnumPlugin = inject('smartEnumPlugin');
// ----------- table 查询操作 start -----------
const columns = ref([
  {
    title: '收款单号',
    dataIndex: 'receiveOrderNumber',
    width: 150,
    fixed: 'left'
  },
  {
    title: '账期',
    dataIndex: 'accountPeriodDate',
    width: 100,
  },
  {
    title: '货主简称',
    dataIndex: 'shortName',
    width: 150,
    ellipsis: true,
  },
  {
    title: '货主全称',
    dataIndex: 'consignor',
    width: 150,
    ellipsis: true,
  },
  {
    title: '是否需要开票',
    dataIndex: 'makeInvoiceFlag',
    width: 110,
  },
  {
    title: '受票方抬头',
    dataIndex: 'invoiceName',
    width: 180,
  },
  {
    title: '是否开票',
    dataIndex: 'invoiceStatus',
    width: 100
  },
  {
    title: '业务负责人',
    dataIndex: 'managerName',
    width: 100
  },
  {
    title: '逾期天数',
    dataIndex: 'overdueDays',
    width: 80,
  },
  {
    title: '运费总额',
    dataIndex: 'freight',
    width: 80
  },
  {
    title: '异常费用总额',
    dataIndex: 'abnormalAmount',
    width: 110
  },
  {
    title: '应收总额',
    dataIndex: 'totalAmount',
    width: 80
  },
  {
    title: '已销金额',
    dataIndex: 'verificationAmount',
    width: 100,
  },
  {
    title: '未销金额',
    dataIndex: 'unpaidAmount',
    width: 100,
  },
  {
    title: '业务日期',
    dataIndex: 'businessDate',
    width: 100,
  },
  {
    title: '应收对账备注',
    dataIndex: 'remark',
    width: 200,
  },
  {
    title: '对账单',
    dataIndex: 'billAttachment',
    width: 200,
  },
  {
    title: '核算凭证',
    dataIndex: 'checkAttachment',
    width: 120,
  },
  {
    title: '创建人',
    dataIndex: 'createUserName',
    width: 60,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 170,
  },
]);

const queryFormState = {
  pageNum: 1,
  pageSize: PAGE_SIZE,
  keywords: '',
  enterpriseId:null,
  makeInvoiceFlag:null,
  overdue: null,
  startTime: '',
  endTime: '',
  accountPeriodStartTime: null,
  accountPeriodEndTime: null,
  checkStatus: CHECK_STATUS_ENUM.CHECK.value,
  invoiceStatus: INVOICE_STATUS_ENUM.INVOICE.value,
  shipperIdList: [],
  verificationFlag: FLAG_NUMBER_ENUM.FALSE.value,
  saleIdList: [],
  employeeIdList: [],
  businessDateBegin: null,
  businessDateEnd: null,
};
const queryForm = reactive({ ...queryFormState });
const createDateRange = ref([]);
const accountPeriodDateRange = ref([]);
const tableLoading = ref(false);
const tableData = ref([]);
const total = ref(0);

function resetQuery () {
  activeKey.value = FLAG_NUMBER_ENUM.FALSE.value;
  Object.assign(queryForm, queryFormState);
  createDateRange.value = [];
  businessDateRange.value = [];
  accountPeriodDateRange.value = [];
  ajaxQuery();
  statisticAmount();
}

function search () {
  queryForm.pageNum = 1;
  ajaxQuery();
  statisticAmount();
}

function refresh () {
  ajaxQuery();
  statisticAmount();
}

async function ajaxQuery () {
  try {
    tableLoading.value = true;
    let responseModel = await receiveOrderApi.queryPage(queryForm);
    const list = responseModel.data.list;
    total.value = responseModel.data.total;
    tableData.value = list;
    clearSelected();
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}

const amountStatistics = ref({
  receivableTotalAmount: 0,
  unpaidTotalAmount: 0,
  verificationTotalAmount: 0,
  overdueTotalAmount: 0,
});

async function statisticAmount () {
  try {
    let responseModel = await receiveOrderApi.statisticAmount(queryForm);
    amountStatistics.value = responseModel.data;
  } catch (e) {
    console.log(e);
  } finally {

  }
}

function changeDate (dates, dateStrings) {
  queryForm.startTime = dateStrings[0];
  queryForm.endTime = dateStrings[1];
}

function changeAccountPeriod (dates, dateStrings) {
  queryForm.accountPeriodStartTime = dateStrings[0];
  queryForm.accountPeriodEndTime = dateStrings[1];
}

// 业务时间
const businessDateRange = ref([]);

function changeBusinessDate (dates, dateStrings) {
  if (_.isEmpty(dates)) {
    queryForm.businessDateBegin = null;
    queryForm.businessDateEnd = null;
    return;
  }
  queryForm.businessDateBegin = dayjs(dates[0]).format('YYYY-MM-01');
  queryForm.businessDateEnd = dayjs(dates[1]).format('YYYY-MM-01');
}

const selectedRowKeyList = ref([]);
const selectedRowList = ref([]);

function onSelectChange (selectedRowKeys, selectedRows) {
  selectedRowKeyList.value = selectedRowKeys;
  selectedRowList.value = selectedRows;
}

function clearSelected () {
  selectedRowKeyList.value = [];
  selectedRowList.value = [];
}

function formatMoneyStr (value) {
  return formatMoney(value);
}
// ----------- table 数据操作 start -----------
// 核销
const verificationRef = ref();
function confirmVerification () {
  verificationRef.value.showModal(selectedRowList.value[0], selectedRowKeyList.value[0]);
}

// 作废
const visible = ref(false);
const remark = ref('');

async function cancel () {
  if (selectedRowKeyList.value.length !== 1) {
    message.error('请选择一个账单');
    return;
  }
  visible.value = true;
  remark.value = '';
}
function onClose () {
  visible.value = false;
}
async function onSubmit () {
  if (!remark.value) {
    message.error('请输入作废原因');
    return;
  }
  await receiveOrderApi.cancelVerification({ receiveOrderId: selectedRowKeyList.value[0], remark: remark.value });
  message.success('作废成功');
  ajaxQuery()
  visible.value = false;
}

// ----------- 批量核销 -----------
const batchVerificationRef = ref();

function showBatchVerificationModal () {
  batchVerificationRef.value.showModal(selectedRowKeyList.value);
}
// ----------- 导出 start -----------
function exportExcel () {
  let typeName = '';
  if(activeKey.value == null){
    typeName = '全部';
  }else{
    typeName = activeKey.value ? '已核销' : '未核销';
  }
  let param = _.cloneDeep(queryForm)
  param.shipperIdList = param.shipperIdList.join('');
  param.employeeIdList = param.employeeIdList.join('');
  receiveOrderApi.exportVerificationList(typeName, param);
}

function exportVerificationExcel () {
  let typeName = '';
  if(activeKey.value == null){
    typeName = '全部';
  }else{
    typeName = activeKey.value ? '已核销' : '未核销';
  }
  let param = _.cloneDeep(queryForm)
  param.shipperIdList = param.shipperIdList.join('');
  param.employeeIdList = param.employeeIdList.join('');
  receiveOrderApi.exportVerificationItemList(typeName, param);
}

// ----------- tab 变化
const activeKey = ref(FLAG_NUMBER_ENUM.FALSE.value);

function changeTab (value) {
  queryForm.verificationFlag = value;
  ajaxQuery();
}

// ----------- 页面跳转 start -----------
function detailReceiveOrder (receiveOrderId) {
  router.push({
    path: '/receive-order/detail',
    query: {
      receiveOrderId
    }
  });
}

// 跳转到货主详情
function detailConsignor (shipperId) {
  router.push({
    path: '/shipper/detail',
    query: {
      shipperId
    }
  });
}

// 订单详情
function detailOrder (orderId) {
  router.push({
    path: '/order/detail',
    query: {
      orderId
    }
  });
}

onMounted(search);
</script>

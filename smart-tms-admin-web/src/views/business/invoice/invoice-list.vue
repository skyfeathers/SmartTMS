<template>
  <a-form class="smart-query-form" v-privilege="'receiveOrderInvoice:query'">
    <a-row class="smart-query-form-row">
      <a-form-item label="关键字" class="smart-query-form-item">
        <a-input
            style="width: 300px"
            v-model:value="queryForm.keywords"
            placeholder="发票抬头/收款单号/创建人"
        />
      </a-form-item>

      <a-form-item label="货主" class="smart-query-form-item">
        <a-input
            style="width: 300px"
            v-model:value="queryForm.consignor"
            placeholder="货主简称"
        />
      </a-form-item>

      <a-form-item label="开票时间">
        <a-range-picker
            v-model:value="createDateRange"
            :presets="defaultTimeRanges"
            style="width: 100%"
            @change="changeDate"
        />
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
        <span v-show="queryForm.invoiceStatus == INVOICE_STATUS_ENUM.WAIT_INVOICE.value">
          <a-button v-privilege="'receiveOrderInvoice:cancel'" :disabled="selectedRowKeyList.length == 0"
                    size="small" type="primary" danger
                    @click="confirmCancel()">作废开票申请
          </a-button>
          <a-button v-privilege="'receiveOrderInvoice:invoice'" :disabled="selectedRowKeyList.length == 0"
                    size="small"
                    type="primary" @click="confirmInvoice()">开票
          </a-button>
          <a-button v-privilege="'receiveOrderInvoice:invoice'" :disabled="selectedRowKeyList.length == 0"
                    size="small"
                    type="primary" @click="batchConfirmInvoice()">批量开票
          </a-button>
        </span>

        <template v-if="queryForm.invoiceStatus == INVOICE_STATUS_ENUM.INVOICE.value">
          <a-button v-privilege="'receiveOrderInvoice:updateInvoice'" :disabled="selectedRowKeyList.length == 0"
                    size="small"
                    type="primary" @click="showUpdateInvoiceModal()">编辑
          </a-button>
        </template>

        <a-button v-show="activeKey == NON_INVOICE" v-privilege="'receiveOrderInvoice:uploadBill'"
                  :disabled="selectedRowKeyList.length == 0" @click="showUploadBill()"
                  size="small" type="primary">上传对账单
        </a-button>
      </div>
      <div class="smart-table-setting-block"></div>
      <div>
        <SmartTableOperator v-model="columns" :tableId="tableId" :refresh="ajaxQuery" />
      </div>
    </a-row>

    <a-tabs @tabClick="changeTab" v-model:activeKey="activeKey">
      <a-tab-pane :key="INVOICE_STATUS_ENUM.WAIT_INVOICE.value" tab="待开票">
      </a-tab-pane>
      <a-tab-pane :key="INVOICE_STATUS_ENUM.INVOICE.value" tab="已开票">
      </a-tab-pane>
      <a-tab-pane :key="INVOICE_STATUS_ENUM.CANCEL.value" tab="已作废">
      </a-tab-pane>
      <a-tab-pane :key="NON_INVOICE" tab="非开票业务">
      </a-tab-pane>
    </a-tabs>
    <a-table
        :scroll="{ x: 3000 }"
        size="small"
        :dataSource="tableData"
        :columns="columns"
        :row-selection="{ selectedRowKeys: selectedRowKeyList, onChange: onSelectChange}"
        :rowKey="rowKey"
        :pagination="false"
        :loading="tableLoading"
        bordered
    >
      <template #bodyCell="{record,text,index,column}">
        <template v-if="column.dataIndex === 'receiveOrderNumber'">
          <a type="link" @click="detailReceiveOrder(record.receiveOrderId)">{{ text }}</a><SmartCopyIcon :value="text" />
        </template>
        <template v-if="column.dataIndex === 'shortName'">
          <a type="link" @click="detailConsignor(record.shipperId)">{{ text }}</a>
        </template>
        <template v-if="column.dataIndex === 'makeInvoiceFlag'">
          {{ text ? '是' : '否' }}
        </template>
        <template v-if="column.dataIndex === 'invoiceStatus'">
          {{ $smartEnumPlugin.getDescByValue('INVOICE_STATUS_ENUM', text) }}
        </template>
        <template v-if="column.dataIndex === 'invoiceType'">
          {{ $smartEnumPlugin.getDescByValue('INVOICE_TYPE_ENUM', text) }}
        </template>
        <template v-if="column.dataIndex === 'invoiceKindType'">
          {{ $smartEnumPlugin.getDescByValue('INVOICE_KIND_TYPE_ENUM', text) }}
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
    <MakeInvoiceModal ref="makeInvoiceRef" @refresh="ajaxQuery"/>
    <CancelModal ref="cancelRef" @onSubmit="cancelInvoice" title="确认要作废该开票申请吗？"/>

    <!-- 上传对账单 -->
    <UploadBillModal ref="uploadBillRef"/>
    <!-- 更新开票信息 -->
    <UpdateInvoiceModal ref="updateInvoiceModal" @refresh="ajaxQuery"/>
  </a-card>
</template>
<script setup>
import MakeInvoiceModal from './components/make-invoice-modal.vue';
import UploadBillModal from './components/upload-bill-modal.vue';
import CancelModal from '../receive-order/components/cancel-modal.vue';
import SmartTableOperator from '/@/components/smart-table-operator/index.vue';
import FilePreview from '/@/components/file-preview/index.vue';
import EnterpriseSelect from '/@/components/enterprise-select/index.vue';
import UpdateInvoiceModal from './components/update-invoice-modal.vue';

import { TABLE_ID_CONST } from '/@/constants/table-id-const';
import { defaultTimeRanges } from '/@/lib/default-time-ranges';
import { reactive, ref, inject, onMounted, computed } from 'vue';
import { message, Modal } from 'ant-design-vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import { receiveOrderApi } from '/@/api/business/receive-order/receive-order-api';
import { PAGE_SIZE, PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import { router } from '/@/router/index';
import {CHECK_STATUS_ENUM, INVOICE_STATUS_ENUM} from '/@/constants/business/receive-order-const';
import { receiveOrderColumns, invoiceColumns, createColumns } from './invoice-columns';
import _ from 'lodash';

let smartEnumPlugin = inject('smartEnumPlugin');
// ----------- table 查询操作 start -----------
const NON_INVOICE = ref(10);

const queryFormState = {
  pageNum: 1,
  pageSize: PAGE_SIZE,
  keywords: '',
  consignor:'',
  invoiceStartTime: null,
  invoiceEndTime: null,
  invoiceStatus: INVOICE_STATUS_ENUM.WAIT_INVOICE.value,
  enterpriseId: null
};
const queryForm = reactive({ ...queryFormState });
const createDateRange = ref([]);
const accountPeriodDateRange = ref([]);
const tableLoading = ref(false);
const tableData = ref([]);
const total = ref(0);

function resetQuery () {
  Object.assign(queryForm, queryFormState);
  activeKey.value = INVOICE_STATUS_ENUM.WAIT_INVOICE.value;
  createDateRange.value = [];
  accountPeriodDateRange.value = [];
  ajaxQuery();
}

function search () {
  queryForm.pageNum = 1;
  ajaxQuery();
}

async function ajaxQuery () {
  try {
    tableLoading.value = true;
    let responseModel = await receiveOrderApi.queryInvoicePage(queryForm);
    const list = responseModel.data.list;
    total.value = responseModel.data.total;
    tableData.value = list;
    selectedRowKeyList.value = [];
    selectedRowList.value = [];
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}

function changeDate (dates, dateStrings) {
  queryForm.invoiceStartTime = dateStrings[0];
  queryForm.invoiceEndTime = dateStrings[1];
}

const selectedRowKeyList = ref([]);
const selectedRowList = ref([]);

function onSelectChange (selectedRowKeys, selectedRows) {
  selectedRowKeyList.value = selectedRowKeys;
  selectedRowList.value = selectedRows;
}
// ----------- table 数据操作 start -----------
// 确认开票
const makeInvoiceRef = ref();
function confirmInvoice () {
  makeInvoiceRef.value.showModal(selectedRowKeyList.value[0], []);
}

// 批量开票
function batchConfirmInvoice () {
  // let groupByData = _.groupBy(selectedRowList.value, 'shipperId');
  // if (Object.keys(groupByData).length > 1) {
  //   message.error('所属货主不一致，请重新选择');
  //   return;
  // }
  makeInvoiceRef.value.showModal(null, selectedRowKeyList.value);
}
// ----------- 上传对账单 -----------
// 上传对账单
const uploadBillRef = ref();

function showUploadBill () {
  uploadBillRef.value.showModal(selectedRowList.value[0].receiveOrderId);
}
// ----------- tab 变化
const activeKey = ref(INVOICE_STATUS_ENUM.WAIT_INVOICE.value);

const tableId = computed(() => {
  if (activeKey.value == INVOICE_STATUS_ENUM.WAIT_INVOICE.value) {
    return TABLE_ID_CONST.INVOICE_APPLY_WAIT_INVOICE;
  }
  if (activeKey.value == INVOICE_STATUS_ENUM.INVOICE.value) {
    return TABLE_ID_CONST.INVOICE_APPLY_ALREADY;
  }
  if (activeKey.value == INVOICE_STATUS_ENUM.CANCEL.value) {
    return TABLE_ID_CONST.INVOICE_APPLY_CANCEL;
  }
});
let rowKey = ref('receiveOrderInvoiceId');
let columns = ref([...receiveOrderColumns,...createColumns]);
function changeTab (value) {
  if(value == NON_INVOICE.value){
    rowKey.value = 'receiveOrderId';
  }else{
    rowKey.value = 'receiveOrderInvoiceId';
  }
  if (value == NON_INVOICE.value) {
    queryForm.invoiceStatus = null;
    queryForm.makeInvoiceFlag = false;
  } else {
    queryForm.makeInvoiceFlag = true;
    queryForm.invoiceStatus = value;
    if (queryForm.invoiceStatus == INVOICE_STATUS_ENUM.INVOICE.value) {
      columns.value = [...receiveOrderColumns, ...invoiceColumns, ...createColumns];
    } else {
      columns.value = [...receiveOrderColumns, ...createColumns];
    }
  }
  ajaxQuery();
}

const cancelRef = ref();

function confirmCancel () {
  cancelRef.value.showModal(selectedRowKeyList.value[0]);
}

async function cancelInvoice (params, receiveOrderInvoiceId) {
  try {
    useSpinStore().show();
    params.receiveOrderInvoiceId = receiveOrderInvoiceId;
    await receiveOrderApi.cancelInvoice(params);
    message.success('作废成功');
    ajaxQuery();
  } catch (e) {
    console.log(e);
  } finally {
    useSpinStore().hide();
  }
}

// ----------- 修改开票信息 start -----------
const updateInvoiceModal = ref();

function showUpdateInvoiceModal () {
  if (_.isEmpty(selectedRowList.value)) {
    message.error('请选择要修改的单据');
    return;
  }
  if (selectedRowList.value.length > 1) {
    message.error('请选择一条单据');
    return;
  }
  updateInvoiceModal.value.showModal(selectedRowList.value[0]);
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

onMounted(ajaxQuery);
</script>

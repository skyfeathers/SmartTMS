<!-- 应收帐款 ---  财务核算页面 -->
<template>
  <a-form class="smart-query-form" v-privilege="'receiveOrderCheck:query'">
    <a-row class="smart-query-form-row">
      <a-form-item label="关键字" class="smart-query-form-item">
        <a-input
            style="width: 300px"
            v-model:value="queryForm.keywords"
            placeholder="收款单号/创建人"
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

      <a-form-item class="smart-query-form-item" label="开票状态">
      <SmartEnumSelect v-model:value="queryForm.invoiceStatus" enum-name="INVOICE_STATUS_ENUM" placeholder="请选择开票状态"
                       width="100%"/>
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
        <span v-show="queryForm.checkStatus == CHECK_STATUS_ENUM.WAIT_CHECK.value">
          <!-- 只有待核算才展示按钮 -->
          <a-button v-privilege="'receiveOrderCheck:cancel'" :disabled="selectedRowKeyList.length == 0" size="small" @click="confirmCancel()" type="primary" danger >作废账单</a-button>
          <a-button v-privilege="'receiveOrderCheck:confirmCheck'" :disabled="selectedRowKeyList.length == 0" size="small" @click="confirmCheck()">确认核算</a-button>
        </span>
        <a-button v-privilege="'receiveOrderCheck:export'" size="small" @click="exportExcel()">导出</a-button>
      </div>

      <div class="smart-table-setting-block"></div>
      <div>
        <SmartTableOperator v-model="columns" :tableId="tableId" :refresh="ajaxQuery" />
      </div>
    </a-row>

    <a-tabs @tabClick="changeTab" v-model:activeKey="activeKey">
      <a-tab-pane :key="CHECK_STATUS_ENUM.WAIT_CHECK.value" tab="待核算">
      </a-tab-pane>
      <a-tab-pane :key="CHECK_STATUS_ENUM.CHECK.value" tab="已核算">
      </a-tab-pane>
      <a-tab-pane :key="CHECK_STATUS_ENUM.CANCEL.value" tab="已作废">
      </a-tab-pane>
    </a-tabs>
    <a-table
        :scroll="{ x: '100%' }"
        size="small"
        :dataSource="tableData"
        :columns="columns"
        :row-selection="{ selectedRowKeys: selectedRowKeyList, onChange: onSelectChange,type:'radio'}"
        rowKey="receiveOrderId"
        :pagination="false"
        :loading="tableLoading"
        bordered
    >
      <template #bodyCell="{ column, record, text }">
        <template v-if="column.dataIndex === 'receiveOrderNumber'">
          <a @click="detailReceiveOrder(record.receiveOrderId)" type="link">{{ text }}</a>
          <SmartCopyIcon :value="text"/>
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

        <template v-if="column.dataIndex === 'invoiceStatus'">
          <span v-if="record.makeInvoiceFlag">{{$smartEnumPlugin.getDescByValue('INVOICE_STATUS_ENUM', text)}}</span>
          <span v-else>无需开票</span>
        </template>

        <template v-if="column.dataIndex === 'invoiceType'">
          {{ $smartEnumPlugin.getDescByValue('INVOICE_TYPE_ENUM', text) }}
        </template>

        <template v-if="column.dataIndex === 'invoiceKindType'">
          {{ $smartEnumPlugin.getDescByValue('INVOICE_KIND_TYPE_ENUM', text) }}
        </template>

        <template v-if="column.dataIndex === 'businessDate'">
          {{ (record.businessDate || '').substring(0, 7) }}
        </template>

        <template v-if="column.dataIndex === 'attachment'">
          <file-preview :fileList="record.attachment"/>
        </template>

        <template v-if="column.dataIndex === 'cancelRemark'">
          <span v-if="record.checkStatus == CHECK_STATUS_ENUM.CANCEL.value">已作废<span v-if="record.checkRemark">（ {{ record.checkRemark }}）</span></span>
          <template v-if="record.invoiceStatus == INVOICE_STATUS_ENUM.CANCEL.value">
            <span v-if="record.invoiceRemark">开票申请被作废（ {{ record.invoiceRemark }}）</span>
          </template>
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
    <CheckModal ref="checkRef" @refresh="ajaxQuery"/>
    <CancelModal ref="cancelRef" title="确认要作废该对账单吗？" @onSubmit="cancelReceiveOrder"/>
  </a-card>
</template>
<script setup>
import CheckModal from './components/check-modal.vue';
import SmartBooleanSelect from '/@/components/smart-boolean-select/index.vue';
import EnterpriseSelect from '/@/components/enterprise-select/index.vue';
import CancelModal from './components/cancel-modal.vue';
import SmartTableOperator from '/@/components/smart-table-operator/index.vue';
import ShipperSelect from '/@/components/shipper-select/index.vue';
import EmployeeSelect from '/@/components/role-employee-select/index.vue';
import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';
import FilePreview from '/@/components/file-preview/index.vue';

import { TABLE_ID_CONST } from '/@/constants/table-id-const';
import { defaultTimeRanges } from '/@/lib/default-time-ranges';
import { reactive, ref, inject, onMounted, computed } from 'vue';
import { message, Modal } from 'ant-design-vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import { receiveOrderApi } from '/@/api/business/receive-order/receive-order-api';
import { PAGE_SIZE, PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import { CHECK_STATUS_ENUM, INVOICE_STATUS_ENUM } from '/@/constants/business/receive-order-const';
import { router } from '/@/router/index';
import { checkColumns, cancelColumns, checkCreateColumns } from './receive-order-columns';
import { SYSTEM_CONFIG_KEY_ENUM } from '/@/constants/system/system-config-const';
import dayjs from "dayjs";
import _ from 'lodash';
import TextEllipsis from '/@/components/text-ellipsis/index.vue'
let smartEnumPlugin = inject('smartEnumPlugin');

const queryFormState = {
  pageNum: 1,
  pageSize: PAGE_SIZE,
  keywords: '',
  startTime: '',
  endTime: '',
  makeInvoiceFlag: null,
  invoiceType: null,
  invoiceKindType: null,
  checkStatus: CHECK_STATUS_ENUM.WAIT_CHECK.value,
  enterpriseId: null,
  shipperIdList: [],
  employeeIdList: [],
  saleIdList: [],
  invoiceStatus: null,
  businessDateBegin: null,
  businessDateEnd: null,
};
const queryForm = reactive({ ...queryFormState });
const createDateRange = ref([]);
const tableLoading = ref(false);
const tableData = ref([]);
const total = ref(0);

function resetQuery () {
  activeKey.value = CHECK_STATUS_ENUM.WAIT_CHECK.value;
  Object.assign(queryForm, queryFormState);
  createDateRange.value = [];
  businessDateRange.value = [];
  ajaxQuery();
}

function search () {
  queryForm.pageNum = 1;
  ajaxQuery();
}

async function ajaxQuery () {
  try {
    tableLoading.value = true;
    let responseModel = await receiveOrderApi.queryPage(queryForm);
    const list = responseModel.data.list;
    total.value = responseModel.data.total;
    tableData.value = list;
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}

function changeDate (dates, dateStrings) {
  queryForm.startTime = dateStrings[0];
  queryForm.endTime = dateStrings[1];
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

function onSelectChange (selectedRowKeys) {
  selectedRowKeyList.value = selectedRowKeys;
}

// ----------- table 数据操作 start -----------
// 确认对账 核算
const checkRef = ref();

function confirmCheck () {
  checkRef.value.showModal(selectedRowKeyList.value[0]);
}

// 作废应收对账
const cancelRef = ref();
function confirmCancel () {
  let receiveOrderId = selectedRowKeyList.value[0];
  let find = tableData.value.find(e => e.receiveOrderId == receiveOrderId);
  if (!find) {
    message.error('数据不存在');
    return;
  }
  if (find.checkStatus == CHECK_STATUS_ENUM.CHECK.value) {
    message.error('已核算不能作废');
    return;
  }
  cancelRef.value.showModal(selectedRowKeyList.value[0]);
}

async function cancelReceiveOrder (params, receiveOrderId) {
  try {
    useSpinStore().show();
    params.receiveOrderId = receiveOrderId;
    await receiveOrderApi.cancelReceiveOrder(params);
    message.success('作废成功');
    ajaxQuery();
  } catch (e) {
    console.log(e);
  } finally {
    useSpinStore().hide();
  }
}

// ----------- tab 变化
const activeKey = ref(CHECK_STATUS_ENUM.WAIT_CHECK.value);

const tableId = computed(() => {
  if (activeKey.value == CHECK_STATUS_ENUM.WAIT_CHECK.value) {
    return TABLE_ID_CONST.RECEIVE_ORDER_WAIT_CHECK;
  }
  if (activeKey.value == CHECK_STATUS_ENUM.CHECK.value) {
    return TABLE_ID_CONST.RECEIVE_ORDER_CHECK;
  }
  if (activeKey.value == CHECK_STATUS_ENUM.CANCEL.value) {
    return TABLE_ID_CONST.RECEIVE_ORDER_CANCEL;
  }
  return '';
});

let columns = ref([...checkColumns, ...checkCreateColumns]);

function changeTab (value) {
  queryForm.checkStatus = value;
  if (queryForm.checkStatus == CHECK_STATUS_ENUM.CANCEL.value) {
    columns.value = [...checkColumns, ...cancelColumns, ...checkCreateColumns];
  } else {
    columns.value = [...checkColumns, ...checkCreateColumns];
  }
  ajaxQuery();
}

// ----------- 导出 start -----------
function exportExcel () {
  let typeName = smartEnumPlugin.getDescByValue('CHECK_STATUS_ENUM', activeKey.value);
  receiveOrderApi.exportCheckList(typeName, queryForm);
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

onMounted(ajaxQuery);
</script>

<template>
  <a-form v-privilege="'shipperReport:query'" class="smart-query-form">
    <a-row class="smart-query-form-row">
      <a-form-item class="smart-query-form-item" label="创建时间">
        <a-range-picker :allowClear="false" v-model:value="searchDate" @change="dateChange" :presets="defaultTimeRanges" />
      </a-form-item>
      <a-form-item class="smart-query-form-item" label="货主">
        <ShipperSelect v-model:value="queryForm.shipperId" placeholder="请选择货主" />
      </a-form-item>
      <a-form-item class="smart-query-form-item" label="销售">
        <employee-select ref="salesRef" multiple v-model:value="queryForm.managerIdList"
          :systemConfigKey="SYSTEM_CONFIG_KEY_ENUM.SALES_ROLE_CODE.value" placeholder="请选择销售员" width="200px" />
      </a-form-item>
      <a-form-item class="smart-query-form-item smart-margin-left10">
        <a-button type="primary" @click="search">
          <template #icon>
            <SearchOutlined />
          </template>
          查询
        </a-button>
        <a-button @click="resetQuery">
          <template #icon>
            <ReloadOutlined />
          </template>
          重置
        </a-button>
      </a-form-item>
    </a-row>
  </a-form>

  <a-card :bordered="false" :hoverable="true" size="small">
    <a-row class="smart-table-btn-block">
      <div class="smart-table-operate-block">
        <a-button v-privilege="'shipperReport:export'" size="small" @click="exportExcel()">导出</a-button>
      </div>
    </a-row>
    <a-table :scroll="{ x: 2200 }" :columns="columns" :dataSource="tableData" :loading="tableLoading" :pagination="false"
      bordered size="small">
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'consignor'">
          <a @click="shipperDetail(record.shipperId)">{{ text }}</a>
        </template>
        <template v-if="column.dataIndex === 'rate'">{{ record.rate }}%</template>
        <template v-if="column.dataIndex === 'action'">
          <a-button  @click="showReceive(record.shipperId)" type="link">查看应收</a-button>
        </template>
      </template>
    </a-table>
    <div class="smart-query-table-page">
      <a-pagination v-model:current="queryForm.pageNum" v-model:pageSize="queryForm.pageSize"
        :defaultPageSize="queryForm.pageSize" :pageSizeOptions="PAGE_SIZE_OPTIONS" :show-total="(total) => `共${total}条`"
        :total="total" show-less-items showQuickJumper showSizeChanger @change="ajaxQuery" @showSizeChange="ajaxQuery" />
    </div>
  </a-card>

  <ReceiveList ref="receiveListRef"></ReceiveList>
</template>
<script setup>
import EmployeeSelect from '/@/components/role-employee-select/index.vue';
import ReceiveList from './receive-list.vue';
import { SmartLoading } from '/@/components/smart-loading';
import { onMounted, reactive, ref } from 'vue';
import { PAGE_SIZE, PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import { useRouter } from 'vue-router';
import dayjs from 'dayjs';
import { dailyReportApi } from '/@/api/business/report/daily-report-api';
import _ from 'lodash';
import ShipperSelect from '/@/components/shipper-select/index.vue';
import { defaultTimeRanges } from '/@/lib/default-time-ranges';
import { SYSTEM_CONFIG_KEY_ENUM } from '/@/constants/system/system-config-const';
const queryFormState = {
  pageSize: PAGE_SIZE,
  pageNum: 1,
  startDate: null,
  endDate: null,
  shipperId: null,
  managerIdList: [],
};
const queryForm = reactive({ ...queryFormState });
// 日期选择
let searchDate = ref();
function dateChange(dates, dateStrings) {
  queryForm.startDate = dateStrings[0];
  queryForm.endDate = dateStrings[1];
}
function resetQuery() {
  Object.assign(queryForm, queryFormState);
  searchDate.value = [];
  let startMonth = dayjs(); //.startOf('month');
  let endMonth = dayjs(); //.endOf('month');
  searchDate.value = [startMonth, endMonth];
  queryForm.startDate = startMonth.format('YYYY-MM-DD');
  queryForm.endDate = endMonth.format('YYYY-MM-DD');
  ajaxQuery();
}
function search() {
  queryForm.pageNum = 1;
  ajaxQuery();
}

// 表格列
let columns = reactive([
  {
    title: '货主名称',
    dataIndex: 'consignor',
    width: 240,
    ellipsis: true,
  },
  {
    title: '下单数量',
    dataIndex: 'waybillCount',
  },
  {
    title: '下单金额',
    dataIndex: 'receiveTotalAmount',
    width: 300,
  },
  {
    title: '异常运单数量',
    dataIndex: 'cancelOrderCount',
  },
  {
    title: '应收金额',
    dataIndex: 'receiveAmount',
  },
  {
    title: '运费金额',
    dataIndex: 'freightAmount',
  },
  {
    title: '异常金额',
    dataIndex: 'exceptionAmount',
  },
  {
    title: '工资',
    dataIndex: 'salaryAmount'
  },
  {
    title: '在途费用',
    dataIndex: 'carCostAmount'
  },
  {
    title: '税率',
    dataIndex: 'taxRate',
  },
  {
    title: '利润',
    dataIndex: 'profitAmount',
  },
  {
    title: '去年同期运单数量',
    dataIndex: 'lastYearWaybillCount',
  },
  {
    title: '去年同期下单金额',
    dataIndex: 'lastYearWReceiveTotalAmount',
  },
  {
    title: '比例',
    dataIndex: 'rate',
  },
  {
    title: '应收账款金额',
    dataIndex: 'receiveCostAmount',
  },
  {
    title: '已开票金额',
    dataIndex: 'invoiceAmount',
  },
  {
    title: '未开票金额',
    dataIndex: 'waitInvoiceAmount',
  },
  {
    title: '预计收回金额',
    dataIndex: 'unRecoveredAmount',
  },
  {
    title: '业务负责人',
    dataIndex: 'managerName',
  },
  {
    title: '操作',
    dataIndex: 'action',
    fixed: 'right',
    width: 100,
  }
]);

const tableLoading = ref(false);
const tableData = ref([]);
const total = ref(0);

async function ajaxQuery() {
  try {
    tableLoading.value = true;
    let responseModel = await dailyReportApi.querySipper(queryForm);
    const list = responseModel.data.list;
    tableData.value = list;
    total.value = responseModel.data.total;
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}

let receiveListRef = ref();
function showReceive(shipperId) {
  receiveListRef.value.showModal(shipperId)
}

// ------------ 导出 Excel -----------
function exportExcel() {
  SmartLoading.show();
  let param = _.cloneDeep(queryForm)
  param.managerIdList = (param.managerIdList || []).join(',');
  dailyReportApi.downShipper(param);
  SmartLoading.hide();
}
let router = useRouter();
function shipperDetail(shipperId) {
  router.push({ path: '/shipper/detail', query: { shipperId } });
}

onMounted(() => {
  let startMonth = dayjs(); //.startOf('month');
  let endMonth = dayjs(); //.endOf('month');
  searchDate.value = [startMonth, endMonth];
  queryForm.startDate = startMonth.format('YYYY-MM-DD');
  queryForm.endDate = endMonth.format('YYYY-MM-DD');
  ajaxQuery();
});
</script>
<style lang="less" scoped></style>

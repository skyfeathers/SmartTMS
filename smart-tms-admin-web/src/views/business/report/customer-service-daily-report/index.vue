<template>
  <a-form v-privilege="'customerServiceReport:query'" class="smart-query-form">
    <a-row class="smart-query-form-row">
      <a-form-item class="smart-query-form-item" label="创建时间">
        <a-range-picker
          :allowClear="false"
          v-model:value="searchDate"
          @change="dateChange"
          :presets="defaultTimeRanges"
        />
      </a-form-item>
      <a-form-item class="smart-query-form-item" label="员工">
        <employee-select
          ref="salesRef"
          multiple
          v-model:value="queryForm.customerServiceIdList"
          :systemConfigKey="SYSTEM_CONFIG_KEY_ENUM.CUSTOMER_SERVICE_ROLE_CODE.value"
          placeholder="请选择员工"
          width="200px"
        />
      </a-form-item>
      <!--      <a-form-item class="smart-query-form-item" label="所属部门">
        <DepartmentSelect v-model:value="queryForm.departmentId" placeholder="请选择所属部门"/>
      </a-form-item>-->
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
        <a-button v-privilege="'customerServiceReport:export'" size="small" @click="exportExcel()">导出</a-button>
      </div>
    </a-row>
    <a-table
      :scroll="{ x: 2200}"
      :columns="columns"
      :dataSource="tableData"
      :loading="tableLoading"
      :pagination="false"
      bordered
      size="small"
    >
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'rate'">{{record.rate}}%</template>
      </template>
    </a-table>
    <!-- <div class="smart-query-table-page">
      <a-pagination
          v-model:current="queryForm.pageNum"
          v-model:pageSize="queryForm.pageSize"
          :defaultPageSize="queryForm.pageSize"
          :pageSizeOptions="PAGE_SIZE_OPTIONS"
          :show-total="(total) => `共${total}条`"
          :total="total"
          show-less-items
          showQuickJumper
          showSizeChanger
          @change="ajaxQuery"
          @showSizeChange="ajaxQuery"
      />
    </div>-->
  </a-card>
</template>
<script setup>
import { SmartLoading } from '/@/components/smart-loading';
import { onMounted, reactive, ref } from 'vue';
import { PAGE_SIZE, PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import { useRouter } from 'vue-router';
import dayjs from 'dayjs';
import { dailyReportApi } from '/@/api/business/report/daily-report-api';
import _ from 'lodash';
import EmployeeSelect from '/@/components/role-employee-select/index.vue';
import { SYSTEM_CONFIG_KEY_ENUM } from '/@/constants/system/system-config-const';
import DepartmentSelect from '/@/components/department-select/index.vue';
import { defaultTimeRanges } from '/@/lib/default-time-ranges';
const queryFormState = {
  // pageSize: PAGE_SIZE,
  // pageNum: 1,
  startDate: null,
  endDate: null,
  customerServiceIdList: [],
  departmentId: null,
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
  // queryForm.pageNum = 1;
  ajaxQuery();
}

// 表格列
let columns = reactive([
  {
    title: '员工',
    dataIndex: 'employeeName',
  },
  {
    title: '所属部门',
    dataIndex: 'departmentName',
  },
  {
    title: '订单量',
    dataIndex: 'orderCount',
  },
  {
    title: '运单量',
    dataIndex: 'waybillCount',
  },
  {
    title: '运单金额-应收',
    dataIndex: 'totalReceiveAmount',
  },
  {
    title: '运单金额-应付',
    dataIndex: 'totalPayableAmount',
  },
  {
    title: '异常订单量',
    dataIndex: 'cancelOrderCount',
  },
  {
    title: '提交应付单量',
    dataIndex: 'payOrderCount',
  },
  {
    title: '应付金额',
    dataIndex: 'payableAmount',
  },
  {
    title: '去年同期运单量',
    dataIndex: 'lastYearWaybillCount',
  },
  {
    title: '同期运单金额',
    dataIndex: 'lastYearWaybillAmount',
  },
  {
    title: '比例',
    dataIndex: 'rate',
  },
]);

const tableLoading = ref(false);
const tableData = ref([]);
const total = ref(0);

async function ajaxQuery() {
  try {
    tableLoading.value = true;
    let responseModel = await dailyReportApi.queryCustomerService(queryForm);
    const list = responseModel.data;
    tableData.value = list;
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}

// ------------ 导出 Excel -----------
function exportExcel() {
  SmartLoading.show();
  let param = _.cloneDeep(queryForm)
  param.customerServiceIdList = (param.customerServiceIdList || []).join(',');
  dailyReportApi.downloadCustomerService(param);
  SmartLoading.hide();
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
<style lang="less" scoped>
</style>

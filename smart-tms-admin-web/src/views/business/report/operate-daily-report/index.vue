<template>
  <a-form v-privilege="'operateDailyReport:query'" class="smart-query-form">
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
          v-model:value="queryForm.scheduleIdList"
          :systemConfigKey="SYSTEM_CONFIG_KEY_ENUM.SCHEDULE_ROLE_CODE.value"
          placeholder="请选择员工"
          width="200px"
        />
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
        <a-button v-privilege="'operateDailyReport:export'" size="small" @click="exportExcel()">导出</a-button>
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
      <template #headerCell="{ column }">
        <template v-if="column.dataIndex === 'inCompleteOrderCount'">
          <span>
            异常数量
            <a-tooltip placement="top">
              <template #title>
                <span>查询未分配完运单的订单数量</span>
              </template>
              <question-circle-outlined />
            </a-tooltip>
          </span>
        </template>
      </template>
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'oilCardRate'">{{text}}%</template>
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
import { defaultTimeRanges } from '/@/lib/default-time-ranges';
const queryFormState = {
  // pageSize: PAGE_SIZE,
  // pageNum: 1,
  startDate: null,
  endDate: null,
  scheduleIdList: [],
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
    width: 80,
  },
  {
    title: '所属部门',
    dataIndex: 'departmentName',
  },
  {
    title: '运单量',
    dataIndex: 'waybillCount',
  },
  {
    title: '派车金额',
    dataIndex: 'freightAmount',
  },
  {
    title: '油卡金额',
    dataIndex: 'oilCardAmount',
  },
  {
    title: '油卡占比',
    dataIndex: 'oilCardRate',
  },
  {
    title: '异常数量',
    dataIndex: 'inCompleteOrderCount',
  },
  {
    title: '去年同期派车量',
    dataIndex: 'lastYearWaybillCount',
  },
  {
    title: '去年同期运单金额',
    dataIndex: 'lastYearFreightAmount',
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
    let responseModel = await dailyReportApi.querySchedule(queryForm);
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
  param.scheduleIdList = (param.scheduleIdList || []).join(',');
  dailyReportApi.downloadSchedule(param);
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

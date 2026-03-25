<template>
  <a-form v-privilege="'salesDailyReport:query'" class="smart-query-form">
    <a-row class="smart-query-form-row">
      <a-form-item class="smart-query-form-item" label="创建时间">
        <a-range-picker :allowClear="false" v-model:value="searchDate" picker="month" @change="dateChange" />
      </a-form-item>
      <a-form-item class="smart-query-form-item" label="销售">
        <employee-select ref="salesRef" multiple v-model:value="queryForm.salesIdList"
          :systemConfigKey="SYSTEM_CONFIG_KEY_ENUM.SALES_ROLE_CODE.value" placeholder="请选择销售员" width="200px" />
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
        <a-button v-privilege="'salesDailyReport:export'" size="small" @click="exportExcel()">导出</a-button>
      </div>
    </a-row>
    <a-table :scroll="{ x: 2200 }" :columns="columns" :dataSource="tableData" :loading="tableLoading" :pagination="false"
      bordered size="small">
      <template #headerCell="{ column }">
        <template v-if="column.dataIndex === 'profitAmount'">
          <span>
            税后毛利额
            <a-tooltip placement="top">
              <template #title>
                <span>税后毛利额 = 已完成 / 1.09 - 短驳金额 - 运费金额 - 油卡金额 - 异常费用</span>
              </template>
              <question-circle-outlined />
            </a-tooltip>
          </span>
        </template>
      </template>
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'completeRate'">{{ record.completeRate }}%</template>
        <template v-if="column.dataIndex === 'profitRate'">{{ record.profitRate }}%</template>
        <template v-if="column.dataIndex === 'rate'">{{ record.rate }}%</template>
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
  salesIdList: [],
  departmentId: null,
};
const queryForm = reactive({ ...queryFormState });
function dateChange(dates, dateStrings) {
  let year = 0;
  let month = 0;
  queryForm.startDate = dateStrings[0] + '-01';
  year = dateStrings[1].split('-')[0]
  month = dateStrings[1].split('-')[1]
  queryForm.endDate = dateStrings[1] + `-${getLastDayOfMonth(year, month)}`;
}

function getLastDayOfMonth(year, month) {
  return new Date(year, month, 0).getDate();
}

function resetQuery() {
  Object.assign(queryForm, queryFormState);
  searchDate.value = [];
  let startMonth = dayjs().startOf('month');
  let endMonth = dayjs().endOf('month');
  searchDate.value = [startMonth, endMonth];
  queryForm.startDate = startMonth.format('YYYY-MM-DD');
  queryForm.endDate = endMonth.format('YYYY-MM-DD');
  ajaxQuery();
}
let searchDate = ref();
function search() {
  // queryForm.pageNum = 1;
  ajaxQuery();
}

// 表格列
let columns = reactive([
  {
    title: '销售',
    dataIndex: 'employeeName',
    fixed: 'left',
  },
  {
    title: '所属部门',
    dataIndex: 'departmentName',
    fixed: 'left',
  },
  {
    title: '月指标',
    dataIndex: 'targetAmount',
  },
  {
    title: '已完成',
    dataIndex: 'completedAmount',
  },
  {
    title: '未完成',
    dataIndex: 'inCompleteAmount',
  },
  {
    title: '完成比例',
    dataIndex: 'completeRate',
  },
  {
    title: '去年同期',
    dataIndex: 'lastYearCompletedAmount',
  },
  {
    title: '比例',
    dataIndex: 'rate',
  },
  {
    title: '运单量',
    dataIndex: 'waybillCount',
  },
  {
    title: '运费金额',
    dataIndex: 'freightAmount',
  },
  {
    title: '油卡金额',
    dataIndex: 'oilCardAmount',
  },
  {
    title: '异常费用',
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
    title: '税后毛利额',
    dataIndex: 'profitAmount',
  },
  {
    title: '毛利率',
    dataIndex: 'profitRate',
  },
]);

const tableLoading = ref(false);
const tableData = ref([]);
const total = ref(0);

async function ajaxQuery() {
  try {
    tableLoading.value = true;
    let responseModel = await dailyReportApi.query(queryForm);
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
  param.salesIdList = (param.salesIdList || []).join(',');
  dailyReportApi.downloadSaleDayStatistic(param);
  SmartLoading.hide();
}

onMounted(() => {
  let startMonth = dayjs().startOf('month');
  let endMonth = dayjs().endOf('month');
  searchDate.value = [startMonth, endMonth];
  queryForm.startDate = startMonth.format('YYYY-MM-DD');
  queryForm.endDate = endMonth.format('YYYY-MM-DD');
  ajaxQuery();
});
</script>
<style lang="less" scoped></style>

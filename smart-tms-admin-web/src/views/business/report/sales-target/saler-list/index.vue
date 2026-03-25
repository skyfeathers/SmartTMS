<template>
  <a-form v-privilege="'salesList:query'" class="smart-query-form">
    <a-row class="smart-query-form-row">
      <a-form-item class="smart-query-form-item" label="业务时间">
        <a-space :size="12" direction="vertical">
          <a-date-picker :allowClear="false" :value="queryDate" picker="month" @change="dateChange" />
        </a-space>
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

  <a-card :bordered="false" :hoverable="true" size="small">
    <a-row class="smart-table-btn-block">
      <div class="smart-table-operate-block">
        <a-button v-privilege="'salesList:export'" size="small">导出</a-button>
      </div>
      <div class="smart-table-setting-block"></div>
    </a-row>

    <a-table
        :columns="columns"
        :dataSource="tableData"
        :loading="tableLoading"
        :pagination="false"
        :scroll="{ x: 1300 }"
        bordered
        rowKey="employeeId"
        size="small"
    >
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'completeRate'">
          {{record.completeRate}}%
        </template>
        <template v-if="column.dataIndex === 'lastYearCompleteRate'">
          {{record.lastYearCompleteRate}}%
        </template>
      </template>
    </a-table>

  </a-card>
</template>
<script setup>
import { reactive, ref, onMounted } from 'vue';
import { employeeBusinessApi } from '/@/api/business/report/employee-business-api';
import dayjs from 'dayjs';
import EnterpriseSelect from '/@/components/enterprise-select/index.vue';

const columns = reactive([
  {
    title: '姓名',
    dataIndex: 'employeeName'
  },
  // {
  //   title: '所属公司',
  //   dataIndex: 'enterpriseName'
  // },
  {
    title: '月度目标',
    dataIndex: 'targetAmount'
  },
  {
    title: '完成目标',
    dataIndex: 'completeAmount'
  },
  {
    title: '完成率',
    dataIndex: 'completeRate'
  },
  {
    title: '去年同期目标',
    dataIndex: 'lastYearTargetAmount'
  },
  {
    title: '去年同期完成目标',
    dataIndex: 'lastYearCompleteAmount'
  },
  {
    title: '去年同期完成率',
    dataIndex: 'lastYearCompleteRate'
  }
]);

const queryFormState = {
  keywords: '',
  queryDate: dayjs().format('YYYY-MM-01'),
  enterpriseId: null
};
const queryForm = reactive({ ...queryFormState });
const tableLoading = ref(false);
const tableData = ref([]);

// 日期选择
let queryDate = ref(dayjs());

function dateChange (date, dateStrings) {
  queryDate.value = date;
  queryForm.queryDate = dayjs(date).format('YYYY-MM-01');
}

function resetQuery () {
  queryDate.value = dayjs();
  Object.assign(queryForm, queryFormState);
  ajaxQuery();
}

function search () {
  queryForm.pageNum = 1;
  ajaxQuery();
}

async function ajaxQuery () {
  try {
    tableLoading.value = true;
    let responseModel = await employeeBusinessApi.querySalesList(queryForm);
    const list = responseModel.data;
    tableData.value = list;
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}

onMounted(ajaxQuery);
</script>

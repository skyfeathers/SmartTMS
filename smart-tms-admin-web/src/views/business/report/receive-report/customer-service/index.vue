<template>
  <a-form class="smart-query-form" v-privilege="'customerServiceReceiveReport:query'">
    <a-row class="smart-query-form-row">
      <a-form-item label="客服负责人" >
        <employee-select
            ref="customerServiceRef"
            v-model:value="queryForm.employeeId"
            :systemConfigKey="SYSTEM_CONFIG_KEY_ENUM.CUSTOMER_SERVICE_ROLE_CODE.value"
            placeholder="请选择客服负责人"
            width="200px"
        />
      </a-form-item>

      <a-form-item label="业务时间" class="smart-query-form-item">
        <a-space direction="vertical" :size="12">
          <a-date-picker :value="queryDate" @change="dateChange" picker="month" :allowClear="false" />
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

  <a-card size="small" :bordered="false" :hoverable="true">
    <a-row class="smart-table-btn-block">
      <div class="smart-table-operate-block">
        <a-button @click="exportExcel" v-privilege="'customerServiceReceiveReport:query'" size="small">导出</a-button>
      </div>
      <div class="smart-table-setting-block"></div>
    </a-row>

    <a-table
        :scroll="{ x: 1300 }"
        size="small"
        :dataSource="tableData"
        :columns="columns"
        rowKey="employeeId"
        :pagination="false"
        :loading="tableLoading"
        bordered
    >
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'costAmount'">
          --
        </template>
        <template v-if="column.dataIndex === 'profitRate'">
          {{record.profitRate}}%
        </template>

      </template>
    </a-table>

  </a-card>
</template>
<script setup>
import { reactive, ref, onMounted } from 'vue';
import { employeeBusinessApi } from '/@/api/business/report/employee-business-api';
import dayjs from 'dayjs';
import EmployeeSelect from '/@/components/role-employee-select/index.vue';
import { SYSTEM_CONFIG_KEY_ENUM } from '/@/constants/system/system-config-const';
import { waybillReportApi } from '/@/api/business/report/waybill-report-api';
import { SmartLoading } from '/@/components/smart-loading';

const columns = reactive([
  {
    title: '客服负责人',
    dataIndex: 'employeeName'
  },
  {
    title: '业务应收',
    dataIndex: 'receiveAmount'
  },
  {
    title: '业务应付',
    dataIndex: 'payableAmount'
  },
  {
    title: '费用支出',
    dataIndex: 'costAmount'
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
    title: '税金',
    dataIndex: 'taxAmount'
  },
  {
    title: '毛利润',
    dataIndex: 'profitAmount'
  },
  {
    title: '毛利率',
    dataIndex: 'profitRate'
  }
]);

const queryFormState = {
  employeeId: null,
  queryDate: dayjs().format('YYYY-MM-01'),
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
    let responseModel = await waybillReportApi.queryAmountByCustomerService(queryForm);
    const list = responseModel.data;
    tableData.value = list;
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}

function exportExcel(){
  SmartLoading.show();
  waybillReportApi.downloadAmountByCustomerService(queryForm);
  SmartLoading.hide();
}
onMounted(ajaxQuery);
</script>

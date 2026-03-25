<template>
  <a-form v-privilege="'waybillReceiveAmount:query'" class="smart-query-form">
    <a-row class="smart-query-form-row">
      <a-form-item class="smart-query-form-item" label="业务时间">
        <a-range-picker :allowClear="false" :value="businessDateList" picker="month" @change="dateChange"/>
      </a-form-item>
      <a-form-item class="smart-query-form-item" label="销售">
        <employee-select
            ref="managerSelectRef" v-model:value="queryForm.employeeIdList"
            :systemConfigKey="SYSTEM_CONFIG_KEY_ENUM.SALES_ROLE_CODE.value"
            multiple
            placeholder="请选择销售"
            width="200px"
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

  <a-card :bordered="false" :hoverable="true" size="small">
    <a-row class="smart-table-btn-block">
      <div class="smart-table-operate-block">
        <a-button v-privilege="'waybillReceiveAmount:export'" size="small" @click="exportExcel()">导出</a-button>
      </div>
    </a-row>
    <a-table
        :columns="columns"
        :dataSource="tableData"
        :loading="tableLoading"
        :pagination="false"
        bordered
        size="small"
    >
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'receiveAmountRate'">
          {{ text}}%
        </template>
      </template>
    </a-table>
  </a-card>

</template>
<script setup>
import { defaultTimeRanges } from '/@/lib/default-time-ranges';
import { SmartLoading } from '/@/components/smart-loading';

import { onMounted, reactive, ref } from 'vue';
import { PAGE_SIZE, PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import { useRouter } from 'vue-router';
import dayjs from 'dayjs';
import { waybillReportApi } from '/@/api/business/report/waybill-report-api';
import EmployeeSelect from '/@/components/role-employee-select/index.vue';
import _ from 'lodash';
import { SYSTEM_CONFIG_KEY_ENUM } from '/@/constants/system/system-config-const';

const queryFormState = {
  pageSize: PAGE_SIZE,
  pageNum: 1,
  businessStartDate: null,
  businessEndDate: null,
  employeeIdList: [],
};

const queryForm = reactive({ ...queryFormState });

function resetQuery () {
  Object.assign(queryForm, queryFormState);
  businessDateList.value = [dayjs(), dayjs()];
  queryForm.businessStartDate = dayjs(businessDateList.value[0]).format('YYYY-MM-01');
  queryForm.businessEndDate = dayjs(businessDateList.value[1]).format('YYYY-MM-01');
  ajaxQuery();
}

function search () {
  queryForm.pageNum = 1;
  ajaxQuery();
}

// 表格列
let columns = reactive([
  {
    title: '销售人员',
    dataIndex: 'employeeName',
  },
  {
    title: '应收金额',
    dataIndex: 'receiveAmount',
  },
  {
    title: '应收占比',
    dataIndex: 'receiveAmountRate',
  },
]);

const tableLoading = ref(false);
const tableData = ref([]);
const total = ref(0);

async function ajaxQuery () {
  try {
    tableLoading.value = true;
    let responseModel = await waybillReportApi.queryWaybillReceiveAmount(queryForm);
    const list = responseModel.data;
    tableData.value = list;
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}

//创建时间
let businessDateList = ref([dayjs(), dayjs()]);
queryForm.businessStartDate = dayjs(businessDateList.value[0]).format('YYYY-MM-01');
queryForm.businessEndDate = dayjs(businessDateList.value[1]).format('YYYY-MM-01');

function dateChange (dateList, dateStrings) {
  businessDateList.value = dateList;
  queryForm.businessStartDate = dayjs(dateList[0]).format('YYYY-MM-01');
  queryForm.businessEndDate = dayjs(dateList[1]).format('YYYY-MM-01');
}

// ------------ 导出 Excel -----------
function exportExcel () {
  SmartLoading.show();
  waybillReportApi.downloadWaybillReceiveAmount(queryForm);
  SmartLoading.hide();
}

let router = useRouter();

function waybillDetail (waybillId) {
  router.push({ path: '/waybill/waybill-detail', query: { waybillId } });
}

onMounted(() => {
  ajaxQuery();
});

</script>
<style lang="less" scoped>
</style>

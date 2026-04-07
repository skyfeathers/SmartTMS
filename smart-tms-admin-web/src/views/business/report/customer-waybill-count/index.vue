<template>
  <a-form v-privilege="'customerWaybillCount:query'" class="smart-query-form">
    <a-row class="smart-query-form-row">
      <a-form-item class="smart-query-form-item" label="客服">
        <employee-select
            ref="customerServiceRef"
            v-model:value="queryForm.employeeIdList"
            :systemConfigKey="SYSTEM_CONFIG_KEY_ENUM.CUSTOMER_SERVICE_ROLE_CODE.value"
            multiple
            placeholder="请选择客服"
            width="200px"
        />
      </a-form-item>

      <a-form-item class="smart-query-form-item" label="业务类型">
        <SmartEnumSelect v-model:value="queryForm.tripTypeList" enumName="TRANSPORT_MODE_ENUM" multiple
                         placeholder="业务类型" width="200px"/>
      </a-form-item>

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
        <a-button v-privilege="'customerWaybillCount:export'" size="small" @click="exportExcel">导出</a-button>
      </div>
      <div class="smart-table-setting-block"></div>
    </a-row>

    <a-table
        :columns="columns"
        :dataSource="tableData"
        :loading="tableLoading"
        :pagination="false"
        :scroll="{ x: 1500 }"
        bordered
        rowKey="employeeId"
        size="small"
    >
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'oilCardRate'">
          {{record.oilCardRate}}%
        </template>

      </template>
    </a-table>

  </a-card>
</template>
<script setup>
import { reactive, ref, onMounted, inject } from 'vue';
import { employeeBusinessApi } from '/@/api/business/report/employee-business-api';
import dayjs from 'dayjs';
import EmployeeSelect from '/@/components/role-employee-select/index.vue';
import { SYSTEM_CONFIG_KEY_ENUM } from '/@/constants/system/system-config-const';
import { waybillReportApi } from '/@/api/business/report/waybill-report-api';
import { SmartLoading } from '/@/components/smart-loading';
import _ from 'lodash';
import { customExport } from '/@/utils/custom-export-util';
import { message } from 'ant-design-vue';
import { defaultColumns } from '/@/views/business/order-import/components/import-columns';
import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';

const smartEnumPlugin = inject('smartEnumPlugin');
const initColumns = [
  {
    title: '姓名',
    dataIndex: 'employeeName',
    fixed: 'left'
  },
  {
    title: '类型/日期',
    dataIndex: 'tripTypeDesc',
    fixed: 'left'
  },
  {
    title: '合计',
    dataIndex: 'total',
    width: 50,
    fixed: 'left'
  }
];

const queryFormState = {
  employeeIdList: [],
  tripTypeList: [],
  startLoadTime: dayjs().format('YYYY-MM-01'),
  endLoadTime: dayjs().endOf('month').format('YYYY-MM-DD'),
};
const queryForm = reactive({ ...queryFormState });
const tableLoading = ref(false);
const tableData = ref([]);

// 日期选择
let queryDate = ref(dayjs());

function dateChange (date, dateStrings) {
  queryDate.value = date;
  queryForm.startLoadTime = dayjs(date).format('YYYY-MM-01');
  queryForm.endLoadTime = dayjs(date).endOf('month').format('YYYY-MM-DD')
}

function resetQuery () {
  queryDate.value = dayjs();
  queryForm.startLoadTime = dayjs().format('YYYY-MM-01');
  queryForm.endLoadTime = dayjs().endOf('month').format('YYYY-MM-DD')
  Object.assign(queryForm, queryFormState);
  ajaxQuery();
}

function search () {
  ajaxQuery();
}

async function ajaxQuery () {
  try {
    tableLoading.value = true;
    let responseModel = await waybillReportApi.queryCustomerWaybillCount(queryForm);
    const list = responseModel.data;
    buildList(list);
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}

const columns = ref([]);
let daysInMonthColumn = [];
function buildColumns () {
  let daysInMonth = dayjs(queryForm.startLoadTime).daysInMonth();

  daysInMonthColumn = [];
  for (let i = 1; i <= daysInMonth; i++) {
    daysInMonthColumn.push({
      title: i,
      dataIndex: i.toString(),
      width: 40
    });
  }
  columns.value = initColumns.concat(daysInMonthColumn);
}


function buildList (list) {
  let employeeGroup = _.groupBy(list,'employeeId');

  let result = [];
  for (let [employeeId, waybillCountList] of Object.entries(employeeGroup)) {
    let typeGroup = _.groupBy(waybillCountList, 'tripType');
    const { employeeName} = waybillCountList[0];

    // 横向合计列
    let totalData = {
      employeeName,
      tripTypeDesc: '小计'
    };

    // 组装员工列表数据
    let employeeTableList = [];
    for (let [type, typeList] of Object.entries(typeGroup)) {
      let total = typeList.map(e => e.waybillCount || 0).reduce((a, b) => {
        return a + b;
      });
      let data = {
        employeeName,
        tripTypeDesc: typeList[0].tripTypeDesc,
        total
      };

      // 设置小计列的合计
      totalData.total = (totalData.total || 0) + data.total;

      for (let dayInfo of daysInMonthColumn) {
        let dataIndex = dayInfo.dataIndex;
        let find = typeList.find(typeInfo => dayjs(typeInfo.loadTime).date().toString() == dataIndex);
        data[dataIndex] = find ? find.waybillCount : 0;

        // 设置小计列每天的总数
        totalData[dataIndex] = (totalData[dataIndex] || 0) + data[dataIndex];
      }
      employeeTableList.push(data);
    }

    result = result.concat(employeeTableList).concat(totalData);
  }
  tableData.value = result;
}

function exportExcel(){
  if (_.isEmpty(tableData.value)) {
    message.error('没有可供导出的数据');
    return;
  }
  customExport(tableData.value, columns.value, '客服日推进表.xlsx');
}

onMounted(() => {
  buildColumns();
  ajaxQuery();
});
</script>

<template>
  <a-alert :closable="true" message="当未选择任何车辆时，系统默认查询所有自有车的费用情况" type="warning" show-icon>
    <template #icon><smile-outlined /></template>
  </a-alert>
  <div class="view-box">
    <a-form v-privilege="'carCostReport:query'" class="smart-query-form">
      <a-row class="smart-query-form-row">
        <a-form-item class="smart-query-form-item" label="车辆">
          <VehicleSelect v-model:value="queryForm.vehicleIdList" multiple width="150px"/>
        </a-form-item>


        <a-form-item class="smart-query-form-item" label="日期">
          <a-range-picker v-model:value="createDateRange" :presets="defaultTimeRanges" style="width: 100%"
                          :allowClear="false" picker="month" @change="changeDate"/>
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

        <a-form-item class="smart-query-form-item smart-margin-left10">
          <a-button v-privilege="'carCostReport:export'" @click="exportExcel()">导出</a-button>
        </a-form-item>
      </a-row>
    </a-form>

    <a-card :bordered="false" :hoverable="true" size="small">
      <a-row class="smart-table-btn-block">
        <div class="smart-table-operate-block">
        </div>
        <div class="smart-table-setting-block"></div>
        <div>
         <SmartTableOperator v-model="columns" :tableId="TABLE_ID_CONST.OWN_CAR_COST_MONTH" :refresh="ajaxQuery"/>
        </div>
      </a-row>

      <a-table
          :columns="columns"
          :dataSource="tableData"
          :pagination="false"
          :scroll="{ x: tableWidth, y: tableheight }"
          bordered
          rowKey=""
          size="small"
      >
        <template #bodyCell="{ text, record, index, column }">
          <template v-if="column.dataIndex === 'action'">
            <a-button v-privilege="'carCostReport:detail'" size="small" type="link" @click="detail(record.vehicleId)">明细</a-button>
            <a-button v-privilege="'carCostReport:export'" size="small" type="link" @click="exportDetailExcel(record.vehicleId)" >导出</a-button>
          </template>
        </template>
      </a-table>

    </a-card>
  </div>
</template>
<script setup>
import VehicleSelect from '/@/components/vehicle-select/index.vue';
import SmartTableOperator from '/@/components/smart-table-operator/index.vue';

import { onMounted, reactive, ref, computed, inject, nextTick } from 'vue';
import { SmartLoading } from '/@/components/smart-loading';
import { defaultTimeRanges } from '/@/lib/default-time-ranges';
import { TABLE_ID_CONST } from '/@/constants/table-id-const';
import { waybillCarCostReportApi } from '/@/api/business/report/car-cost-report-api';
import _ from 'lodash';
import dayjs from 'dayjs';
import useDragTable from '/@/hook/use-drag-table/index';
import { useRouter } from 'vue-router';

const smartEnumPlugin = inject('smartEnumPlugin');


// --------------------- computed ------------------------
const { columnsData:columns, tableWidth } =useDragTable([
  {
    title: '车辆',
    dataIndex: 'vehicleNumber',
    fixed: 'left'
  },
  {
    title: '应收运费）',
    width: 170,
    dataIndex: 'receiveFrightAmount'
  },
  {
    title: '高速里程',
    width: 170,
    dataIndex: 'highSpeedMileage'
  },
  {
    title: '低速里程',
    width: 170,
    dataIndex: 'lowSpeedMileage'
  },
  {
    title: '总公里数',
    width: 130,
    dataIndex: 'gpsMileage'
  },
  {
    title: '维修费用',
    dataIndex: 'repairAmount'
  },
  {
    title: '保养费用',
    dataIndex: 'maintenanceAmount'
  },
  {
    title: '保险费用',
    dataIndex: 'insuranceAmount'
  },
  {
    title: '审车费用',
    dataIndex: 'reviewAmount'
  },
  {
    title: '用油量/升',
    dataIndex: 'oilConsumption'
  },
  {
    title: '支出',
    dataIndex: 'payAmount'
  },
  {
    title: '利润',
    dataIndex: 'profitAmount'
  },
  {
    title: '现金',
    dataIndex: 'cashPayAmount'
  },
  {
    title: '油费-专卡',
    dataIndex: 'dieselOilPayAmount'
  },
  {
    title: '油费-普卡',
    dataIndex: 'gasolineOilPayAmount'
  },
  {
    title: 'etc',
    dataIndex: 'etcPayAmount'
  },
  {
    title: '尿素',
    dataIndex: 'ureaPayAmount'
  },
  {
    title: '油耗升/100km',
    width: 140,
    dataIndex: 'oilConsumptionRate'
  },  {
    title: '成本占比(%)',
    dataIndex: 'payRate'
  },
  {
    title: '油耗占比(%)',
    dataIndex: 'oilCardRate'
  },
  {
    title: '尿素占比(%)',
    dataIndex: 'ureaRate'
  },
  {
    title: 'ETC占比(%)',
    dataIndex: 'roadRate'
  },
  {
    title: '工资占比(%)',
    dataIndex: 'salaryRate'
  },
  {
    title: '其他占比(%)',
    dataIndex: 'otherRate'
  },
  {
    title: '利润占比(%)',
    dataIndex: 'profitRate'
  },
  {
    title: '操作',
    dataIndex: 'action',
    fixed: 'right'
  }
],TABLE_ID_CONST.OWN_CAR_COST_MONTH);

// --------------------- 列表查询 ------------------------
const queryFormState = {
  keywords: '',
  vehicleIdList: [],
  enterpriseId: null,
  startTime: null,
  endTime: null
};
const createDateRange = ref([dayjs(), dayjs().endOf('month')]);

const queryForm = reactive({ ...queryFormState });

queryForm.startTime = dayjs(createDateRange.value[0]).format('YYYY-MM-01');
queryForm.endTime = dayjs(createDateRange.value[1]).endOf('month').format('YYYY-MM-DD');

function resetQuery () {
  Object.assign(queryForm, queryFormState);
  createDateRange.value = [dayjs(), dayjs()];
  queryForm.startTime = dayjs(createDateRange.value[0]).format('YYYY-MM-01');
  queryForm.endTime = dayjs(createDateRange.value[1]).endOf('month').format('YYYY-MM-DD');
  ajaxQuery();
}

function search () {
  ajaxQuery();
}

const tableData = ref([]);

async function ajaxQuery () {
  try {
    SmartLoading.show();
    let responseModel = await waybillCarCostReportApi.queryCarCostMonthStatistic(queryForm);
    const list = responseModel.data;
    tableData.value = list;
  } catch (e) {
    console.log(e);
  } finally {
    SmartLoading.hide();
  }
}

function changeDate (dates, dateStrings) {
  queryForm.startTime = dayjs(dates[0]).format('YYYY-MM-01');
  queryForm.endTime = dayjs(dates[1]).endOf('month').format('YYYY-MM-DD');
}

// ------------ 导出 Excel -----------
function exportExcel () {
  let params = _.cloneDeep(queryForm);
  waybillCarCostReportApi.downloadMonthList(params);
}

function exportDetailExcel (vehicleId) {
  let params = _.cloneDeep(queryForm);
  params.vehicleId = vehicleId;
  waybillCarCostReportApi.downloadDayList(params);
}

// ----------------- 跳转 --------------------
let router = useRouter();

function detail (vehicleId) {
  let {
    enterpriseId,
    startTime,
    endTime
  } = queryForm
  router.push({
    path: '/car-cost/day-stastics',
    query: {
      vehicleId,
      enterpriseId,
      startTime,
      endTime
    }
  });
}

const tableheight = ref()

onMounted(() => {
  let fh = document.querySelector('.view-box').clientHeight
  let bh =  document.querySelector(".smart-query-form").clientHeight
  tableheight.value = fh - bh - 108;
  ajaxQuery();
});
</script>
<style lang="less" scoped>
  .view-box{
    height: 100%;
  }
</style>

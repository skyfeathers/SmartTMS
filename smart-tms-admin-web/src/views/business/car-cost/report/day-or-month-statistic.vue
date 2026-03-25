<template>
  <a-form class="smart-query-form">
    <a-row class="smart-query-form-row">
      <a-form-item class="smart-query-form-item" label="按月查询">
        <a-switch v-model:checked="month" @change="change"/>
      </a-form-item>
      <a-form-item class="smart-query-form-item" label="创建时间">
        <a-range-picker :allowClear="false" :value="createDateList" :picker="month ? 'month' : ''" @change="dateChange"/>
      </a-form-item>
      <a-form-item class="smart-query-form-item" label="车辆">
        <VehicleSelect multiple v-model:value="queryForm.vehicleIdList" placeholder="请选择车辆" width="200px"/>
      </a-form-item>

      <a-form-item class="smart-query-form-item" label="异常标识">
        <smart-enum-select v-model:value="queryForm.exceptionFlag" enumName="FLAG_NUMBER_ENUM" placeholder="请选择异常标识"/>
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
        <template v-if="column.dataIndex === 'index'">
          {{ index + 1 }}
        </template>
        <!-- 如果当前值不存在，则使用默认值 -->
        <template v-if="column.default">
          <span v-if="record[column.dataIndex]">{{ record[column.dataIndex] }}</span>
          <span v-else>{{ column.defaultValue }}</span>
        </template>
        <template v-if="column.showType == 'custom'">
          {{ getColumnValue(column.dataIndex, record) }}
          <a-tooltip v-if="column.remarkDataIndex && getColumnValue(column.remarkDataIndex, record)" placement="top">
            <template #title>
              <span>
                {{ getColumnValue(column.remarkDataIndex, record) }}
              </span>
            </template>
            <question-circle-outlined/>
          </a-tooltip>
        </template>
      </template>
    </a-table>
  </a-card>
</template>
<script setup>

import { onMounted, reactive, ref, computed, nextTick } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import dayjs from 'dayjs';
import VehicleSelect from '/@/components/vehicle-select/index.vue';
import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';
import _ from 'lodash';
import { waybillCarCostReportApi } from '/@/api/business/report/car-cost-report-api';
import useDragTable from '/@/hook/use-drag-table/index';

const month = ref(true);

function change(){
  if(month.value){
    createDateList.value = [dayjs(), dayjs()];
    queryForm.startTime = dayjs(createDateList.value[0]).format('YYYY-MM-01');
    queryForm.endTime = dayjs(createDateList.value[1]).endOf('month').format('YYYY-MM-DD');
    ajaxQuery();
  }else{
    createDateList.value = [dayjs(), dayjs()];
    queryForm.startTime = dayjs(createDateList.value[0]).format('YYYY-MM-DD');
    queryForm.endTime = dayjs(createDateList.value[1]).format('YYYY-MM-DD');
    ajaxQuery();
  }
}

const queryFormState = {
  startTime: null,
  endTime: null,
  vehicleIdList: [],
  exceptionFlag: null,
};

const queryForm = reactive({ ...queryFormState });

function resetQuery () {
  Object.assign(queryForm, queryFormState);
  createDateList.value = [dayjs(), dayjs()];
  queryForm.startTime = dayjs(createDateList.value[0]).format('YYYY-MM-01');
  queryForm.endTime = dayjs(createDateList.value[1]).endOf('month').format('YYYY-MM-DD');
  ajaxQuery();
}

function search () {
  ajaxQuery();
}


// 表格列
const { columnsData:columns } = useDragTable([
    {
      title: '支出',
      width: 100,
      dataIndex: 'expenditure'
    },
    {
      title: '收入',
      width: 100,
      dataIndex: 'income'
    },
    {
      title: '毛利',
      width: 100,
      dataIndex: 'profit'
    },
    {
      title: '车辆ID',
      width: 100,
      dataIndex: 'vehicleId'
    },
    {
      title: '车牌号',
      width: 100,
      dataIndex: 'vehicleNumber'
    }
  ]);

const tableLoading = ref(false);
const tableData = ref([]);
const total = ref(0);

async function ajaxQuery () {
  try {
    tableLoading.value = true;
    let responseModel = await waybillCarCostReportApi.queryCarCostDayOrMonthStatistic(queryForm);
    const list = responseModel.data;
    tableData.value = list;
  } catch (e) {
    console.log(e);
    tableData.value = [];
  } finally {
    tableLoading.value = false;
  }
}

function buildShowList (list) {
  list.forEach(item => {
    let cashInfoVO = item.cashInfoVO;
    if (!cashInfoVO) {
      return;
    }
    let itemInfoVOList = cashInfoVO.itemInfoVOList || [];
    if (_.isEmpty(itemInfoVOList)) {
      return;
    }
    itemInfoVOList.forEach(category => {
      item.cashInfoVO[category.categoryId] = category.amount;
      if (category.remark) {
        item.cashInfoVO[`${category.categoryId}_remark`] = category.remark;
      }
    });
  });
}

//创建时间
let createDateList = ref([dayjs(), dayjs().endOf('month')]);
queryForm.startTime = dayjs(createDateList.value[0]).format('YYYY-MM-01');
queryForm.endTime = dayjs(createDateList.value[1]).format('YYYY-MM-DD');

function dateChange (dateList, dateStrings) {
  createDateList.value = dateList;
  queryForm.startTime = dayjs(dateList[0]).format('YYYY-MM-01');
  queryForm.endTime = dayjs(dateList[1]).endOf('month').format('YYYY-MM-DD');
}

// ------------ 导出 Excel -----------
function exportExcel () {
  let data = {
    ...queryForm,
    vehicleIdList:queryForm.vehicleIdList.join(',')
  }
  waybillCarCostReportApi.downloadDayList(data);
}

function exportFlowExcel () {
  let data = {
    ...queryForm,
    vehicleIdList:queryForm.vehicleIdList.join(',')
  }
  waybillCarCostReportApi.downloadFlowList(data);
}

let router = useRouter();
let route = useRoute();

function waybillDetail (waybillId) {
  router.push({ path: '/waybill/waybill-detail', query: { waybillId } });
}

// ------------ 导出 Excel -----------
function getColumnValue (keyList, record) {
  let dataIndex = _.cloneDeep(keyList);
  if (!record) {
    return '';
  }
  let dataIndexType = typeof dataIndex;
  if (dataIndexType == 'string') {
    return record[dataIndex];
  }
  if (dataIndex.length == 1) {
    return record[dataIndex[0]];
  }

  let currentKey = dataIndex[0];
  dataIndex.shift(0);
  let value = record[currentKey];
  if (typeof value != 'object') {
    return value;
  }
  return getColumnValue(dataIndex, record[currentKey]);
}

function getValueByKey (key, record) {
  let value = record[key];
  let valueType = typeof value;
  if(valueType !== 'object'){
    return value;
  }
  getValueByKey();
}

onMounted(() => {
  if (route.query.vehicleId) {
    queryForm.vehicleIdList = [Number(route.query.vehicleId)];
  }
  if (route.query.enterpriseId) {
    queryForm.enterpriseId = Number(route.query.enterpriseId);
  }
  if (route.query.startTime && route.query.endTime) {
    createDateList.value = [dayjs(route.query.startTime), dayjs(route.query.endTime)];
    queryForm.startTime = route.query.startTime;
    queryForm.endTime = route.query.endTime;
  }
  ajaxQuery();
});

</script>
<style lang="less" scoped>
</style>

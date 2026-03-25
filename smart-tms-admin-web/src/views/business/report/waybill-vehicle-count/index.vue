<template>
  <a-form v-privilege="'waybillVehicleCount:query'" class="smart-query-form">
    <a-row class="smart-query-form-row">
      <a-form-item class="smart-query-form-item" label="业务时间">
        <a-range-picker :allowClear="false" :value="businessDateList" picker="month" @change="dateChange"/>
      </a-form-item>
      <a-form-item class="smart-query-form-item" label="车辆">
        <VehicleSelect v-model:value="queryForm.vehicleIdList" :multiple="true" placeholder="请选择车辆" width="200px"/>
      </a-form-item>

      <a-form-item class="smart-query-form-item" label="经营方式">
        <smart-enum-select v-model:value="queryForm.waybillSettleMode" enumName="SELECT_SETTLE_MODE_ENUM" placeholder="请选择经营方式"/>
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
        <a-button v-privilege="'waybillVehicleCount:export'" size="small" @click="exportExcel()">导出</a-button>
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
        <template v-if="column.dataIndex === 'index'">
          {{ (queryForm.pageSize * (queryForm.pageNum - 1)) + index + 1 }}
        </template>
        <template v-if="column.dataIndex === 'driverNameList'">
          {{ showDriverName(record.driverNameList) }}
        </template>
      </template>
    </a-table>

    <div class="smart-query-table-page">
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
    </div>
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
import VehicleSelect from '/@/components/vehicle-select/index.vue';
import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';
import _ from 'lodash';

const queryFormState = {
  pageSize: PAGE_SIZE,
  pageNum: 1,
  businessStartDate: null,
  businessEndDate: null,
  vehicleIdList: [],
  waybillSettleMode: null,// 结算方式
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
    title: '车牌号',
    dataIndex: 'vehicleNumber',
  },
  {
    title: '运单数量',
    dataIndex: 'waybillCount',
  },
  {
    title: '司机',
    dataIndex: 'driverNameList',
  },
  {
    title: '排行',
    dataIndex: 'index',
  },
]);

const tableLoading = ref(false);
const tableData = ref([]);
const total = ref(0);

async function ajaxQuery () {
  try {
    tableLoading.value = true;
    let responseModel = await waybillReportApi.queryWaybillVehicleCount(queryForm);
    const list = responseModel.data.list;
    total.value = responseModel.data.total;
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

function showDriverName (driverNameList) {
  if (_.isEmpty(driverNameList)) {
    return '';
  }
  if (driverNameList.length > 2) {
    driverNameList.length = 2;
    return `${driverNameList.join(',')}...`;
  }
  return driverNameList.join(',');
}

// ------------ 导出 Excel -----------
function exportExcel () {
  SmartLoading.show();
  waybillReportApi.downloadWaybillVehicleCount(queryForm);
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

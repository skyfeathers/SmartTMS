<template>
  <a-form class="smart-query-form" v-privilege="'shipperOrderReport:query'">
    <a-row class="smart-query-form-row">
      <a-form-item label="关键字" class="smart-query-form-item">
        <a-input
            style="width: 200px"
            v-model:value="queryForm.keywords"
            placeholder="货主名称"
        />
      </a-form-item>
      <a-form-item label="年份" class="smart-query-form-item">
        <a-date-picker :value="chooseYear"
                       :allowClear="false"
                       picker="year"
                       :open="openYearFlag"
                       @openChange="handleOpenChange"
                       @change="handlePanelChange"/>

<!--        :open="openYearFlag"
        @openChange="handleOpenChange"
        @change="handlePanelChange"-->
      </a-form-item>

      <a-form-item class="smart-query-form-item smart-margin-left10">
        <a-button-group>
          <a-button type="primary"
                    @click="search">
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
        </a-button-group>

      </a-form-item>
      <a-button @click="exportExcel()" class="smart-margin-left10" v-privilege="'shipperOrderReport:export'">导出</a-button>
    </a-row>
  </a-form>
  <a-card size="small" :bordered="false" :hoverable="true">
    <a-tabs @tabClick="changeTab" v-model:activeKey="activeKey">
      <a-tab-pane key="waybill" tab="运单量" />
      <a-tab-pane key="order" tab="订单量" />
    </a-tabs>

    <a-table class="table-list"
             :scroll="{ x: 1500, y:600 }"
             size="small"
             :dataSource="tableData"
             :columns="columns"
             :pagination="false"
             :loading="tableLoading"
             rowKey="shipperId"
             bordered>
    </a-table>

    <div class="smart-query-table-page">
      <a-pagination
          showSizeChanger
          showQuickJumper
          show-less-items
          :pageSizeOptions="PAGE_SIZE_OPTIONS"
          :defaultPageSize="queryForm.pageSize"
          v-model:current="queryForm.pageNum"
          v-model:pageSize="queryForm.pageSize"
          :total="total"
          @change="init"
          @showSizeChange="init"
          :show-total="(total) => `共${total}条`"
      />
    </div>
  </a-card>
</template>
<script setup>
import { reactive, onMounted, ref } from 'vue';
import _ from 'lodash';
import { shipperReportApi } from '/@/api/business/report/shipper-report-api';
import {FLAG_NUMBER_ENUM, PAGE_SIZE, PAGE_SIZE_OPTIONS} from '/@/constants/common-const';
import dayjs, { Dayjs } from 'dayjs';
import {SmartLoading} from "/@/components/smart-loading";

onMounted(() => {
  init();
});

const activeKey = ref('waybill');
function changeTab (value) {
  activeKey.value = value;
  init();
}

const queryFormState = {
  keywords: null,
  year: dayjs().format('YYYY'),
  departmentId: undefined,
  searchWord: undefined,
  pageNum: 1,
  pageSize: PAGE_SIZE
};

const queryForm = reactive({
  ...queryFormState,
});

const columns = reactive([]);

const tableData = ref([]);
let total = ref(0);
const tableLoading = ref(false);
const chooseYear = ref(dayjs());
const openYearFlag = ref(false);

function search(){
  queryForm.pageNum = 1;
  init();
}

function init(){
  initTable();
  queryList();
}

function initTable () {
  columns.length = 0;
  columns.push({
    title: '客户名称',
    dataIndex: 'shortName',
    fixed: 'left',
    width: 240,
  });
  for (let i = 0; i < 12; i++) {
    let title = dayjs().year(queryForm.year).month(i).format('YYYY-MM');
    columns.push({
      title,
      dataIndex: title,
      customRender: ({ record }) => {
        let monthList = record.monthList;
        let find = monthList.find((e) => e.month == title);
        if (find) {
          return find.count;
        }
        return '0';
      },
    });
  }
}

// 查询
async function queryList () {
  try {
    tableLoading.value = true;
    let responseModel = null;
    let totalResponseModel = null;
    if(activeKey.value == 'order'){
      responseModel = await shipperReportApi.queryShipperOrderStatistic(queryForm);
      totalResponseModel = await shipperReportApi.queryShipperOrderTotal(queryForm);
    }else {
      responseModel = await shipperReportApi.queryShipperWaybillStatistic(queryForm);
      totalResponseModel = await shipperReportApi.queryShipperWaybillTotal(queryForm);
    }
    const listData = responseModel.data.list;
    total.value = responseModel.data.total;

    const totalData = totalResponseModel.data;
    if (!_.isEmpty(totalData)) {
      const totalRowData = setTotalRow(totalData);
      listData.push(totalRowData);
    }
    tableData.value = listData;
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}

// 添加合计行
const TOTAL_ROW_ID = 'total';

function setTotalRow (totalData) {
  const totalRowData = {
    shipperId: TOTAL_ROW_ID,
    shortName: '合计',
    monthList: totalData,
    isTotalRow: true,
  };
  return totalRowData;
}

// 弹出日历和关闭日历的回调
function handleOpenChange (open) {
  openYearFlag.value = open;
}

// 选择年份改变
function handlePanelChange (value) {
  queryForm.year = value.format('YYYY');
  chooseYear.value = value;
  openYearFlag.value = false;
}

// 点击重置
function resetQuery () {
  Object.assign(queryForm, queryFormState);
  init();
}

// ------------ 导出 Excel -----------
function exportExcel () {
  SmartLoading.show();
  let params = _.cloneDeep(queryForm);
  if(activeKey.value == 'order') {
    shipperReportApi.orderMonthStatisticExport('每月订单量.xlsx', params);
  } else {
    shipperReportApi.waybillMonthStatisticExport('每月运单量.xlsx', params);
  }
  SmartLoading.hide();
}

</script>

<style scoped lang="less">
.table-list {
  :deep(.ant-table-tbody) {
    tr[data-row-key='total'] {
      td {
        position: sticky !important;
        bottom: 0 !important;
        border-top: 1px solid #f0f0f0;
        background-color: #fafafa !important;
      }
    }
  }
}
</style>

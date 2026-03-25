<template>
  <a-form class="smart-query-form" v-privilege="'waybillProfit:query'">
    <a-row class="smart-query-form-row">
      <a-form-item label="关键字" class="smart-query-form-item">
        <a-input style="width: 300px" v-model:value="queryForm.keywords" placeholder="运单号/订单号/货主名称/货主简称"/>
      </a-form-item>
      <a-form-item label="司机" class="smart-query-form-item">
        <DriverSelect width="150px" v-model:value="queryForm.driverId"/>
      </a-form-item>
      <a-form-item class="smart-query-form-item" label="车辆">
        <VehicleSelect v-model:value="queryForm.vehicleIdList" multiple width="150px"/>
      </a-form-item>
      <a-form-item class="smart-query-form-item" label="销售">
        <employee-select ref="salesRef" multiple v-model:value="queryForm.managerIdList"
                         :systemConfigKey="SYSTEM_CONFIG_KEY_ENUM.SALES_ROLE_CODE.value" placeholder="请选择销售员" width="200px" />
      </a-form-item>
      <a-form-item class="smart-query-form-item" label="调度员">
        <RoleEmployeeSelect v-model:value="queryForm.scheduleIdList"
                            :systemConfigKey="SYSTEM_CONFIG_KEY_ENUM.SCHEDULE_ROLE_CODE.value" multiple placeholder="请选择调度员" width="200px" />
      </a-form-item>
      <a-form-item class="smart-query-form-item" label="客服">
        <RoleEmployeeSelect v-model:value="queryForm.customerIdList"
                            :systemConfigKey="SYSTEM_CONFIG_KEY_ENUM.CUSTOMER_SERVICE_ROLE_CODE.value" multiple placeholder="请选择客服"
                            width="200px" />
      </a-form-item>
      <a-form-item class="smart-query-form-item" label="运输方式">
        <smart-enum-select v-model:value="queryForm.transportMode" enumName="WAYBILL_TRANSPORT_MODE_ENUM" placeholder="请选择运输方式"/>
      </a-form-item>

      <a-form-item label="创建时间" class="smart-query-form-item">
        <a-space direction="vertical" :size="12">
          <a-range-picker v-model:value="searchDate" :allowClear="true" :presets="defaultTimeRanges" @change="dateChange"/>
        </a-space>
      </a-form-item>
      <a-form-item class="smart-query-form-item" label="业务时间">
        <a-range-picker :allowClear="true" :value="businessDateList" picker="month" @change="businessDateChange"/>
      </a-form-item>
      <a-form-item class="smart-query-form-item" label="装货时间">
        <a-range-picker v-model:value="loadTimeRange" :presets="defaultTimeRanges" style="width: 230px"
                        @change="changeLoadTime"/>
      </a-form-item>
      <a-form-item class="smart-query-form-item" label="卸货时间">
        <a-range-picker v-model:value="unloadTimeRange" :presets="defaultTimeRanges" style="width: 230px"
                        @change="changeUnloadTime"/>
      </a-form-item>
      <a-form-item label="利润区间" name="price" style="display: flex">
        <a-input-number
            placeholder="请输入开始利润"
            v-model:value="queryForm.startProfitAmount"
            :min="0"
            :max="9999"
        />
        -
        <a-input-number
            placeholder="请输入结束利润"
            v-model:value="queryForm.endProfitAmount"
            :min="0"
            :max="9999"
        />
      </a-form-item>


      <a-form-item class="smart-query-form-item smart-margin-left10">
        <a-button type="primary" @click="ajaxQuery">
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
        <a-button @click="exportExcel()" v-privilege="'waybillProfit:export'" size="small">导出</a-button>


      </div>
      <div class="smart-table-setting-block"></div>
      <div>
        <SmartTableOperator v-model="columnsList" :refresh="ajaxQuery" :tableId="TABLE_ID_CONST.WAYBILL_PROFIT_REPORT"/>
      </div>
    </a-row>
    <div class="statistics">
      <a-row class="smart-statistics">
        应付总额：<span class="amount">{{ formatMoney(amountStatistics?.payableAmount) }}</span>元
        ,司机工资：<span class="amount">{{ formatMoney(amountStatistics?.salaryAmount) }}</span>元
        ,在途费用：<span class="amount">{{ formatMoney(amountStatistics?.carCostAmount) }}</span>元
        ,应收总额：<span class="amount">{{ formatMoney(amountStatistics?.receiveAmount) }}</span>元
        ,税金：<span class="amount">{{ formatMoney(amountStatistics?.taxAmount) }}</span>元
        ,利润：<span class="amount">{{ formatMoney(amountStatistics?.profitAmount) }}</span>元
      </a-row>
    </div>
    <a-table
        :scroll="{ x: 8300 ,y:scrollY}"
        size="small"
        bordered
        :dataSource="tableData"
        :columns="columnsList"
        rowKey="waybillId"
        :pagination="false"
    >
      <template #headerCell="{ column }">
        <template v-if="column.dataIndex === 'receiveAmount'">
          <span class="receive-style">
            应收总额
              <a-tooltip placement="top">
                <template #title>
                  <span>应收运费与应收异常费用的合计</span>
                </template>
                 <question-circle-outlined/>
              </a-tooltip>
          </span>
        </template>

        <template v-if="column.dataIndex === 'payableAmount'">
          <span class="pay-style">
            应付总额
              <a-tooltip placement="top">
                <template #title>
                  <span>付给司机的金额合计，包含现金、异常费用、油卡</span>
                </template>
                 <question-circle-outlined/>
              </a-tooltip>
          </span>
        </template>

        <template v-if="column.dataIndex === 'taxAmount'">
          <span>
            税金
              <a-tooltip placement="top">
                <template #title>
                  <span>应收乘订单的税点为税金</span>
                </template>
                 <question-circle-outlined/>
              </a-tooltip>
          </span>
        </template>

        <template v-if="column.dataIndex === 'profitAmount'">
          <span>
            利润
              <a-tooltip placement="top">
                <template #title>
                  <span>应收总额 - 应付总额 - 税金 = 利润</span>
                </template>
                 <question-circle-outlined/>
              </a-tooltip>
          </span>
        </template>

      </template>


      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'index'">
          {{ record.shortName == '合计' ? '' : index + 1 }}
        </template>
        <template v-if="column.dataIndex === 'orderNo'">
          <a @click="orderDetail(record.orderId)">{{ text }}</a>
        </template>
        <template v-if="column.dataIndex === 'waybillNumber'">
          <a @click="waybillDetail(record.waybillId)">{{ text }}</a>
        </template>
        <template v-if="column.dataIndex === 'settleType'">
          {{ $smartEnumPlugin.getDescByValue('SETTLE_TYPE_ENUM', text) }}
        </template>
        <template v-for="item in costColumnsList">
          <template v-if="column.dataIndex === item">
            {{ text || 0 }}
          </template>
        </template>
        <template v-if="column.dataIndex === 'businessDate'">
          {{ (text || '').substring(0, 7) }}
        </template>
        <template v-if="column.dataIndex === 'transportMode'">
          {{$smartEnumPlugin.getDescByValue('WAYBILL_TRANSPORT_MODE_ENUM', record.transportMode)}}
        </template>
        <template v-if="column.dataIndex === 'payFlag'">
          <template v-if="record.shortName !== '合计'">
            <a-tag :color="text?'success':'warning'">
              {{ text ? '已支付' : '未支付' }}
            </a-tag>
          </template>
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
          @change="changePage"
          @showSizeChange="changePage"
      />
    </div>
  </a-card>
</template>
<script setup>
import SmartTableOperator from '/@/components/smart-table-operator/index.vue';
import DriverSelect from '/@/components/driver-select/index.vue';
import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';
import EnterpriseSelect from '/@/components/enterprise-select/index.vue';
import VehicleSelect from '/@/components/vehicle-select/index.vue';
import EmployeeSelect from '/@/components/role-employee-select/index.vue';
import RoleEmployeeSelect from '/@/components/role-employee-select/index.vue';
import { SYSTEM_CONFIG_KEY_ENUM } from '/@/constants/system/system-config-const';
import { TABLE_ID_CONST } from '/@/constants/table-id-const';
import { reactive, ref, onMounted, computed } from 'vue';
import { PAGE_SIZE, PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import { SmartLoading } from '/@/components/smart-loading';
import { useRouter } from 'vue-router';
import { waybillReportApi } from '/@/api/business/report/waybill-report-api';
import dayjs from 'dayjs';
import { defaultTimeRanges } from '/@/lib/default-time-ranges';
import { formatMoney } from '/@/utils/money-util';
import _ from 'lodash';
import { costItemApi } from '/@/api/business/cost-item/cost-item-api';
import { COST_ITEM_TYPE_ENUM } from '/@/constants/business/cost-const';
import { headerColumns, footerColumns } from './waybill-profit-columns';

const columns = ref([]);

const columnsList = computed(() => {
  let list = headerColumns.concat(receiveColumns.value).concat(payColumns.value).concat(footerColumns);
  return list;
});

// 由于接口返回的费用字段，过滤了小于0的数据，导致列表显示的费用项可能为空
const costColumnsList = computed(() => {
  let receiveCostItemIdList = receiveColumns.value.map(e => e.dataIndex);
  let costItemIdList = payColumns.value.map(e => e.dataIndex);
  return receiveCostItemIdList.concat(costItemIdList);
});

const queryFormState = {
  keywords: '',
  driverId: null,
  auditStatus: null,
  endTime: null,
  startTime: null,
  transportMode: null,
  vehicleIdList: [],
  enterpriseIdList: [],
  //销售
  managerIdList: [],
  // 调度
  scheduleIdList: [],
  // 客服
  customerIdList: [],
  startProfitAmount: null,
  endProfitAmount: null,
  pageNum: 1,
  pageSize: PAGE_SIZE,

  // 装货时间
  startLoadTime: null,
  endLoadTime: null,
  // 卸货时间
  startUnloadTime: null,
  endUnloadTime: null,
};
const queryForm = reactive({ ...queryFormState });
const tableData = ref([]);
let originTableData = [];
const total = ref(0);
let amountStatistics = ref({
  payableTotalAmount: 0,
  paidTotalAmount: 0
});

// 日期选择
let searchDate = ref();
// 业务日期选择
let businessDateList = ref([dayjs(), dayjs()]);
function dateChange (dates, dateStrings) {
  queryForm.startTime = dateStrings[0];
  queryForm.endTime = dateStrings[1];
}

function businessDateChange (dateList, dateStrings) {
  businessDateList.value = dateList;
  queryForm.businessStartDate = '';
  queryForm.businessEndDate = '';
  if (!_.isEmpty(dateList)) {
    queryForm.businessStartDate = dayjs(dateList[0]).format('YYYY-MM-01');
    queryForm.businessEndDate = dayjs(dateList[1]).format('YYYY-MM-01');
  }
}

// 装货时间
const loadTimeRange = ref([]);

function changeLoadTime (dates, dateStrings) {
  queryForm.startLoadTime = dateStrings[0];
  queryForm.endLoadTime = dateStrings[1];
}

// 卸货时间
const unloadTimeRange = ref([]);

function changeUnloadTime (dates, dateStrings) {
  queryForm.startUnloadTime = dateStrings[0];
  queryForm.endUnloadTime = dateStrings[1];
}

function resetQuery () {
  searchDate.value = [];
  Object.assign(queryForm, queryFormState);
  let startMonth = dayjs().startOf('month');
  let endMonth = dayjs().endOf('month');
  searchDate.value = [startMonth, endMonth];
  queryForm.startTime = startMonth.format('YYYY-MM-DD');
  queryForm.endTime = endMonth.format('YYYY-MM-DD');
  queryForm.businessStartDate = dayjs().format('YYYY-MM-01');
  queryForm.businessEndDate = dayjs().format('YYYY-MM-01');
  loadTimeRange.value = [];
  unloadTimeRange.value = [];
  businessDateList.value = [dayjs(),dayjs()];
  ajaxQuery();
}

async function ajaxQuery () {
  try {
    SmartLoading.show();
    let responseModel = await waybillReportApi.query(queryForm);
    const list = responseModel.data;
    let statistic = {};
    if (!_.isEmpty(list)) {
      statistic = list[list.length - 1];
    }
    let tableList = list.map(item => {
      // 应付费用
      let costList = _.cloneDeep(item.costList);
      (costList || []).forEach(costItem => {
        item[costItem.costItemId] = costItem.costAmount;
        statistic[costItem.costItemId] = Number((costItem.costAmount + (statistic[costItem.costItemId] || 0)).toFixed(2));
        // statistic[costItem.costItemId] = costItem.costAmount + (statistic[costItem.costItemId] || 0)
      });
      // 应收费用
      let receiveCostList = _.cloneDeep(item.receiveCostList);
      (receiveCostList || []).forEach(costItem => {
        item[costItem.costItemId] = costItem.costAmount;
        statistic[costItem.costItemId] = Number((costItem.costAmount + (statistic[costItem.costItemId] || 0)).toFixed(2));
      });
      delete item.costList;
      delete item.receiveCostList;
      return item;
    });
    originTableData = tableList;
    amountStatistics.value = statistic;
    let tableSize = tableList.length;
    total.value = tableSize - 1;
    changePage();
  } catch (e) {
    console.log(e);
  } finally {
    SmartLoading.hide();
  }
}

function exportExcel () {
  SmartLoading.show();
  let params = Object.assign({}, queryForm);
  params.vehicleIdList = (params.vehicleIdList || []).join(',');
  params.enterpriseIdList = (params.enterpriseIdList || []).join(',');
  waybillReportApi.downloadExcel(params);
  SmartLoading.hide();
}

let receiveColumns = ref([]);
let payColumns = ref([]);

async function queryCostItemList (type) {
  try {
    SmartLoading.show();
    let params = {
      pageNum: 1,
      pageSize: 100,
      type
    };
    let responseModel = await costItemApi.query(params);
    const list = responseModel.data.list;
    let costList = list.map(item => {
      return {
        title: item.name,
        dataIndex: item.costItemId,
        width: 90,
        class: type == COST_ITEM_TYPE_ENUM.RECEIVE.value ? 'receive-style' : 'pay-style'
      };
    });
    if (type == COST_ITEM_TYPE_ENUM.RECEIVE.value) {
      receiveColumns.value = costList;
    } else if (type == COST_ITEM_TYPE_ENUM.PAY.value) {
      payColumns.value = costList;
    }
  } catch (e) {
    console.log(e);
  } finally {
    SmartLoading.hide();
  }
}

function changePage () {
  SmartLoading.show();
  let pageNum = queryForm.pageNum;
  let pageSize = queryForm.pageSize;
  tableData.value = _.cloneDeep(originTableData).splice((pageNum - 1) * pageSize, pageSize);
  SmartLoading.hide();
}

let scrollY = ref(500);
onMounted(() => {
  scrollY.value = document.getElementById('smartAdminLayoutContent').offsetHeight - 10 -
      document.getElementsByClassName('smart-query-form')[0].offsetHeight - 10
      - document.getElementsByClassName('statistics')[0].offsetHeight - 12 * 2 - 24 - 10 - 40;
  let startMonth = dayjs().startOf('month');
  let endMonth = dayjs().endOf('month');
  searchDate.value = [startMonth, endMonth];
  queryForm.startTime = startMonth.format('YYYY-MM-DD');
  queryForm.endTime = endMonth.format('YYYY-MM-DD');
  queryForm.businessStartDate = startMonth.format('YYYY-MM-01');
  queryForm.businessEndDate = endMonth.format('YYYY-MM-01');
  queryCostItemList(COST_ITEM_TYPE_ENUM.RECEIVE.value);
  queryCostItemList(COST_ITEM_TYPE_ENUM.PAY.value);
  ajaxQuery();
});

// ----------------- 跳转 --------------------
let router = useRouter();

function orderDetail (orderId) {
  router.push({ path: '/order/detail', query: { orderId } });
}

function waybillDetail (waybillId) {
  router.push({ path: '/waybill/waybill-detail', query: { waybillId } });
}

</script>
<style>
.receive-style {
  color: green !important;
}

.pay-style {
  color: #1890ff !important;
}
</style>

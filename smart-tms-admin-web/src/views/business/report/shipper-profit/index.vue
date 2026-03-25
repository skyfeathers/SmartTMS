<template>
  <a-form v-privilege="'waybillShipperProfit:query'" class="smart-query-form">
    <a-row class="smart-query-form-row">

      <a-form-item class="smart-query-form-item" label="货主">
        <ShipperSelect v-model:value="queryForm.shipperId" placeholder="请选择货主"/>
      </a-form-item>

      <a-form-item class="smart-query-form-item" label="运输类型">
        <SmartEnumSelect v-model:value="queryForm.transportMode" enumName="WAYBILL_TRANSPORT_MODE_ENUM"/>
      </a-form-item>

      <a-form-item class="smart-query-form-item" label="所属销售">
        <employee-select
            ref="managerSelectRef"
            v-model:value="queryForm.managerIdList"
            :systemConfigKey="SYSTEM_CONFIG_KEY_ENUM.SALES_ROLE_CODE.value"
            mode="multiple"
            placeholder="请选择所属销售"
            width="200px"
        />
      </a-form-item>

      <a-form-item class="smart-query-form-item" label="业务时间">
        <a-range-picker :allowClear="false" :value="businessDateList" picker="month" @change="dateChange"/>
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
        <a-button v-privilege="'waybillShipperProfit:export'" size="small" @click="exportExcel()">导出</a-button>
      </div>
      <div class="smart-table-setting-block"></div>
      <div>
      </div>
    </a-row>

    <div class="smart-statistics smart-margin-top5">
      应付总计：<span class="amount">{{ formatMoneyStr(amountStatistics.payableAmount) }}</span>，
      应收总计：<span class="amount">{{ formatMoneyStr(amountStatistics.receiveTotalAmount) }}</span>，
      未开票金额：<span class="amount">{{ formatMoneyStr(amountStatistics.waitInvoiceAmount) }}</span>元，
      总运单量：<span class="amount">{{ amountStatistics.waybillCount }}</span>单。
      <br/>
      工资：<span class="amount">{{ formatMoneyStr(amountStatistics.salaryAmount) }}</span>，
      在途费用：<span class="amount">{{ formatMoneyStr(amountStatistics.carCostAmount) }}</span>，
      税金：<span class="amount">{{ formatMoneyStr(amountStatistics.taxAmount) }}</span>，
      毛利润：<span class="amount">{{ formatMoneyStr(amountStatistics.profitAmount) }}</span>元，
      毛利率：<span class="amount">{{ formatMoneyStr(amountStatistics.profitRate) }}</span>%。
    </div>

    <a-table
        :columns="columns"
        :dataSource="tableData"
        :pagination="false"
        :scroll="{ x: 2200}"
        bordered
        rowKey="shipperId"
        size="small"
    >
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'profitRate'">
          {{record.profitRate}}%
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
import ShipperSelect from '/@/components/shipper-select/index.vue';
import EnterpriseSelect from '/@/components/enterprise-select/index.vue';
import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';
import EmployeeSelect from '/@/components/role-employee-select/index.vue';

import { SYSTEM_CONFIG_KEY_ENUM } from '/@/constants/system/system-config-const';
import { reactive, ref, onMounted, computed } from 'vue';
import { SmartLoading } from '/@/components/smart-loading';
import { useRouter } from 'vue-router';
import dayjs from 'dayjs';
import _ from 'lodash';
import { formatMoney } from '/@/utils/money-util';
import { waybillReportApi } from '/@/api/business/report/waybill-report-api';
import { PAGE_SIZE, PAGE_SIZE_OPTIONS } from '/@/constants/common-const';

function formatMoneyStr (value) {
  return formatMoney(value);
}

const queryFormState = {
  shipperId: null,
  enterpriseId: null,
  businessStartDate: dayjs().format('YYYY-MM-01'),
  businessEndDate: dayjs().format('YYYY-MM-01'),
  transportMode: null,
  managerIdList: [],
  pageNum: 1,
  pageSize: PAGE_SIZE
};
const queryForm = reactive({ ...queryFormState });
const tableData = ref([]);
const total = ref(0);

// 日期选择
let businessDateList = ref([dayjs(), dayjs()]);

function dateChange (dateList, dateStrings) {
  businessDateList.value = dateList;
  queryForm.businessStartDate = dayjs(dateList[0]).format('YYYY-MM-01');
  queryForm.businessEndDate = dayjs(dateList[1]).format('YYYY-MM-01');
}

function resetQuery () {
  businessDateList.value = [dayjs(),dayjs()];
  Object.assign(queryForm, queryFormState);
  ajaxQuery();
  statisticAmount();
}

function search () {
  queryForm.pageNum = 1;
  ajaxQuery();
  statisticAmount();
}

async function ajaxQuery () {
  try {
    SmartLoading.show();
    let responseModel = await waybillReportApi.queryWaybillShipperProfitList(queryForm);
    const list = responseModel.data.list;
    tableData.value = list;
    total.value = responseModel.data.total;
  } catch (e) {
    console.log(e);
  } finally {
    SmartLoading.hide();
  }
}

const amountStatistics = ref({
  receiveAmount: 0,
  waitInvoiceAmount: 0,
  waybillCount: 0,
  payableAmount: 0,
  salaryAmount: 0,
  carCostAmount: 0,
  taxAmount: 0,
  profitAmount: 0,
  profitRate: 0,
});

async function statisticAmount () {
  try {
    let responseModel = await waybillReportApi.queryWaybillShipperProfitSummary(queryForm);
    amountStatistics.value = responseModel.data;
  } catch (e) {
    console.log(e);
  } finally {

  }
}



function exportExcel () {
  SmartLoading.show();
  waybillReportApi.downloadWaybillShipperProfitExcel(queryForm);
  SmartLoading.hide();
}

onMounted(() => {
  ajaxQuery();
  statisticAmount();
});

// ----------------- 跳转 --------------------
let router = useRouter();

function orderDetail (orderId) {
  router.push({ path: '/order/detail', query: { orderId } });
}

function waybillDetail (waybillId) {
  router.push({ path: '/waybill/waybill-detail', query: { waybillId } });
}

// ----------------- 定义table列 --------------------
const columns = ref([
  {
    title: '货主名称',
    dataIndex: 'consignor'
  },
  {
    title: '自有车应收',
    dataIndex: 'selfVehicleReceiveAmount'
  },
  {
    title: '挂靠车应收',
    dataIndex: 'monthReceiveAmount'
  },
  {
    title: '外派车应收',
    dataIndex: 'arrivePayReceiveAmount',
  },
  {
    title: '应收总计',
    dataIndex: 'receivelAmount'
  },
  {
    title: '未开票金额',
    dataIndex: 'waitInvoiceAmount'
  },
  {
    title: '已开票金额',
    dataIndex: 'invoiceAmount'
  },
  {
    title: '自有车单量',
    dataIndex: 'selfVehicleWaybillCount'
  },
  {
    title: '挂靠车单量',
    dataIndex: 'monthWaybillCount'
  },
  {
    title: '外派车单量',
    dataIndex: 'arrivePayWaybillCount'
  },
  {
    title: '总运单量',
    dataIndex: 'waybillCount'
  },
  {
    title: '自有车应付',
    dataIndex: 'selfVehiclePayAmount'
  },
  {
    title: '挂靠车应付',
    dataIndex: 'monthPayAmount'
  },
  {
    title: '外派车应付',
    dataIndex: 'arrivePayAmount'
  },
  {
    title: '应付总计',
    dataIndex: 'payableAmount'
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
  },
  {
    title: '所属销售',
    dataIndex: 'managerName'
  }
]);
</script>
<style>
</style>

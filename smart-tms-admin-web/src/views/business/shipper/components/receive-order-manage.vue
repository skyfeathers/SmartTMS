<!--
 * @Description:
 * @version:
 * @Author: lidoudou
 * @Date: 2022-08-09 14:40:21
 * @LastEditors: lidoudou
 * @LastEditTime: 2022-08-09 15:35:45
-->
<template>
  <a-form class="smart-query-form">
    <a-row class="smart-query-form-row">
      <a-form-item label="关键字" class="smart-query-form-item">
        <a-input
            style="width: 300px"
            v-model:value="queryForm.keywords"
            placeholder="收款单号/创建人"
        />
      </a-form-item>

      <a-form-item label="账期">
        <a-range-picker
            v-model:value="accountPeriodDateRange"
            :ranges="defaultTimeRanges"
            style="width: 100%"
            @change="changeAccountPeriod"
        />
      </a-form-item>

      <a-form-item label="对账状态" class="smart-query-form-item">
        <a-radio-group v-model:value="queryForm.checkStatus" @change="ajaxQuery">
          <a-radio-button :value="CHECK_STATUS_ENUM.CHECK.value">已对账</a-radio-button>
          <a-radio-button :value="CHECK_STATUS_ENUM.WAIT_CHECK.value">未对账</a-radio-button>
          <a-radio-button :value="CHECK_STATUS_ENUM.CANCEL.value">已作废</a-radio-button>
        </a-radio-group>
      </a-form-item>

      <a-form-item label="发票状态" class="smart-query-form-item">
        <a-radio-group v-model:value="queryForm.invoiceStatus" @change="ajaxQuery">
          <a-radio-button :value="INVOICE_STATUS_ENUM.INVOICE.value">已开票</a-radio-button>
          <a-radio-button :value="INVOICE_STATUS_ENUM.WAIT_INVOICE.value">未开票</a-radio-button>
          <a-radio-button :value="INVOICE_STATUS_ENUM.CANCEL.value">已作废</a-radio-button>
        </a-radio-group>
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

  <a-card class="smart-margin-top10">
    <a-table size="small" :dataSource="tableData" :columns="columns" :pagination="false" :scroll="{x:1000}"
             :loading="tableLoading" bordered>
      <template #receiveOrderNumber="{record,text}">
        <a @click="detailReceiveOrder(record.receiveOrderId)" type="link">{{ text }}</a>
      </template>
      <template #accountPeriodDate="{record,text}">
        <span :class="{ unverification: !record.verificationFlag }">{{ text }}</span>
      </template>
      <template #checkStatus="{text}">
        <span>{{ $smartEnumPlugin.getDescByValue('CHECK_STATUS_ENUM', text) }}</span>
      </template>
      <template #invoiceStatus="{text}">
        <span>{{ $smartEnumPlugin.getDescByValue('INVOICE_STATUS_ENUM', text) }}</span>
      </template>
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
          @change="ajaxQuery"
          @showSizeChange="ajaxQuery"
          :show-total="(total) => `共${total}条`"
      />
    </div>
  </a-card>
</template>
<script setup>
import { reactive, ref, onMounted } from 'vue';
import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import { useRoute, useRouter } from 'vue-router';
import { CHECK_STATUS_ENUM, INVOICE_STATUS_ENUM } from '/@/constants/business/receive-order-const';
import { defaultTimeRanges } from '/@/lib/default-time-ranges';
import { receiveOrderApi } from '/@/api/business/receive-order/receive-order-api';

const route = useRoute();
const router = useRouter();

// ------- 查询条件 form表单 start --------
const accountPeriodDateRange = ref([]);
const queryFormState = {
  pageNum: 1,
  pageSize: 10,
  shipperId: route.query.shipperId,
  keywords: '',
  checkStatus: null,
  invoiceStatus: null,
  accountPeriodStartTime: null,
  accountPeriodEndTime: null,
};
const queryForm = reactive({ ...queryFormState });

function changeAccountPeriod (dates, dateStrings) {
  queryForm.accountPeriodStartTime = dateStrings[0];
  queryForm.accountPeriodEndTime = dateStrings[1];
}

// ------- 列表相关 start --------
const columns = reactive([
  {
    title: '收款单号',
    dataIndex: 'receiveOrderNumber',
    slots: { customRender: 'receiveOrderNumber' },
    width: 150,
    fixed: 'left'
  },
  {
    title: '账期',
    dataIndex: 'accountPeriodDate',
    slots: { customRender: 'accountPeriodDate' },
    width: 100,
  },
  {
    title: '应收总额',
    dataIndex: 'totalAmount',
    width: 80
  },
  {
    title: '已销金额',
    dataIndex: 'verificationAmount',
    width: 100,
  },
  {
    title: '未销金额',
    dataIndex: 'unpaidAmount',
    width: 100,
  },
  {
    title: '对账状态',
    dataIndex: 'checkStatus',
    slots: { customRender: 'checkStatus' },
    width: 100,
  },
  {
    title: '发票状态',
    dataIndex: 'invoiceStatus',
    slots: { customRender: 'invoiceStatus' }
  },
  {
    title: '创建人',
    dataIndex: 'createUserName',
    width: 60,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 170,
  },
]);
const tableLoading = ref(false);
const tableData = ref([]);
const total = ref(0);

function search () {
  queryForm.pageNum = 1;
  ajaxQuery();
}

async function ajaxQuery () {
  try {
    tableLoading.value = true;
    let responseModel = await receiveOrderApi.queryPageByShipper(queryForm);
    const list = responseModel.data.list;
    total.value = responseModel.data.total;
    tableData.value = list;
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}

function resetQuery () {
  Object.assign(queryForm, queryFormState);
  accountPeriodDateRange.value = [];
  ajaxQuery();
}

// ----------- 页面跳转 start -----------
function detailReceiveOrder (receiveOrderId) {
  router.push({
    path: '/receive-order/detail',
    query: {
      receiveOrderId
    }
  });
}

// ------- 列表相关 end --------

onMounted(() => {
  ajaxQuery();
});

defineExpose({
  ajaxQuery
});
</script>
<style scoped lang="less">
.unverification {
  color: red;
}
</style>

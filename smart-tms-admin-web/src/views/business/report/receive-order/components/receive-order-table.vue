<!--
 * @Description: file content
 * @Author: yandy
 * @Date: 2022-08-03 15:35:07
 * @LastEditors:
 * @LastEditTime: 2022-08-03 15:35:07
-->
<template>
  <a-table
      :columns="columns"
      :dataSource="tableData"
      :loading="tableLoading"
      :pagination="false"
      bordered
      size="small"
  >
    <template #bodyCell="{ text, record, index, column }">
      <template v-if="column.dataIndex === 'receiveOrderNumber'">
        <a @click="receiveOrderDetail(record.receiveOrderId)">{{ text }}</a><SmartCopyIcon :value="text" />
      </template>
    </template>
  </a-table>

  <div class="smart-query-table-page">
    <a-pagination
        v-model:current="pageForm.pageNum"
        v-model:pageSize="pageForm.pageSize"
        :defaultPageSize="pageForm.pageSize"
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

</template>
<script setup>
import { onMounted, reactive, ref } from 'vue';
import { PAGE_SIZE, PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import { receiveOrderApi } from '/@/api/business/receive-order/receive-order-api';
import { useRouter } from 'vue-router';


defineExpose({
  ajaxQuery
})

const props = defineProps({
  pageSize: {
    type: Number,
    default: 5
  }
});

// 表格列
let columns = reactive([
  {
    title: '收款单号',
    dataIndex: 'receiveOrderNumber',
  },
  {
    title: '货主名称',
    dataIndex: 'consignor',
    ellipsis: true,
  },
  {
    title: '开票/应收金额',
    dataIndex: 'totalAmount',
    ellipsis: true,
    width: 100
  },
  {
    title: '回款金额',
    dataIndex: 'verificationAmount',
    width: 90
  },
  {
    title: '应收款余额',
    dataIndex: 'waitReceiveAmount',
    width: 100
  },
  {
    title: '帐期',
    width: 100,
    dataIndex: 'accountPeriodDate',
  },
  {
    title: '逾期天数',
    dataIndex: 'overdueDays'
  },
  {
    title: '销售',
    dataIndex: 'managerName'
  }
]);

const pageFormState = {
  pageNum: 1,
  pageSize: props.pageSize
};
const pageForm = reactive({ ...pageFormState });
const tableLoading = ref(false);
const tableData = ref([]);
const total = ref(0);

async function ajaxQuery (queryForm) {
  try {
    tableLoading.value = true;
    let params = pageForm;
    if (queryForm) {
      Object.assign(params, queryForm,);
    }
    let responseModel = await receiveOrderApi.queryWaitReceiveOrder(params);
    const list = responseModel.data.list;
    total.value = responseModel.data.total;
    tableData.value = list;
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}

let router = useRouter();

function receiveOrderDetail (receiveOrderId) {
  router.push({ path: '/receive-order/detail', query: { receiveOrderId } });
}

onMounted(() => {
  ajaxQuery();
});


</script>
<style lang="less" scoped>
</style>

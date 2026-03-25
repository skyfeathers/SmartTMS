<!--
 * @Description:  付款单交易记录
 * @Author: zhuoda
 * @Date: 2022-08-02
 * @LastEditTime: 2022-08-02
 * @LastEditors: zhuoda
-->
<template>
  <a-table size="small" bordered :dataSource="tableData" :columns="columns" rowKey="payOrderId" :pagination="false" :loading="tableLoading">
    <template #bodyCell="{ text, record, index, column }">
      <template v-if="column.dataIndex === 'attachment'">
        <FilePreview :fileList="record.attachment" />
      </template>
    </template>
  </a-table>
</template>
<script setup>
import { onMounted, reactive, ref } from 'vue';
import FilePreview from '/@/components/file-preview/index.vue';
import { payOrderApi } from '/@/api/business/pay/pay-order-api';
const props = defineProps({
  payOrderId: Number,
});

const columns = reactive([
  {
    title: '付款账户',
    dataIndex: 'bankName',
  },
  {
    title: '银行流水号',
    dataIndex: 'sequenceCode',
  },
  {
    title: '付款金额',
    dataIndex: 'payAmount',
  },
  {
    title: '付款凭证',
    dataIndex: 'attachment',
  },
  {
    title: '操作人',
    dataIndex: 'createUserName',
    width: 100,
  },
  {
    title: '付款时间',
    dataIndex: 'payTime',
    width: 160,
  },
]);

onMounted(queryRecord);

const tableData = ref([]);
const tableLoading = ref(false);
async function queryRecord() {
  try {
    tableLoading.value = true;
    let res = await payOrderApi.getRecordList(props.payOrderId);
    tableData.value = res.data;
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}
</script>

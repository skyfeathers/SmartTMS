<template>
  <a-modal width="1300px" :open="visible" title="收款信息" ok-text="提交" cancel-text="取消" @ok="onClose" @cancel="onClose">
    <a-table
        :columns="columns"
        :dataSource="tableData"
        bordered
        :pagination="false"
        rowKey="orderId"
        size="small"
    >
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'makeInvoiceFlag'">
          {{ text ? '是' : '否' }}
          </template>
        <template v-if="column.dataIndex === 'invoiceStatus'">
          <span v-if="record.makeInvoiceFlag">{{ $smartEnumPlugin.getDescByValue('INVOICE_STATUS_ENUM', text) }}</span>
          <span v-else>无需开票</span>
        </template>
      </template>
    </a-table>
  </a-modal>
</template>
<script setup>
import { SmartLoading } from '/@/components/smart-loading';
import { reactive, ref } from 'vue';
import { receiveOrderApi } from '/@/api/business/receive-order/receive-order-api';

// ----------------------- 对外暴露 ------------------------
defineExpose({
  showModal,
});

// ----------------------- 显示/隐藏 ------------------------
const visible = ref(false);

function onClose () {
  visible.value = false;
}

function showModal (waybillId) {
  visible.value = true;
  queryList(waybillId);
}

const tableData = ref([]);

async function queryList (waybillId) {
  try {
    SmartLoading.show();
    let result = await receiveOrderApi.queryByWaybillId(waybillId);
    tableData.value = result.data;
  } catch (e) {
    console.log(e);
  } finally {
    SmartLoading.hide();
  }
}

const columns = reactive([
  {
    title: '收款单号',
    dataIndex: 'receiveOrderNumber',
    slots: { customRender: 'receiveOrderNumber' },
    width: 150,
    fixed: 'left'
  },
  {
    title: '本次应收总额',
    dataIndex: 'totalAmount',
    width: 130
  },
  {
    title: '是否需要开票',
    dataIndex: 'makeInvoiceFlag',
    width: 110,
  },
  {
    title: '开票状态',
    dataIndex: 'invoiceStatus',
    width: 80
  },
  {
    title: '税点',
    dataIndex: 'taxPoint',
    width: 80
  },
  {
    title: '应收对账备注',
    dataIndex: 'remark',
    width: 110,
  },
])

</script>
<style scoped lang="less">
</style>

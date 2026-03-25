<template>
  <a-table
      :scroll="{ x: 4500 }"
      size="small"
      bordered
      :dataSource="tableData"
      :columns="columns"
      rowKey="waybillId"
      :pagination="false"
      :loading="tableLoading"
  >
    <template #bodyCell="{ text, record, index, column }">
      <template v-if="column.dataIndex === 'waybillNumber'">
        <a-button @click="waybillDetail(record.waybillId)" type="link">{{ text }}</a-button>
      </template>
      <template v-if="column.dataIndex === 'orderNumber'">
        <a-button @click="orderDetail(record.orderId)" type="link">{{ text }}</a-button>
      </template>
      <template v-if="column.dataIndex === 'shipperName'">
        <a-button @click="shipperDetail(record.shipperId)" type="link">{{ text }}</a-button>
      </template>
      <template v-if="column.dataIndex === 'orderType'">
        {{ $smartEnumPlugin.getDescByValue('ORDER_TYPE_ENUM', text) }}
      </template>
      <template v-if="column.dataIndex === 'settleType'">
        {{$smartEnumPlugin.getDescByValue('SETTLE_TYPE_ENUM', record.settleType)}}
      </template>

      <template v-if="column.dataIndex === 'auditStatus'">
        <a-tag :color="$smartEnumPlugin.getFieldByValue('FLOW_AUDIT_STATUS_ENUM', record.auditStatus, 'color')">{{
            $smartEnumPlugin.getDescByValue('FLOW_AUDIT_STATUS_ENUM', record.auditStatus)
          }}</a-tag>
      </template>
      <template v-if="column.dataIndex === 'waybillStatus'">
        <a-tag :color="$smartEnumPlugin.getFieldByValue('WAYBILL_STATUS_ENUM', record.waybillStatus, 'color')">{{
            $smartEnumPlugin.getDescByValue('WAYBILL_STATUS_ENUM', record.waybillStatus)
          }}</a-tag>
      </template>
    </template>
  </a-table>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue';
import { waybillApi } from '/@/api/business/waybill/waybill-api';
import { columns } from '/@/views/business/waybill/components/waybill-list-table-column.js';
import { useRouter } from 'vue-router';

const props = defineProps({
  orderId: Number
});


const tableLoading = ref(false);
const tableData = ref([]);
const total = ref(0);

onMounted(()=>ajaxQuery(props.orderId));

async function ajaxQuery(orderId) {
  try {
    tableLoading.value = true;

    let responseModel = await waybillApi.queryByOrderId(orderId);
    tableData.value = responseModel.data;
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}

let router = useRouter();
function waybillDetail(waybillId) {
  router.push({ path: '/waybill/waybill-detail', query: { waybillId } });
}
function orderDetail(orderId) {
  router.push({ path: '/order/detail', query: { orderId } });
}
function shipperDetail(shipperId) {
  router.push({ path: '/shipper/detail', query: { shipperId } });
}

defineExpose({
  ajaxQuery,
});
</script>
<style lang="less" scoped></style>

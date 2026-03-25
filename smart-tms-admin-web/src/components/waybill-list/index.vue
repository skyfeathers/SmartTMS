<template>
  <a-table
      :scroll="{ x: 5800 }"
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
        <a @click="waybillDetail(record.waybillId)" type="link">{{ text }}</a>
      </template>
      <template v-if="column.dataIndex === 'orderNo'">
        <a @click="orderDetail(record.orderId)" type="link">{{ text }}</a>
      </template>
      <template v-if="column.dataIndex === 'shipperName'">
        <a-button @click="shipperDetail(record.shipperId)" type="link">{{ text }}</a-button>
      </template>
      <template v-if="column.dataIndex === 'orderType'">
        {{ $smartEnumPlugin.getDescByValue('ORDER_TYPE_ENUM', text) }}
      </template>
      <template v-if="column.dataIndex === 'settleMode'">
        {{$smartEnumPlugin.getDescByValue('SETTLE_MODE_ENUM', record.settleMode)}}
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
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue';
import { waybillApi } from '/@/api/business/waybill/waybill-api';
import { columns } from '/@/views/business/waybill/components/waybill-list-table-column.js';
import { useRouter } from 'vue-router';
import { AUDIT_STATUS_ENUM, PAGE_SIZE, PAGE_SIZE_OPTIONS } from '/@/constants/common-const';

const props = defineProps({
  driverId: {
    type: Number
  },
  vehicleId: {
    type: Number
  }
});


onMounted(() => ajaxQuery());

const queryFormState = {
  driverId:props.driverId,
  vehicleId: props.vehicleId,
  pageNum: 1,
  pageSize: PAGE_SIZE,
};
const queryForm = reactive({...queryFormState});
const tableLoading = ref(false);
const tableData = ref([]);
const total = ref(0);

async function ajaxQuery(orderId) {
  if (!props.driverId && !props.vehicleId) {
    return;
  }
  try {
    tableLoading.value = true;
    let responseModel = await waybillApi.query(queryForm);
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

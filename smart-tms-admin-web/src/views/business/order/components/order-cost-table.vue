<!--
 * @Description: 订单编辑费用明细弹窗
 * @version:
 * @Author: lidoudou
 * @Date: 2022-08-01
 * @LastEditors: lidoudou
 * @LastEditTime: 2022-08-01
-->
<template>
  <a-card>
    <a-table
        :columns="columns"
        :dataSource="tableData"
        bordered
        :pagination="false"
        size="small"
    >
      <template #index="{index}">
        {{ index + 1 }}
      </template>
      <template #costAmount="{text,index,record}">
        <span v-if="readonlyFlag">{{text}}</span>
        <a-input-number v-else :value="text" @change="value=>changeAmount(value,index,record)" :min="0"/>
      </template>
      <template #type>
        应收
      </template>
      <template #payObject>
        客户
      </template>
      <template #consignor>
        {{ consignor }}
      </template>
    </a-table>
  </a-card>
</template>
<script setup>
import { ref, reactive, onMounted, watch } from 'vue';
import { SmartLoading } from '/@/components/smart-loading';
import { orderApi } from '/@/api/business/order/order-api';
import _ from 'lodash';

// ----------------------- 表单 ------------------------
const tableData = ref([]);

function changeAmount (amount, index, record) {
  let list = _.cloneDeep(tableData.value);
  record.costAmount = amount;
  list[index] = record;
  tableData.value = list;
}
// ----------------------- 对外暴露 ------------------------
const props = defineProps({
  orderId: {
    type: Number
  },
  // 货主名称
  consignor: {
    type: String,
  },
  readonlyFlag: {
    type: Boolean,
    default: false
  }
});
defineExpose({
  tableData
});
// 箭头value变化
watch(
    () => props.orderId,
    (newValue) => {
      getCostDetail();
    }
);
// ----------------------- 查询数据 ------------------------
async function getCostDetail () {
  if (!props.orderId) {
    return;
  }
  SmartLoading.show();
  try {
    let responseModel = await orderApi.getCostDetail(props.orderId);
    let detail = responseModel.data || {};
    tableData.value = detail;
  } catch (error) {
    console.log('error', error);
  } finally {
    SmartLoading.hide();
  }
}

onMounted(() => {
  getCostDetail();
});
// ----------------------- 查询数据 ------------------------

const columns = reactive([
  {
    title: '序号',
    dataIndex: 'index',
    slots: { customRender: 'index' }
  },
  {
    title: '费用名称',
    dataIndex: 'costItemName'
  },
  {
    title: '金额',
    dataIndex: 'costAmount',
    slots: { customRender: 'costAmount' }
  },
  {
    title: '费用类型',
    dataIndex: 'type',
    slots: { customRender: 'type' },
  },
  {
    title: '支付单位',
    dataIndex: 'payObject',
    slots: { customRender: 'payObject' }
  },
  {
    title: '支付对象',
    dataIndex: 'consignor',
    slots: { customRender: 'consignor' }
  }
]);
</script>
<style scoped lang="less">
</style>

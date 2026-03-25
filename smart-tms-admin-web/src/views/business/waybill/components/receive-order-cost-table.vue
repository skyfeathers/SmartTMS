<template>
  <a-row class="smart-margin-top10">
    本次应收合计：{{totalReceiveAmount}}
  </a-row>
  <a-table class="smart-margin-top10"
      :scroll="{ x: 700 }"
      :columns="columns"
      :dataSource="costList"
      bordered
      :pagination="false"
      size="small"
  >
    <template #headerCell="{ column }">
      <template v-if="column.dataIndex === 'waitReceivedCostAmount'">
              <span>
                待收金额
                  <a-tooltip placement="top">
                    <template #title>
                      <span>处于待核算或核算通过未核销的金额</span>
                    </template>
                     <question-circle-outlined />
                  </a-tooltip>
              </span>
      </template>
    </template>
    <template #bodyCell="{ text, record, index, column }">
      <template v-if="column.dataIndex === 'costItemType'">
        {{ $smartEnumPlugin.getDescByValue('COST_ITEM_TYPE_ENUM', record.costItemType) }}
      </template>
      <template v-if="column.dataIndex === 'payableAmount'">
        <a-input-number v-model:value="record.payableAmount" :precision="4" :min="0"
                        :max="record.thisPayableAmount" :placeholder="'请输入' + record.costItemName"
                        style="width: 100px"/>
      </template>
    </template>
  </a-table>
</template>
<script setup>
import { reactive, computed } from 'vue';
// ----------------------- props ------------------------
const props = defineProps({
  costList: {
    type: Array
  }
})
// ----------------------- 对外暴露 ------------------------
defineExpose({
  getCostList
});

// ----------------------- 应付费用表格 ------------------------
const totalReceiveAmount = computed(() => {
  let totalAmount = (props.costList || []).map(e => e.payableAmount).reduce((a, b) => {
    return a + b;
  });
  return totalAmount;
});
function getCostList () {
  return (props.costList || []).map(e => {
    return {
      waybillReceiveCostId: e.waybillReceiveCostId,
      payableAmount: e.payableAmount
    };
  });
}
const columns = reactive([
  {
    title: '运单号',
    dataIndex: 'waybillNumber',
    width: 150,
    fixed: 'left'
  },
  {
    title: '费用名称',
    width: 80,
    dataIndex: 'costItemName',
  },
  {
    title: '应收金额',
    width: 80,
    dataIndex: 'costAmount',
  },
  {
    title: '已收金额',
    width: 80,
    dataIndex: 'receivedCostAmount',
  },
  {
    title: '待收金额',
    width: 100,
    dataIndex: 'waitReceivedCostAmount',
  },
  {
    title: '本次应收',
    width: 150,
    dataIndex: 'payableAmount',
  },
  {
    title: '费用类型',
    width: 80,
    dataIndex: 'costItemType'
  }
]);
</script>
<style scoped lang="less">
:deep(.ant-descriptions-item-container) {
  height: 30px;
}

.warning {
  font-weight: bold;
  color: #FF0000;
}
</style>

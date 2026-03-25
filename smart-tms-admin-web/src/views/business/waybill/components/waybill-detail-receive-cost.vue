<template>
  <a-table
      :scroll="{ x: 500 }"
      :columns="columns"
      :dataSource="costList"
      bordered
      :pagination="false"
      size="small"
  >
    <template #bodyCell="{ text, record, index, column }">
      <template v-if="column.dataIndex === 'costItemType'">
        {{ $smartEnumPlugin.getDescByValue('COST_ITEM_TYPE_ENUM', record.costItemType) }}
      </template>
    </template>
  </a-table>
</template>
<script setup>
import {reactive} from 'vue';

const props = defineProps({
  costList: {
    type: Object,
    default: () => {
      return [];
    }
  },
});
const columns = reactive([
  {
    title: '费用名称',
    dataIndex: 'costItemName',
  },
  {
    title: '费用类型',
    dataIndex: 'costItemType'
  },
  {
    title: '费用',
    dataIndex: 'costAmount'
  },
  {
    title: '备注',
    dataIndex: 'remark'
  },

]);


</script>
<style scoped lang="less">

</style>


<template>
  <a-table :columns="columns" :dataSource="goodsList" bordered :pagination="false" size="small" >
    <template #bodyCell="{ text, record, index, column }">
      <template v-if="column.dataIndex === 'goodsUnit'">
        {{$smartEnumPlugin.getDescByValue('GOODS_UNIT_TYPE_ENUM', record.goodsUnit)}}
      </template>
    </template>
  </a-table>
</template>
<script setup>
import { reactive } from 'vue';

const props = defineProps({
  goodsList: {
    type: Array,
  },
});

const columns = reactive([
  {
    title: '货物类型',
    dataIndex: 'goodsTypeName',
    fixed: 'left',
  },
  {
    title: '货物名称',
    dataIndex: 'goodsName',
  },
  {
    title: '货物单位',
    width: 80,
    dataIndex: 'goodsUnit',
  },
  {
    title: '货物量',
    width: 80,
    dataIndex: 'goodsQuantity',
  },
  {
    title: '备注',
    dataIndex: 'remark',
  },
]);
</script>

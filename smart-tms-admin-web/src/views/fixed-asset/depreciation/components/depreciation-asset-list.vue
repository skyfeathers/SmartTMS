<template>
  <a-table
      size="small"
      :dataSource="assetList"
      :columns="columns"
      rowKey="assetId"
      bordered
      :pagination="false"
  >
    <template #bodyCell="{ text, record, column ,index}">
<!--      <template v-if="column.dataIndex === 'assetName' || column.dataIndex === 'assetNo'">
        <a @click="toDetail(record.assetId)">{{ record[column.dataIndex] }}</a>
      </template>-->
    </template>
  </a-table>
</template>
<script setup>
import { ref } from 'vue';

const props = defineProps({
  assetList:{
    type:Array,
  }
})

const columns = ref([
  {
    title: '资产名称',
    dataIndex: 'assetName',
  },
  {
    title: '资产编号',
    dataIndex: 'assetNo',
  },
  {
    title: '原值',
    dataIndex: 'price',
    width: 60
  },
  {
    title: '期初累计折旧',
    dataIndex: 'initialDepreciationAmount',
  },
  {
    title: '本月折旧',
    dataIndex: 'depreciationAmount',
    width: 90
  },
  {
    title: '月折旧率(%)',
    dataIndex: 'monthResidualValueRate',
  },
  {
    title: '期末累计折旧',
    dataIndex: 'endingDepreciationAmount',
  },
]);

</script>


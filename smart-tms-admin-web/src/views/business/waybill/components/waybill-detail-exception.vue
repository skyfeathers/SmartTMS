<template>
  <a-table :columns="columns" :dataSource="exceptionList" bordered :pagination="false" size="small"
           :scroll="{x:1300}">
    <template #bodyCell="{ text, record, index, column }">
      <template v-if="column.dataIndex === 'exceptionType'">
        {{ $smartEnumPlugin.getDescByValue('WAYBILL_EXCEPTION_TYPE_ENUM', text) }}
      </template>
    </template>
  </a-table>
</template>
<script setup>
import {reactive} from 'vue';

const props = defineProps({
  exceptionList: Array, // 异常列表
})

const columns = reactive([
  {
    title: '异常类型',
    dataIndex: 'exceptionType',
    width: 80
  },
  {
    title: '异常描述',
    dataIndex: 'exceptionMessage',
    width: 250
  },
  {
    title: '触发人',
    dataIndex: 'createUserName',
    width: 80
  },
  {
    title: '触发时间',
    dataIndex: 'createTime',
    width: 80
  },
]);
</script>

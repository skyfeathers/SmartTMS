<!--
 * @Description: file content
 * @Author: yandy
 * @Date: 2022-07-13 11:32:34
 * @LastEditors:
 * @LastEditTime: 2022-07-13 11:32:34
-->
<template>
  <a-card size="small"
    :bordered="false">
    <a-tabs @change="changeTabs" v-model:activeKey="activeKey">
      <a-tab-pane v-for="item in $smartEnumPlugin.getValueDescList('FLOW_INSTANCE_QUERY_TYPE_ENUM')"
        :key="item.value"
        :tab="item.desc" />
    </a-tabs>
    <flow-table ref="tableRef" :query-type="activeKey" />
  </a-card>
</template>
<script setup>
import FlowTable from './components/flow-table.vue';
import { nextTick, ref, onMounted, onActivated } from 'vue';
import { FLOW_INSTANCE_QUERY_TYPE_ENUM } from "/@/constants/business/flow-const";

// 默认tab 页
let activeKey = ref(FLOW_INSTANCE_QUERY_TYPE_ENUM.MY_HANDLE.value);

// 审批列表
const tableRef = ref();
onMounted(() => {
  tableRef.value.queryList();
});

// 查询审批列表
// onActivated(() => {
//   tableRef.value.queryList();
// });

// 重置查询条件
function changeTabs() {
  nextTick(() => {
    tableRef.value.resetQuery();
  });
}
</script>

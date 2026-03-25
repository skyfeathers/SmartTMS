<template>
  <a-card size="small" :bordered="false">
    <a-tabs v-model:activeKey="activeKey">
<!--      <a-tab-pane tab="校区/小组目标设置"
        key="1"
        :forceRender="true">
        <DeptTab v-privilege="'deptSalesTarget:query'" />
      </a-tab-pane>-->
      <a-tab-pane tab="个人目标设置"
        key="2"
        :forceRender="true">
        <div v-privilege="'employeeSalesTarget:query'">
          <EmployeeTab />
        </div>
      </a-tab-pane>
    </a-tabs>
  </a-card>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue';
import DeptTab from './components/dept-tab.vue';
import EmployeeTab from './components/employee-tab.vue';

const activeKey = ref('2');
</script>
<style lang='less' scoped>
</style>

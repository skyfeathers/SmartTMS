<!--
 * @Author: LiHaiFan
 * @Date: 2021-08-26 10:36:54
 * @LastEditTime: 2022-08-25
 * @LastEditors: zhuoda
 * @Description:
-->
<template>
  <a-card class="role-container" size="small">
    <a-tabs v-model:activeKey="activeKey">
      <a-tab-pane key="1" tab="角色-功能权限">
        <RoleMenu />
      </a-tab-pane>
      <a-tab-pane key="2" tab="角色-数据范围">
        <RoleDataScope />
      </a-tab-pane>
      <a-tab-pane key="3" tab="角色-员工列表">
        <RoleEmployeeList />
      </a-tab-pane>
    </a-tabs>
  </a-card>
</template>
<script setup>
import { ref } from 'vue';
import RoleEmployeeList from '../role-employee-list/index.vue';
import RoleMenu from '../role-menu/index.vue';
 import RoleDataScope from '../role-data-scope/index.vue';

defineProps({
  value: Number,
});

defineEmits(['update:value']);

let activeKey = ref();
</script>
<style scoped lang="less">
.role-container {
  height: 100%;
}
</style>

<!--
 * @Author: zhuoda
 * @Date: 2021-08-12 16:11:06
 * @LastEditTime: 2022-06-23
 * @LastEditors: zhuoda
 * @Description: 角色
 * @FilePath: /smart-admin/src/views/system/employee/role/index.vue
-->
<template>
  <div class="height100">
    <a-row :gutter="10" type="flex" class="height100">
      <a-col flex="200px">
        <RoleList ref="roleList" />
      </a-col>
      <a-col style="width:calc(100% - 200px)">
        <RoleSetting />
      </a-col>
    </a-row>
  </div>
</template>
<script setup>
  import { computed, provide, ref } from 'vue';
  import RoleList from './components/role-list/index.vue';
  import RoleSetting from './components/role-setting/index.vue';
  defineProps({
    value: Object,
  });
  defineEmits('update:value');

  let roleList = ref();
  const selectRoleId = computed(() => {
    if (!roleList.value) {
      return null;
    }
    return roleList.value.selectRoleId;
  });
  provide('selectRoleId', selectRoleId);
</script>
<style scoped lang="less">
  .height100 {
    height: 100%;
  }
</style>

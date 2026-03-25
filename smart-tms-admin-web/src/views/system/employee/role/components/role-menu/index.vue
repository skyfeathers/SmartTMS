<!--
 * @Author: zhuoda
 * @Date: 2021-08-28 11:46:46
 * @LastEditTime: 2022-08-21
 * @LastEditors: zhuoda
 * @Description: 
 * @FilePath: /smart-admin/src/views/system/employee/role/components/role-tree/index.vue
-->
<template>
  <div>
    <div class="tree-header">
      <p>设置角色对应的功能操作、后台管理权限</p>
      <a-button v-if="selectRoleId" type="primary" @click="saveChange"> 保存 </a-button>
    </div>
    <!--CheckboxGroup 功能权限勾选部分 start-->
    <RoleTreeCheckbox :tree="tree" />
    <!--CheckboxGroup 功能权限勾选部分 end-->
  </div>
</template>
<script setup>
import { ref, inject, watch } from 'vue';
import { message } from 'ant-design-vue';
import _ from 'lodash';
import RoleTreeCheckbox from './role-tree-checkbox.vue';
import { roleMenuApi } from '/@/api/system/role-menu/role-menu-api';
import { useRoleStore } from '/@/store/modules/system/role';
import { useSpinStore } from '/@/store/modules/system/spin';

let roleStore = useRoleStore();
let tree = ref();
let selectRoleId = inject('selectRoleId');

watch(selectRoleId, () => getRoleSelectedMenu(), {
  immediate: true,
});

async function getRoleSelectedMenu() {
  if (!selectRoleId.value) {
    return;
  }
  let res = await roleMenuApi.getRoleSelectedMenu(selectRoleId.value);
  let data = res.data;
  if (_.isEmpty(roleStore.treeMap)) {
    roleStore.initTreeMap(data.menuTreeList || []);
  }
  roleStore.initCheckedData(data.selectedMenuId || []);
  tree.value = data.menuTreeList;
}
async function saveChange() {
  let checkedData = roleStore.checkedData;
  if (_.isEmpty(checkedData)) {
    message.error('还未选择任何权限');
    return;
  }
  let params = {
    roleId: selectRoleId.value,
    menuIdList: checkedData,
  };
  useSpinStore().show();
  try {
    await roleMenuApi.updateRoleMenu(params);
    message.success('保存成功');
  } catch (error) {
    console.log(error);
  } finally {
    useSpinStore().hide();
  }
}
</script>
<style scoped lang="less">
@import './index.less';
</style>

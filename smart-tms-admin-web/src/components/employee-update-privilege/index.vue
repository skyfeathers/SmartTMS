<template>
  <a-modal :width="700" :open="visible" title="更新权限" :destroyOnClose="true" :closable="false"
           :getContainer="getContainer">
    <a-row>
<!--      <a-button @click="expandTree" type="link">{{ expandFlag ? '收起' : '展开' }}</a-button>-->
      <a-button @click="syncRoleMenu" type="link">同步角色权限</a-button>
      <a-button @click="clearMenu" type="link">清除全部</a-button>
    </a-row>

    <RoleTreeCheckbox :tree="menuTree" />
<!--    <a-tree
        checkable
        :tree-data="menuTree"
        :fieldNames="fieldNames"
        v-model:checkedKeys="checkedKeys"
        v-model:expandedKeys="expandedKeys"
        :height="400"
    >
    </a-tree>-->
    <template #footer>
      <a-button key="back" @click="onClose">取消</a-button>
      <a-button key="submit" type="primary" @click="onSubmit">保存</a-button>
    </template>
  </a-modal>
</template>
<script setup>
import RoleTreeCheckbox from '/@/views/system/employee/role/components/role-menu/role-tree-checkbox.vue';

import { ref, reactive, computed, watch } from 'vue';
import { SmartLoading } from '/@/components/smart-loading';
import { getContainer } from '/@/utils/container-util';
import { employeeMenuApi } from '/@/api/system/employee/employee-menu-api';
import _ from "lodash"
import { useRoleStore } from '/@/store/modules/system/role';
import { message } from 'ant-design-vue';

let roleStore = useRoleStore();
// ----------------------- 对外暴露 ------------------------
const emits = defineEmits(['refresh']);

defineExpose({
  showModal,
});

// ----------------------- 显示/隐藏 ------------------------
const visible = ref(false);

function onClose () {
  Object.assign(form, formDefault);
  visible.value = false;
}

function showModal (employeeId) {
  form.employeeId = employeeId;
  form.menuIdList = [];
  visible.value = true;
  getMenuTree(employeeId);
}

// ----------------------- 表单 ------------------------
const formDefault = {
  employeeId: null,
  menuIdList: []
};
let form = reactive({ ...formDefault });
const fieldNames = computed(() => {
  return { title: 'menuName', key: 'menuId' };
});

// ----------------------- 提交数据 ------------------------
async function onSubmit () {
  try {
    let checkedData = roleStore.checkedData;
    if (_.isEmpty(checkedData)) {
      message.error('还未选择任何权限');
      return;
    }
    form.menuIdList = checkedData;
    await employeeMenuApi.updateEmployeeMenu(form)
    SmartLoading.show();
    onClose();
  } catch (error) {
    console.log(error);
  } finally {
    SmartLoading.hide();
  }
}

// ----------------------- 加载菜单树 ------------------------
const menuTree = ref([]);

async function getMenuTree (employeeId) {
  try {
    SmartLoading.show();
    let result = await employeeMenuApi.getEmployeeSelectedMenu(employeeId);
    let data = result.data;
    menuTree.value = data.menuTreeList;
    roleStore.initCheckedData(data.selectedMenuId || []);
  } catch (error) {
    console.log(error);
  } finally {
    SmartLoading.hide();
  }
}
// ----------------------- 展开菜单树 ------------------------
let expandedKeys = ref([]);
let expandFlag = ref(false);

function expandTree () {
  expandFlag.value = !expandFlag.value;
  if (!expandFlag.value) {
    expandedKeys.value = [];
    return;
  }
  expandAll(menuTree.value);
}

// 全部展开
function expandAll (data) {
  data.forEach(e => {
    e.expand = !e.expand;
    expandedKeys.value.push(e.menuId);
    if (!_.isEmpty(e.children)) {
      expandAll(e.children);
    }
  });
}

async function syncRoleMenu() {
  try {
    SmartLoading.show();
    let result = await employeeMenuApi.getEmployeeRoleMenu(form.employeeId);
    let data = result.data;
    roleStore.initCheckedData(data.selectedMenuId || []);
  } catch (error) {
    console.log(error);
  } finally {
    SmartLoading.hide();
  }
}

function clearMenu() {
  roleStore.initCheckedData([]);
}
</script>
<style scoped lang="less">

</style>

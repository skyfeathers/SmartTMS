<!--
 * @Author: zhuoda
 * @Date: 2021-08-25 21:53:06
 * @LastEditTime: 2022-08-25
 * @LastEditors: zhuoda
 * @Description: 菜单树形选择框，（只存在目录和菜单）
-->
<template>
  <a-tree-select
    :value="props.value"
    :treeData="treeData"
    :fieldNames="{ label: 'menuName', key: 'menuId', value: 'menuId' }"
    show-search
    tree-checkable
    style="width: 100%"
    :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
    placeholder="请选择菜单"
    allow-clear
    tree-default-expand-all
    @change="treeSelectChange"
  />
</template>
<script setup>
import { onMounted, ref } from 'vue';
import _ from 'lodash';
import { menuApi } from '/@/api/system/menu/menu-api';
import { buildMenuTableTree } from '/@/views/system/menu/menu-data-handler';
import { MENU_TYPE_ENUM } from '/@/constants/system/menu-const';

const props = defineProps({
  // 绑定值
  value: Array,
  // 单选多选
  multiple: {
    type: Boolean,
    default: false,
  },
});

const emit = defineEmits(['update:value']);

let treeData = ref([]);
onMounted(queryMenuTree);

// 外部调用初始化
let menuList = [];
async function queryMenuTree() {
  let res = await menuApi.queryMenu();
  menuList = res.data.filter((e) => e.menuType === MENU_TYPE_ENUM.MENU.value || e.menuType === MENU_TYPE_ENUM.CATALOG.value);
  for (const item of menuList) {
    if (item.menuType === MENU_TYPE_ENUM.CATALOG.value) {
      item.disabled = true;
    }
  }
  treeData.value = buildMenuTableTree(menuList);
}

/**
 * 根据id集合，获取菜单集合
 */
function getMenuListByIdList(menuIdList) {
  return _.cloneDeep(menuList.filter((e) => menuIdList.indexOf(e.menuId) > -1));
}

function treeSelectChange(e) {
  emit('update:value', e);
}

// ----------------------- 以下是暴露的方法内容 ------------------------
defineExpose({
  queryMenuTree,
  getMenuListByIdList,
});
</script>

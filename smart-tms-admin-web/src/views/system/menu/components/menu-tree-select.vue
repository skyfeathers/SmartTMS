<!--
 * @Author: zhuoda
 * @Date: 2021-08-10 16:53:06
 * @LastEditTime: 2022-06-23
 * @LastEditors: zhuoda
 * @Description: 菜单树下拉选择
 * @FilePath: /smart-admin/src/views/system/menu/components/menu-tree-select.vue
-->
<template>
  <a-tree-select
    :value="props.value"
    :treeData="treeData"
    :fieldNames="{ label: 'menuName', key: 'menuId', value: 'menuId' }"
    show-search
    style="width: 100%"
    :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
    placeholder="请选择菜单"
    allow-clear
    tree-default-expand-all
    @change="treeSelectChange"
    treeNodeFilterProp="menuName"
  />
</template>
<script setup>
  import { onMounted, ref, watch } from 'vue';
  import { menuApi } from '/@/api/system/menu/menu-api';
  import _ from 'lodash';

  const props = defineProps({
    value: Number,
  });

  let treeData = ref([]);
  async function queryMenuTree() {
    let res = await menuApi.queryMenuTree(true);
    treeData.value = res.data;
  }

  onMounted(queryMenuTree);

  // --------------- 触发时间 ---------------
  const emit = defineEmits(['update:value']);
  function treeSelectChange(e) {
    emit('update:value', e);
  }

  defineExpose({
    queryMenuTree,
  });
</script>

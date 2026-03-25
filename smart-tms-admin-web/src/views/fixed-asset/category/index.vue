<!--
 * @Description:
 * @Author: zhuoda
 * @Date: 2021-11-05
 * @LastEditTime: 2022-06-15
 * @LastEditors: zhuoda
-->
<template>
  <a-card size="small" :bordered="false" :hoverable="true">
    <a-tabs default-active-key="1"
            @change="changeTabs">
      <a-tab-pane v-for="item in TABS_LIST"
                  :key="item.value"
                  :tab="item.desc"/>
    </a-tabs>
    <category-tree-table ref="treeRef" :categoryType="activeKey"/>
  </a-card>
</template>
<script setup>
import CategoryTreeTable from './components/category-tree-table.vue';
import { CATEGORY_TYPE_ENUM } from '/@/constants/business/category-const';
import { nextTick, ref } from 'vue';

const TABS_LIST = [
  {
    value: CATEGORY_TYPE_ENUM.FIXED_ASSET.value,
    desc: CATEGORY_TYPE_ENUM.FIXED_ASSET.desc,
  },
  {
    value: CATEGORY_TYPE_ENUM.CONSUMABLES.value,
    desc: CATEGORY_TYPE_ENUM.CONSUMABLES.desc,
  },
];

const activeKey = ref(TABS_LIST[0].value);
const treeRef = ref();

function changeTabs (value) {
  console.log('change')
  activeKey.value = value;
  nextTick(() => {
    console.log(treeRef.value)
    if (treeRef.value) {
      treeRef.value.queryList();
    }
  });
}

</script>

<!--
 * @Author: zhuoda
 * @Date: 2021-08-03 10:27:11
 * @LastEditTime: 2022-06-23
 * @LastEditors: zhuoda
 * @Description:
 * @FilePath: /smart-admin/src/layout/smart-layout.vue
-->
<template>
  <!--左侧菜单 模式-->
  <smart-side-layout v-if="layout === LAYOUT_ENUM.SIDE.value" />
  <!--左侧展开菜单 模式-->
  <smart-side-expand-layout v-if="layout === LAYOUT_ENUM.SIDE_EXPAND.value" />
</template>
<script setup>
  import { computed } from 'vue';
  import SmartSideExpandLayout from './smart-side-expand-layout.vue';
  import { LAYOUT_ENUM } from '/@/constants/layout-const';
  import SmartSideLayout from '/@/layout/smart-side-layout.vue';
  import { useAppConfigStore } from '/@/store/modules/system/app-config';

  const appConfigStore = useAppConfigStore();
  const layout = computed(() => appConfigStore.$state.layout);
</script>

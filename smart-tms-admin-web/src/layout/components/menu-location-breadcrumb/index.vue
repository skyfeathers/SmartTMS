<!--
 * @Author: zhuoda
 * @Date: 2021-08-03 10:27:11
 * @LastEditTime: 2022-06-23
 * @LastEditors: zhuoda
 * @Description: 
 * @FilePath: /smart-admin/src/components/menu-location-breadcrumb/index.vue
-->
<template>
  <a-breadcrumb separator=">" style="display: inline;" v-show="changeBreadCrumbFlag">
    <a-breadcrumb-item v-for="(item, index) in parentMenuList" :key="index">{{ item.title }}</a-breadcrumb-item>
    <a-breadcrumb-item>{{ currentRoute.meta.title }}</a-breadcrumb-item>
  </a-breadcrumb>
</template>
<script setup>
  import { useRoute } from 'vue-router';
  import { useUserStore } from '/@/store/modules/system/user';
  import { computed } from '@vue/reactivity';
  import { useAppConfigStore } from '/@/store/modules/system/app-config';

  const changeBreadCrumbFlag = computed(() => useAppConfigStore().$state.changeBreadCrumbFlag);

  let currentRoute = useRoute();
  const parentMenuList = computed(() => {
    let currentName = currentRoute.name;
    if (!currentName || typeof currentName !== 'string') {
      return [];
    }
    let menuParentIdListMap = useUserStore().getMenuParentIdListMap;
    return menuParentIdListMap.get(currentName) || [];
  });
</script>

<!--
 * @Description: App
 * @Author: zhuoda
 * @Date: 2021-08-03
 * @LastEditTime: 2022-06-23
 * @LastEditors: xjq
-->
<template>
  <a-config-provider :locale="antdLocale" 
  :theme="{
      algorithm: compactFlag ? theme.compactAlgorithm : theme.defaultAlgorithm,
    }">
    <!---全局loading，常用于表单提交--->
    <a-spin :spinning="spinning" size="large">
      <RouterView />
    </a-spin>
  </a-config-provider>
</template>

<script setup>
import dayjs from 'dayjs';
import { computed } from 'vue';
import { messages } from '/@/i18n/index';
import { useAppConfigStore } from '/@/store/modules/system/app-config';
import { useSpinStore } from '/@/store/modules/system/spin';
import { theme } from 'ant-design-vue';
const antdLocale = computed(() => messages[useAppConfigStore().language].antdLocale);
const dayjsLocale = computed(() => messages[useAppConfigStore().language].dayjsLocale);
dayjs.locale(dayjsLocale);
// 是否紧凑
const compactFlag = computed(() => useAppConfigStore().compactFlag);
let spinStore = useSpinStore();
const spinning = computed(() => spinStore.loading);
</script>

<style lang="less" scoped>

::v-deep(.ant-table .ant-table-thead >tr>th:active[draggable]){
    background: #1890ff !important;
    transition: background 0s ease 0.1s;
}

::v-deep(.ant-table .ant-table-thead >tr>th){
    -webkit-user-select: none; /* Safari 3.1+ */
    -moz-user-select: none;    /* Firefox 2+ */
    -ms-user-select: none;     /* IE 10+ */
    user-select: none;
}
</style>
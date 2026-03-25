<!--
 * @Description: 底部信息
 * @Author: zhuoda
 * @Date: 2021-08-03
 * @LastEditTime: 2022-06-23
 * @LastEditors: zhuoda
-->
<template>
  <div class="version" v-show="footerFlag">
    <a target="_blank" href="">
      {{ websiteName }} @copyright 2015-{{ currentYear }}
    </a>
  </div>
</template>
<script setup>
  import { computed } from '@vue/reactivity';
  import dayjs from 'dayjs';
  import { useAppConfigStore } from '/@/store/modules/system/app-config';

  const footerFlag = computed(() => useAppConfigStore().$state.footerFlag);
  const websiteName = computed(() => useAppConfigStore().websiteName);

  const currentYear = dayjs().year();
</script>

<style lang="less" scoped>
  .version {
    font-size: 14px;
    color: rgba(0, 0, 0, 0.45);
    a {
      color: rgba(0, 0, 0, 0.45);
    }
    a:hover {
      color: @primary-color;
    }
  }
</style>

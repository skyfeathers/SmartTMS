<!--
 * @Description: 
 * @Author: zhuoda
 * @Date: 2022-06-24
 * @LastEditTime: 2022-06-24
 * @LastEditors: zhuoda
-->
<template>
  <a-result status="403" title="对不起，您访问的内容不存在！">
    <template #extra>
      <a-button type="primary" @click="goHome">返回首页</a-button>
    </template>
  </a-result>
</template>
<script setup>
  import { useRouter } from 'vue-router';
  import { HOME_PAGE_NAME } from '/@/constants/system/home-const';

  function goHome() {
    useRouter().push({ name: HOME_PAGE_NAME });
  }
</script>

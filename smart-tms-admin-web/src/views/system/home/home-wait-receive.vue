<!--
 * @Description: file content
 * @Author: yandy
 * @Date: 2022-08-03 15:35:07
 * @LastEditors:
 * @LastEditTime: 2022-08-03 15:35:07
-->
<template>
  <default-home-card icon="dollar-outlined" title="待收款">
    <ReceiveOrderTable/>
  </default-home-card>
</template>
<script setup>
import DefaultHomeCard from "/@/views/system/home/components/default-home-card.vue";
import ReceiveOrderTable from '/@/views/business/report/receive-order/components/receive-order-table.vue';
</script>
<style lang="less" scoped>
</style>

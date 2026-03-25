<!--
 * @Description: 
 * @Author: zhuoda
 * @Date: 2021-11-05
 * @LastEditTime: 2022-06-15
 * @LastEditors: zhuoda
-->
<template>
  <a-card size="small" :bordered="false" :hoverable="true">
    <category-tree-table :category-type="CATEGORY_TYPE_ENUM.GOODS.value" />
  </a-card>
</template>
<script setup>
import CategoryTreeTable from "./components/category-tree-table.vue";
import { CATEGORY_TYPE_ENUM } from "/@/constants/business/category-const";
</script>

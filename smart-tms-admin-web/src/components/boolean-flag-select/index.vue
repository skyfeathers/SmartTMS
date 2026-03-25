<!--
 * @Description: 布尔值下拉选择器
 * @version: 
 * @Author: 李云飞 qq:23983208
 * @Date: 2022-07-14 09:14:02
 * @LastEditors: 李云飞 qq:23983208
 * @LastEditTime: 2022-07-19 10:26:39
-->
<template>
  <a-select :placeholder="placeholder"
    :allowClear="allowClear"
    @change="handleChange">
    <a-select-option :value="1">{{trueText}}</a-select-option>
    <a-select-option :value="2">{{falseText}}</a-select-option>
  </a-select>
</template>

<script setup>
import { ref, watch } from 'vue';
const props = defineProps({
  trueText: {
    type: String,
    default: '启用',
  },
  falseText: {
    type: String,
    default: '禁用',
  },
  allowClear: {
    type: Boolean,
    default: true,
  },
  placeholder: {
    type: String,
    default: '请选择',
  },
});

const emits = defineEmits(['update:value', 'change']);

const handleChange = (value) => {
  emits('update:value', value);
  emits('change', value);
};
</script>

<style lang='less' scoped>
</style>
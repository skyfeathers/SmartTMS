<!--
 * @Author: zhuoda
 * @Date: 2021-08-10 16:53:06
 * @LastEditTime: 2022-08-18
 * @LastEditors: zhuoda
 * @Description: 部门树下拉选择
 * @FilePath: /smart-admin/src/views/system/employee/department/components/department-tree-select/index.vue
-->
<template>
  <a-tree-select
    :value="props.value"
    :treeData="treeData"
    :fieldNames="{ label: 'name', key: 'departmentId', value: 'departmentId' }"
    show-search
    style="width: 100%"
    :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
    placeholder="请选择部门"
    allow-clear
    tree-default-expand-all
    :multiple="props.multiple"
    @change="treeSelectChange"
  />
</template>
<script setup>
import { onMounted, ref } from 'vue';
import _ from 'lodash';
import { departmentApi } from '/@/api/system/department/department-api';

const props = defineProps({
  // 绑定值
  value: {
    type: [Number, Array],
  },
  // 单选多选
  multiple: {
    type: Boolean,
    default: false,
  },
});

const emit = defineEmits(['update:value']);

let treeData = ref([]);
onMounted(queryDepartmentTree);
// 外部调用初始化
async function queryDepartmentTree() {
  let res = await departmentApi.queryDepartmentTree();
  treeData.value = res.data;
}

function treeSelectChange(e) {
  emit('update:value', e);
}

// ----------------------- 以下是暴露的方法内容 ------------------------
defineExpose({
  queryDepartmentTree,
});
</script>

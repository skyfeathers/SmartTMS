<!--
 * @Description: 选择部门列表
 * @version: 
 * @Author: 李云飞 qq:23983208
 * @Date: 2022-07-15 11:52:06
 * @LastEditors: zhuoda
 * @LastEditTime: 2022-08-18
-->
<template>
  <a-table
    rowKey="departmentId"
    :columns="tableColumns"
    :dataSource="tableData"
    :defaultExpandedRowKeys="defaultOpenRowKeys"
    :rowSelection="rowSelection"
    :scroll="{ y: 500 }"
    :pagination="false"
    size="middle"
    bordered
  />
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue';
import lodash from 'lodash';
import { departmentApi } from '/@/api/system/department/department-api';

const emits = defineEmits(['changeSelect']);

const tableColumns = [
  {
    title: '名称',
    dataIndex: 'name',
  },
];

const tableData = ref([]);

onMounted(() => {
  departmentTree();
});

async function departmentTree() {
  try {
    const result = await departmentApi.queryDepartmentTree();
    console.log(result);
    if (!lodash.isEmpty(result.data)) {
      tableData.value = result.data;
    }
  } catch (err) {
    console.log(err);
  }
}

// 默认展开的行
const defaultOpenRowKeys = ref([]);

const rowSelection = reactive({
  checkStrictly: false,
  selectedRowKeys: [],
  onChange: (selectedKeys, selectedRows) => {
    rowSelection.selectedRowKeys = selectedKeys;
    emits('changeSelect', { selectedKeys, selectedRows });
  },
  getCheckboxProps: (record) => ({
    // 有子级的行禁止选择
    disabled: !lodash.isEmpty(record.children),
  }),
});
</script>

<style lang="less" scoped></style>

<!--
 * @Description: 选择员工列表
 * @version: 
 * @Author: 李云飞 qq:23983208
 * @Date: 2022-07-15 11:52:06
 * @LastEditors: 李云飞 qq:23983208
 * @LastEditTime: 2022-07-16 15:06:38
-->
<template>
  <a-table rowKey="key"
    :columns="tableColumns"
    :dataSource="tableData"
    :defaultExpandedRowKeys="defaultOpenRowKeys"
    :rowSelection="{checkStrictly:false, selectedRowKeys: selectedRowKeyList, onChange: onSelectChange }"
    :scroll="{ y: 500 }"
    :pagination="false"
    size="middle"
    bordered />
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue';

const tableColumns = [
  {
    title: '名称',
    dataIndex: 'name',
  },
];

const tableData = [
  {
    key: 1,
    name: '1部',
    children: [
      {
        key: 11,
        name: '马11帅',
      },
      {
        key: 12,
        name: '马12帅',
      },
      {
        key: 13,
        name: '马13帅',
      },
    ],
  },
  {
    key: 2,
    name: '2部',
    children: [
      {
        key: 21,
        name: '马21帅',
      },
      {
        key: 22,
        name: '马22帅',
      },
      {
        key: 23,
        name: '马23帅',
      },
    ],
  },
  {
    key: 3,
    name: '3部',
    children: [
      {
        key: 31,
        name: '马31帅',
      },
      {
        key: 32,
        name: '马32帅',
      },
      {
        key: 33,
        name: '马33帅',
      },
    ],
  },
  {
    key: 4,
    name: '4部',
    children: [
      {
        key: 41,
        name: '马41帅',
      },
      {
        key: 42,
        name: '马42帅',
      },
      {
        key: 43,
        name: '马43帅',
      },
      {
        key: 44,
        name: '马44帅',
      },
    ],
  },
];

// 默认展开的行
const defaultOpenRowKeys = ref([]);

// 选中项的 key列表
const selectedRowKeyList = ref([]);

// 选中项的数据列表
const selectedRowsList = ref([]);

// 选中项发生变化时的回调
function onSelectChange(selectedKeys, selectedRows) {
  selectedRowKeyList.value = selectedKeys;
  selectedRowsList.value = selectedRows;
}
</script>

<style lang='less' scoped>
</style>
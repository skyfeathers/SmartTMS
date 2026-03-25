<template>
    <a-table :dataSource="dataSource" :columns="columns" />
</template>
<script setup>
import { ref, h } from 'vue';
import useDragTable from '/@/hook/use-drag-table/index'
import ellipsisRender from '/@/utils/ellipsis-render/index.js'
const dataSource = ref([
    {
        key: '1',
        name: '胡彦斌11111111111111111111111111111111111111111111',
        age: 32,
        address: '西湖区湖底公园1号',
    },
    {
        key: '2',
        name: '胡彦祖',
        age: 42,
        address: '西湖区湖底公园1号',
    },
])

const { columnsData:columns, tableWidth } = useDragTable([
    {
        title: '姓名',
        width:200,
        dataIndex: 'name',
        key: 'name',
        ellipsis:true,
        customRender:ellipsisRender
    },
    {
        title: '年龄',
        dataIndex: 'age',
        key: 'age',
    },
    {
        title: '住址',
        dataIndex: 'address',
        key: 'address'
    },
])
</script>

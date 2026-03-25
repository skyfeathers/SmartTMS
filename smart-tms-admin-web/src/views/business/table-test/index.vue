<template>
    <a-button type="primary" @click="search">查询</a-button>
    <a-button type="default" @click="reset">重置</a-button>
    <a-table size="small" bordered :dataSource="dataSource" :columns="columns">
        <template #headerCell="{ column }">
            <template v-if="column.dataIndex == 'address'">
                <HeaderCell :column="column">
                    插槽内容
                </HeaderCell>
            </template>
            <template v-else-if="column.filterOptions">
                <HeaderCell v-model:value="queryParams[column.dataIndex]" :column="column" @change="change"/>
            </template>
        </template>
        <template #bodyCell="{ text, record, index, column }">
            <template  v-if="column.dataIndex === 'status'">
                {{ $smartEnumPlugin.getDescByValue('AUDIT_STATUS_ENUM', text) }}
            </template>
        </template>
    </a-table>
</template>
<script setup>
import { ref } from 'vue';
import HeaderCell from '/@/components/smart-table-header-cell/index.vue'
import useDragTable from '/@/hook/use-drag-table/index'
const dataSource = ref([
    {
        key: '1',
        name: '胡彦斌',
        age: 32,
        address: '西湖区湖底公园1号',
        datetime:'2024-06-06 11:11:11',
        status:1,
        bankType:''
    },
    {
        key: '2',
        name: '胡彦祖',
        age: 42,
        address: '西湖区湖底公园1号',
        datetime:'2024-06-06 11:11:11',
        status:2,
        bankType:''
    },
])

const { columnsData:columns, tableWidth } = useDragTable([
    {
        title: '姓名',
        wdith:200,
        dataIndex: 'name',
        key: 'name',
        ellipsis:true,
        filterOptions:{
            type:'input',
        }
    },
    {
        title: '年龄',
        dataIndex: 'age',
        key: 'age',
        wdith:200,
        filterOptions:{
            type:'aaaa'
        }
    },
    {
        title: '住址',
        dataIndex: 'address',
        key: 'address',
        wdith:200,
    },
    {
        title: '审核状态',
        dataIndex: 'status',
        key: 'status',
        wdith:200,
        filterOptions:{
            type:'enum-select',
            enumName:'AUDIT_STATUS_ENUM'
        }
    },
    {
        title: '银行类型',
        dataIndex: 'bankType',
        key: 'bankType',
        wdith:200,
        filterOptions:{
            type:'dict-select',
            keyCode:'BANK-TYPE',
            multiple:true
        }
    },
    {
        title: '日期时间',
        dataIndex: 'datetime',
        key: 'datetime',
        wdith:200,
        filterOptions:{
            type:'datetime-range',
            ranges:true
        }
    },
    {
        title: '日期',
        dataIndex: 'date',
        key: 'date',
        wdith:200,
        filterOptions:{
            type:'date-range',
            ranges:true
        }
    },
])

const defaultParams = {
    name:undefined,
    datetime:[]
}

const queryParams = ref({
    name:'1321',
    datetime:['2024-03-01 22:22:22','2024-04-06 22:22:22']
})

function reset(){
    queryParams.value = {
        ...defaultParams
    }
}

function search(){
    console.log(queryParams.value);
}

function change(obj){
    console.log(obj);
}
</script>

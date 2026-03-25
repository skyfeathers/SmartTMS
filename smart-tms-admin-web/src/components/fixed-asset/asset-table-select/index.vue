<!--
  * 固定资产 - table展示包括弹窗选择
  *
  * @Author:    lidoudou
  * @Date:      2023-03-18 09:15:14
  * @Copyright  1024创新实验室
-->
<template>
  <a-card title="资产列表" size="small" :bordered="false" :hoverable="false" class="smart-margin-bottom5">
    <template #extra>
      <a-button @click="showModal" size="small" type="primary" v-if="operateFlag">选择资产</a-button>
    </template>
    <!---------- 表格 begin ----------->
    <a-table
        size="small"
        :dataSource="tableData"
        :columns="columns"
        rowKey="assetId"
        bordered
        :pagination="false"
    >
      <template #bodyCell="{ text, record, column ,index}">
        <template v-if="column.dataIndex === 'assetName' || column.dataIndex === 'assetNo'">
          <a @click="toDetail(record.assetId)">{{record[column.dataIndex]}}</a>
        </template>
        <template v-if="column.dataIndex === 'sourceId'">
          {{record.sourceId[0].valueName}}
        </template>
        <template v-if="column.dataIndex === 'status'">
          {{ $smartEnumPlugin.getDescByValue('ASSET_STATUS_ENUM', record.status) }}
        </template>
        <template v-if="column.dataIndex === 'action' && operateFlag">
          <a-button @click="remove(index)" size="small" type="link" danger>移除</a-button>
        </template>
      </template>
    </a-table>
    <!---------- 表格 end ----------->
    <AssetSelect ref="assetSelectRef" @confirmSelect="confirmSelect"/>
  </a-card>
</template>
<script setup>
import AssetSelect from '../asset-select/index.vue'

import { ref, onMounted, watch, inject, } from 'vue';
import { useRouter } from 'vue-router';

import _ from 'lodash';
// ---------------------------- props ----------------------------
const props = defineProps({
  // 默认展示的资产列表，可为空
  assetList: {
    type: Array,
    default:()=>{
      return []
    }
  },
  // 是否展示操作按钮，默认不展示
  operateFlag: {
    type: Boolean,
    default: false
  }
});

// ---------------------------- table操作 ----------------------------
let tableData = ref([]);
watch(() => props.assetList,
    (value) => {
      if (!_.isEmpty(props.assetList)) {
        tableData.value = _.cloneDeep(props.assetList);
      }
    },
    {
      immediate: true,
    }
);


function remove(index){
  tableData.value.splice(index, 1);
}

const assetSelectRef = ref();

let customQueryForm = inject('setCustomQueryForm');
let setValidateParam = inject('setValidateParam');

function showModal () {
  if (setValidateParam && !setValidateParam(customQueryForm)) {
    return;
  }
  assetSelectRef.value.showModal(tableData.value);
}

function confirmSelect (selectedAssetList) {
  tableData.value = _.cloneDeep(selectedAssetList);
}

function clear () {
  tableData.value = [];
}
// ---------------------------- 跳转 ----------------------------
let router = useRouter();
function toDetail (assetId) {
  router.push({
    path: '/fixed-asset/asset-detail',
    query: {
      assetId
    }
  });
}
onMounted(() => {
  // 如果显示按钮，则设置操作列
  if (props.operateFlag) {
    if (_.isEmpty(columns.value.filter(e => e.dataIndex == 'action'))) {
      columns.value.push({
        title: '操作',
        dataIndex: 'action',
        width: 80
      });
    }
  }
});

// ---------------------------- 表格列 ----------------------------
const columns = ref([
  {
    title: '资产名称',
    dataIndex: 'assetName',
  },
  {
    title: '资产编号',
    dataIndex: 'assetNo',
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 60
  },
  {
    title: '所属分类',
    dataIndex: 'categoryName',
  },
  {
    title: '资产来源',
    dataIndex: 'sourceId',
    width: 90
  },
  {
    title: '计量单位',
    dataIndex: 'unit',
  },
  {
    title: '存放位置',
    dataIndex: 'locationName',
  },
  {
    title: '创建人',
    dataIndex: 'createUserName',
    width: 80
  },
]);


defineExpose({
  tableData,
  clear
})
</script>

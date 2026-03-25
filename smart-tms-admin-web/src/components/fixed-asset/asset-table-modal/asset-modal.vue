<template>
  <a-modal title="资产列表" v-model:open="visible" :width="800">
    <a-table
        :scroll="{x:1500}"
        size="small"
        :dataSource="tableData"
        :columns="columns"
        rowKey="assetId"
        bordered
        :pagination="false"
    >
      <template #bodyCell="{ text, record, column }">
        <template v-if="column.dataIndex === 'sourceId'">
          {{ record.sourceId[0].valueName }}
        </template>
      </template>
    </a-table>
    <template #footer>
      <a-button @click="onClose" type="primary">关闭</a-button>
    </template>
  </a-modal>
</template>
<script setup>
import { ref } from 'vue';
// ---------------------------- 表格列 ----------------------------

const columns = ref([
  {
    title: '编号',
    dataIndex: 'assetNo',
    width: 140
  },
  {
    title: '资产名称',
    dataIndex: 'assetName',
  },
  {
    title: '所属分类',
    dataIndex: 'categoryName',
  },
  {
    title: '计量单位',
    dataIndex: 'unit',
  },
  {
    title: '品牌',
    dataIndex: 'brand',
  },
  {
    title: '规格型号',
    dataIndex: 'model',
  },
  {
    title: '设备序列号',
    dataIndex: 'serialId',
  },
  {
    title: '存放位置',
    dataIndex: 'locationName',
  },
  {
    title: '所属公司',
    dataIndex: 'enterpriseName',
  },
  {
    title: '供应商',
    dataIndex: 'supplierName',
  },
  {
    title: '预计使用期限(月)',
    dataIndex: 'monthCount',
    width: 130,
  },
]);

const visible = ref(false);
const tableData = ref([]);

function showModal (assetList) {
  visible.value = true;
  tableData.value = assetList;
}

function onClose () {
  visible.value = false;
}

defineExpose({
  showModal
});
</script>

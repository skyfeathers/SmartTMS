<template>
  <a-card title="资产列表">
    <template #extra>
      <a-button @click="addAsset" type="primary" size="small">新增</a-button>
    </template>

    <a-table
        :scroll="{x:1100}"
        size="small"
        :dataSource="assetList"
        :columns="columns"
        rowKey="consumablesId"
        bordered
        :pagination="false"
    >
      <template #headerCell="{ column }">
        <template v-if="column.required">
          {{ column.title }}<span class="required">*</span>
        </template>
      </template>
      <template #bodyCell="{ text, record, column , index}">
        <template v-if="column.dataIndex === 'price'">
          <a-input-number v-model:value="record.price" placeholder="请输入采购单价" style="width: 180px" :min="0"
                          :precision="2" :max="99999999"/>
        </template>
        <template v-if="column.dataIndex === 'count'">
          <a-input-number v-model:value="record.count" placeholder="请输入采购数量" style="width: 180px" :min="0"
                          :precision="0"/>
        </template>

        <template v-if="column.dataIndex === 'action'">
          <div class="smart-table-operate">
            <a-button @click="removeAsset(index)" type="link">移除</a-button>
          </div>
        </template>
      </template>
    </a-table>

    <ConsumablesStockTableSelect ref="selectModalRef" @confirmSelect="confirmSelect"/>
  </a-card>
</template>
<script setup>
import ConsumablesStockTableSelect from '/@/components/fixed-asset/consumables-stock-table-select/index.vue';

import { ref } from 'vue';
import { message } from 'ant-design-vue';
import _ from 'lodash';

// ---------------- 操作Table ----------------
const columns = ref([
  {
    title: '耗材编号',
    dataIndex: 'consumablesNo',
  },
  {
    title: '耗材名称',
    dataIndex: 'consumablesName',
  },
  {
    title: '所属分类',
    dataIndex: 'categoryName',
  },
  {
    title: '采购单价',
    dataIndex: 'price',
    required: true
  },
  {
    title: '采购数量',
    dataIndex: 'count',
    required: true
  },
  {
    title: '操作',
    dataIndex: 'action'
  }
]);
const assetList = ref([]);

function validate () {
  let validateFlag = true;
  for (let i = 0; i < assetList.value.length; i++) {
    let asset = assetList.value[i];
    if (!asset.price) {
      validateFlag = false;
      message.error(`序号${i + 1}的【采购价格】不能为空`);
      break;
    }
    if (!asset.count) {
      validateFlag = false;
      message.error(`序号${i + 1}的【采购数量】不能为空`);
      break;
    }
  }
  if (!validateFlag) {
    return Promise.reject();
  }
  return Promise.resolve(assetList.value);
}

// ---------------- 操作Table ----------------
function confirmSelect (selectedAssetList) {
  assetList.value = _.cloneDeep(selectedAssetList);
}

const selectModalRef = ref();

function addAsset () {

  selectModalRef.value.showModal(_.cloneDeep(assetList.value), {});
}

function removeAsset (index) {
  assetList.value.splice(index, 1);
}

function clear () {
  assetList.value = [];
}

defineExpose({
  assetList,
  validate,
  clear
});
</script>
<style scoped lang="less">
.required {
  color: red;
}
</style>

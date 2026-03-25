<template>
  <a-card title="资产列表">
    <template #extra>
      <a-button @click="addAsset" type="primary" size="small">新增</a-button>
    </template>

    <a-table
        :scroll="{x:2100}"
        size="small"
        :dataSource="assetList"
        :columns="columns"
        rowKey="assetId"
        bordered
        :pagination="false"
    >
      <template #headerCell="{ column }">
        <template v-if="column.required">
          {{ column.title }}<span class="required">*</span>
        </template>
      </template>
      <template #bodyCell="{ text, record, column , index}">
        <template v-if="column.dataIndex === 'index'">
          {{ index + 1 }}
        </template>
        <template v-if="column.dataIndex === 'assetNo'">
          <a-input v-model:value="record.assetNo" placeholder="请输入资产编号" style="width:200px"/>
          <a-tooltip placement="top">
            <template #title>
              <span>为空，则由系统自动生成</span>
            </template>
            <question-circle-outlined />
          </a-tooltip>
        </template>
        <template v-if="column.dataIndex === 'assetName'">
          <a-input v-model:value="record.assetName" placeholder="请输入资产名称" style="width:200px"/>
        </template>
        <template v-if="column.dataIndex === 'categoryId'">
          <CategoryTreeSelect v-model:value="record.categoryId" :categoryType="CATEGORY_TYPE_ENUM.FIXED_ASSET.value"
                              placeholder="请选择所属分类" style="width:200px"/>
        </template>
        <template v-if="column.dataIndex === 'unit'">
          <a-input v-model:value="record.unit" placeholder="请输入计量单位" style="width:140px"/>
        </template>
        <template v-if="column.dataIndex === 'brand'">
          <a-input v-model:value="record.brand" placeholder="请输入品牌" style="width:120px"/>
        </template>
        <template v-if="column.dataIndex === 'model'">
          <a-input v-model:value="record.model" placeholder="请输入规格型号" style="width:140px"/>
        </template>
        <template v-if="column.dataIndex === 'serialId'">
          <a-input v-model:value="record.serialId" placeholder="请输入设备序列号" style="width:160px"/>
        </template>
        <template v-if="column.dataIndex === 'locationId'">
          <LocationSelect v-model:value="record.locationId" placeholder="请选择存放位置" style="width:180px"/>
        </template>
        <template v-if="column.dataIndex === 'price'">
          <a-input-number v-model:value="record.price" placeholder="请输入购入价格" style="width: 120px" :min="0" :precision="2" :max="99999999"/>
        </template>
        <template v-if="column.dataIndex === 'residualValueRate'">
          <a-input-number v-model:value="record.residualValueRate" placeholder="请输入残值率" style="width: 120px" :min="0" :precision="0" :max="100"/>
        </template>
        <template v-if="column.dataIndex === 'supplierName'">
          <a-input v-model:value="record.supplierName" placeholder="请输入供应商" style="width:120px"/>
        </template>
        <template v-if="column.dataIndex === 'monthCount'">
          <a-input-number v-model:value="record.monthCount" placeholder="请输入使用期限" style="width: 120px" :min="0"/>
        </template>

        <template v-if="column.dataIndex === 'action'">
          <div class="smart-table-operate">
            <a-button @click="removeAsset(index)" type="link">移除</a-button>
          </div>
        </template>
      </template>
    </a-table>
  </a-card>
</template>
<script setup>
import LocationSelect from '/@/components/fixed-asset/locaton-select/index.vue';
import CategoryTreeSelect from '/@/components/fixed-asset/category-tree-select/index.vue';

import { CATEGORY_TYPE_ENUM } from '/@/constants/business/category-const';
import { reactive, ref } from 'vue';
import { message } from 'ant-design-vue';

// ---------------- 操作Table ----------------
const columns = ref([
  {
    title: '序号',
    dataIndex: 'index',
    index: 40
  },
  {
    title: '编号',
    dataIndex: 'assetNo',
  },
  {
    title: '资产名称',
    dataIndex: 'assetName',
    required: true
  },
  {
    title: '所属分类',
    dataIndex: 'categoryId',
    required: true
  },
  {
    title: '存放位置',
    dataIndex: 'locationId',
    required: true
  },
  {
    title: '预计使用期限(月)',
    dataIndex: 'monthCount',
    width: 150,
    required: true
  },
  {
    title: '购入价格',
    dataIndex: 'price',
    required: true
  },
  {
    title: '残值率',
    dataIndex: 'residualValueRate',
    required: true
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
    title: '供应商',
    dataIndex: 'supplierName',
  },
  {
    title:'操作',
    dataIndex: 'action'
  }
]);
const assetList = ref([]);

function validate(){
  let validateFlag = true;
  for (let i = 0; i < assetList.value.length; i++) {
    let asset = assetList.value[i];
    if(!asset.assetName){
      validateFlag = false;
      message.error(`序号${i + 1}的【资产名称】不能为空`);
      break;
    }
    if(!asset.categoryId){
      validateFlag = false;
      message.error(`序号${i + 1}的【所属分类】不能为空`);
      break;
    }
    if(!asset.locationId){
      validateFlag = false;
      message.error(`序号${i + 1}的【存放位置】不能为空`);
      break;
    }
    if(!asset.monthCount){
      validateFlag = false;
      message.error(`序号${i + 1}的【预计使用期限】不能为空`);
      break;
    }
  }
  if (!validateFlag) {
    return Promise.reject();
  }
  return Promise.resolve(assetList.value);
}
// ---------------- 操作Table ----------------
function addAsset () {
  assetList.value.push({
    residualValueRate: 5
  });
}

function removeAsset (index) {
  assetList.value.splice(index, 1);
}

defineExpose({
  assetList,
  validate
});
</script>
<style scoped lang="less">
.required {
  color: red;
}
</style>

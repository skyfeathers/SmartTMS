<template>
  <a-card title="资产列表">
    <template #extra>
      <a-button @click="addConsumables" type="primary" size="small">新增</a-button>
    </template>

    <a-table
        :scroll="{x:1000}"
        size="small"
        :dataSource="consumablesList"
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
        <template v-if="column.dataIndex === 'count'">
          <a-input-number v-model:value="record.count" placeholder="请输入领用数量" style="width: 180px" :min="0"
                          :precision="0"/>
        </template>

        <template v-if="column.dataIndex === 'action'">
          <div class="smart-table-operate">
            <a-button @click="removeConsumables(index)" type="link">移除</a-button>
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

const props = defineProps({
  locationId: {
    type: Number
  }
});
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
    title: '库存',
    dataIndex: 'stockCount',
  },
  {
    title: '领用数量',
    dataIndex: 'count',
    required: true
  },
  {
    title: '操作',
    dataIndex: 'action'
  }
]);
const consumablesList = ref([]);

// 点击确定，验证表单
async function onSubmit () {
  try {
    await formRef.value.validateFields();
  } catch (err) {
    message.error('参数验证错误，请仔细填写表单数据!');
  }
}

function validate () {
  let validateFlag = true;
  for (let i = 0; i < consumablesList.value.length; i++) {
    let consumables = consumablesList.value[i];
    if (!consumables.count) {
      validateFlag = false;
      message.error(`${consumables.consumablesName}的【领用数量】不能为空`);
      break;
    }
  }
  if (!validateFlag) {
    return Promise.reject();
  }
  return Promise.resolve(consumablesList.value);
}

function clear () {
  consumablesList.value = [];
}

// ---------------- 操作Table ----------------
function confirmSelect (selectedConsumablesList) {
  consumablesList.value = _.cloneDeep(selectedConsumablesList);
}

const selectModalRef = ref();

function addConsumables () {
  if (!props.locationId) {
    message.error('请选择所属位置');
    return;
  }
  selectModalRef.value.showModal(_.cloneDeep(consumablesList.value), {
    locationId: props.locationId
  });
}

function removeConsumables (index) {
  consumablesList.value.splice(index, 1);
}

defineExpose({
  consumablesList,
  validate,
  clear
});
</script>
<style scoped lang="less">
.required {
  color: red;
}
</style>

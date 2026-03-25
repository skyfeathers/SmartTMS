<template>
  <a-card title="资产列表" size="small" :bordered="false" :hoverable="true" class="smart-margin-bottom5">
    <template #extra v-if="canCheckFlag && $privilege('consumablesCheck:check')">
      <a-button @click="startCheck" v-if="!checkFlag" size="small" type="primary">开始盘点</a-button>
      <a-button @click="completeCheck" v-else size="small" type="primary">保存盘点</a-button>
    </template>
    <!---------- 表格 begin ----------->
    <a-table
        size="small"
        :dataSource="tableData"
        :columns="columns"
        rowKey="itemId"
        bordered
        :pagination="false"
    >
      <template #bodyCell="{ text, record, column ,index}">
        <template v-if="column.dataIndex === 'status'">
          <template v-if="checkFlag">
            <SmartEnumSelect v-model:value="record.status" enumName="ASSET_CHECK_STATUS_ENUM" width="100%"/>
          </template>
          <span v-else>
            {{ $smartEnumPlugin.getDescByValue('ASSET_CHECK_STATUS_ENUM', record.status) }}
          </span>

        </template>
        <template v-if="column.dataIndex === 'count'">
          <template v-if="checkFlag">
            <a-input-number v-model:value="record.count" style="width:200px" :min="0"/>
          </template>
          <span v-else>{{ record.count }}</span>
        </template>
        <template v-if="column.dataIndex === 'remark'">
          <template v-if="checkFlag">
            <a-input v-model:value="record.remark" style="width:200px"/>
          </template>
          <span v-else>{{ record.remark }}</span>
        </template>
      </template>
    </a-table>
  </a-card>
</template>
<script setup>
import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';
import { SmartLoading } from '/@/components/smart-loading';

import { ref, watch } from 'vue';
import { useRoute } from 'vue-router';
import { message } from 'ant-design-vue';
import _ from 'lodash';
import { consumablesCheckApi } from '/@/api/fixed-asset/asset/consumables-check-api';

const emit = defineEmits(['refresh']);
// ---------------------------- props ----------------------------
const props = defineProps({
  // 默认展示的资产列表，可为空
  itemList: {
    type: Array,
    default: () => {
      return [];
    }
  },
  canCheckFlag: {
    type: Boolean,
    default: false
  }
});

// ---------------------------- table操作 ----------------------------
let tableData = ref([]);
watch(() => props.itemList,
    (value) => {
      if (!_.isEmpty(props.itemList)) {
        tableData.value = _.cloneDeep(props.itemList);
      }
    },
    {
      immediate: true,
    }
);

// ---------------------------- 盘点操作 ----------------------------
const route = useRoute();
let checkId = route.query.checkId;

let checkFlag = ref(false);

function startCheck () {
  checkFlag.value = true;
}

async function completeCheck () {
  try {
    SmartLoading.show();
    let itemList = tableData.value.map(e => {
      return {
        itemId: e.itemId,
        count: e.count,
        status: e.status,
        remark: e.remark
      };
    });
    let params = {
      checkId,
      itemList,
    };
    await consumablesCheckApi.check(params);
    message.success('盘点成功');
    emit('refresh');
    checkFlag.value = false;
  } catch (e) {
    console.log(e);
  } finally {
    SmartLoading.hide();
  }
}

// ---------------------------- 表格列 ----------------------------
const columns = ref([
  {
    title: '耗材名称',
    dataIndex: 'consumablesName',
  },
  {
    title: '耗材编号',
    dataIndex: 'consumablesNo',
  },
  {
    title: '实际库存',
    dataIndex: 'stockCount',
  },
  {
    title: '盘点人',
    dataIndex: 'employeeName'
  },
  {
    title: '盘点时间',
    dataIndex: 'checkTime'
  },
  {
    title: '盘点状态',
    dataIndex: 'status'
  },
  {
    title: '盘点数量',
    dataIndex: 'count'
  },
  {
    title: '备注',
    dataIndex: 'remark'
  }
]);

defineExpose({
  tableData
});
</script>

<template>
  <a-card title="资产列表" size="small" :bordered="false" :hoverable="true" class="smart-margin-bottom5">
    <template #extra v-if="canCheckFlag && $privilege('check:check')">
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

import { ref,  watch } from 'vue';
import { useRoute } from 'vue-router';
import { message } from 'ant-design-vue';
import _ from 'lodash';
import { assetCheckApi } from '/@/api/fixed-asset/asset/check-api';

const emit = defineEmits(['refresh']);
// ---------------------------- props ----------------------------
const props = defineProps({
  // 默认展示的资产列表，可为空
  assetList: {
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
    let assetList = tableData.value.map(e => {
      return {
        itemId: e.itemId,
        status: e.status,
        remark: e.remark
      };
    });
    let params = {
      checkId,
      assetList,
    };
    await assetCheckApi.check(params);
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
    title: '资产名称',
    dataIndex: 'assetName',
  },
  {
    title: '资产编号',
    dataIndex: 'assetNo',
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
    title: '备注',
    dataIndex: 'remark'
  }
]);

defineExpose({
  tableData
});
</script>

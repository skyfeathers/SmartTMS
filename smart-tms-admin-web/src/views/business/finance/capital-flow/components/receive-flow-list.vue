<template>
  <a-card :bordered="false" :hoverable="true" size="small">
    <a-row class="smart-table-btn-block">
      <div class="smart-table-setting-block"></div>
      <div>
        <SmartTableOperator v-model="columns" :tableId="TABLE_ID_CONST.CAPITAL_FLOW_RECEIVE" :refresh="queryList" />
      </div>
    </a-row>

    <a-table
        :columns="columns"
        :dataSource="tableData"
        bordered
        :pagination="false"
        :scroll="{ x: 1300,y:500 }"
        rowKey="receiveOrderId"
        size="small"
    >
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'receiveOrderNumber'">
          <a @click="detailReceiveOrder(record.receiveOrderId)">{{ text }}</a><SmartCopyIcon :value="text" />
        </template>
        <template v-if="column.dataIndex === 'attachment'">
          <file-preview :fileList="text"/>
        </template>
      </template>
    </a-table>
    <div class="smart-query-table-page">
      <a-pagination
          v-model:current="queryForm.pageNum"
          v-model:pageSize="queryForm.pageSize"
          :defaultPageSize="queryForm.pageSize"
          :pageSizeOptions="PAGE_SIZE_OPTIONS"
          :show-total="(total) => `共${total}条`"
          :total="total"
          show-less-items
          showQuickJumper
          showSizeChanger
          @change="queryList"
          @showSizeChange="queryList"
      />
    </div>
  </a-card>
</template>
<script setup>
import FilePreview from '/@/components/file-preview/index.vue';
import SmartTableOperator from '/@/components/smart-table-operator/index.vue';

import { TABLE_ID_CONST } from '/@/constants/table-id-const';
import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import { reactive, ref } from 'vue';
import { capitalFlowApi } from '/@/api/business/capital-flow/capital-flow-api';
import { useSpinStore } from '/@/store/modules/system/spin';
import { router } from '/@/router';

const props = defineProps({
  queryForm: {
    type: Object
  }
});

defineExpose({
  queryList
});

const columns = ref([
  {
    title: '日期',
    dataIndex: 'verificationTime',
    width: 100
  },
  {
    title: '货主简称',
    dataIndex: 'shortName'
  },
  {
    title: '核销金额',
    dataIndex: 'verificationAmount',
    width: 100
  },
  {
    title: '流水单号',
    dataIndex: 'sequenceCode'
  },
  {
    title: '收款单号',
    dataIndex: 'receiveOrderNumber',
  },
  {
    title: '备注',
    dataIndex: 'remark'
  },
  {
    title: '凭证',
    dataIndex: 'attachment',
  },
  {
    title: '创建人',
    dataIndex: 'createUserName'
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 160
  }
]);

let tableData = ref([]);
let total = ref(0);

async function queryList () {
  try {
    useSpinStore().show();
    let responseModel = await capitalFlowApi.queryReceiveOrderFlow(props.queryForm);
    const list = responseModel.data.list;
    total.value = responseModel.data.total;
    tableData.value = list;
  } catch (e) {
    console.log(e);
  } finally {
    useSpinStore().hide();
  }
}

// ----------- 页面跳转 -----------
function detailReceiveOrder (receiveOrderId) {
  router.push({
    path: '/receive-order/detail',
    query: {
      receiveOrderId
    }
  });
}
</script>

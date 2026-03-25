<!--
  * 固定资产 - 归还
  *
  * @Author:    lidoudou
  * @Date:      2023-03-21 10:45:14
-->
<template>
  <!---------- 查询表单form begin ----------->
  <a-form class="smart-query-form">
    <a-row class="smart-query-form-row">
      <a-form-item label="关键字" class="smart-query-form-item">
        <a-input style="width: 150px" v-model:value="queryForm.keywords" placeholder="编号/创建人"/>
      </a-form-item>
      <a-form-item class="smart-query-form-item" label="计提日期">
        <a-range-picker v-model:value="createDateRange" :ranges="defaultTimeRanges" style="width: 100%"
                        @change="changeDate"/>
      </a-form-item>
      <a-form-item class="smart-query-form-item">
        <a-button type="primary" @click="queryData">
          <template #icon>
            <ReloadOutlined/>
          </template>
          查询
        </a-button>
        <a-button @click="resetQuery" class="smart-margin-left10">
          <template #icon>
            <SearchOutlined/>
          </template>
          重置
        </a-button>
      </a-form-item>
    </a-row>
  </a-form>
  <!---------- 查询表单form end ----------->

  <a-card size="small" :bordered="false" :hoverable="true">
    <!---------- 表格操作行 begin ----------->
    <a-row class="smart-table-btn-block">
      <div class="smart-table-operate-block">
        <a-button @click="operate" type="primary" size="small">
          <template #icon>
            <PlusOutlined/>
          </template>
          新建
        </a-button>
      </div>
      <div class="smart-table-setting-block">
        <SmartTableOperator v-model="columns" :tableId="TABLE_ID_CONST.ASSET_DEPRECIATION" :refresh="queryData"/>
      </div>
    </a-row>
    <!---------- 表格操作行 end ----------->

    <!---------- 表格 begin ----------->
    <a-table
        :scroll="{x:1000}"
        size="small"
        :dataSource="tableData"
        :columns="columns"
        rowKey="depreciationId"
        bordered
        :loading="tableLoading"
        :pagination="false"
    >
      <template #bodyCell="{ text, record, column }">
        <template v-if="column.dataIndex === 'depreciationNo'">
          <a @click="detail(record)">{{ record.depreciationNo }}</a>
        </template>
        <template v-if="column.dataIndex === 'depreciationDate'">
          {{ (record.depreciationDate || '').substring(0, 7) }}
        </template>

        <template v-if="column.dataIndex === 'action'">
          <div class="smart-table-operate">
            <a-button @click="cancel(record)" type="link">作废</a-button>
          </div>
        </template>
      </template>
    </a-table>
    <!---------- 表格 end ----------->

    <div class="smart-query-table-page">
      <a-pagination
          showSizeChanger
          showQuickJumper
          show-less-items
          :pageSizeOptions="PAGE_SIZE_OPTIONS"
          :defaultPageSize="queryForm.pageSize"
          v-model:current="queryForm.pageNum"
          v-model:pageSize="queryForm.pageSize"
          :total="total"
          @change="queryData"
          @showSizeChange="queryData"
          :show-total="(total) => `共${total}条`"
      />
    </div>

    <AssetListModal ref="assetListRef"/>
  </a-card>
</template>
<script setup>
import SmartTableOperator from '/@/components/smart-table-operator/index.vue';
import EnterpriseSelect from '/@/components/enterprise-select/index.vue';

import { TABLE_ID_CONST } from '/@/constants/table-id-const';
import { reactive, ref, onMounted } from 'vue';
import { assetDepreciationApi } from '/@/api/fixed-asset/asset/depreciation-api';
import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import { useRouter } from 'vue-router';
import { defaultTimeRanges } from '/@/lib/default-time-ranges';
import { SmartLoading } from '/@/components/smart-loading';
import { message, Modal } from 'ant-design-vue';
// ---------------------------- 表格列 ----------------------------

const columns = ref([
  {
    title: '折旧编号',
    dataIndex: 'depreciationNo',
  },
  {
    title: '计提日期',
    dataIndex: 'depreciationDate',
  },
  {
    title: '创建人',
    dataIndex: 'createUserName',
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 170
  },
  {
    title: '操作',
    dataIndex: 'action',
    fixed: 'right',
    width: 130
  },
]);

// ---------------------------- 查询数据表单和方法 ----------------------------

const queryFormState = {
  keywords: '',
  startTime: null,
  endTime: null,
  pageNum: 1,
  pageSize: 10,
};
const createDateRange = ref([]);
// 查询表单form
const queryForm = reactive({ ...queryFormState });
// 表格加载loading
const tableLoading = ref(false);
// 表格数据
const tableData = ref([]);
// 总数
const total = ref(0);

// 重置查询条件
function resetQuery () {
  let pageSize = queryForm.pageSize;
  Object.assign(queryForm, queryFormState);
  queryForm.pageSize = pageSize;
  createDateRange.value = [];
  queryData();
}

// 查询数据
async function queryData () {
  tableLoading.value = true;
  try {
    let queryResult = await assetDepreciationApi.queryPage(queryForm);
    tableData.value = queryResult.data.list;
    total.value = queryResult.data.total;
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}

onMounted(queryData);

function changeDate (dates, dateStrings) {
  queryForm.startTime = dateStrings[0];
  queryForm.endTime = dateStrings[1];
}

// ---------------------------- 列表操作 ----------------------------
function cancel ({depreciationId}) {
  Modal.confirm({
    title: '提示',
    content: '确认要作废吗',
    onOk: async () => {
      try {
        SmartLoading.show();
        await assetDepreciationApi.cancel(depreciationId);
        message.success('作废成功');
        ajaxQuery();
      } catch (e) {
        console.log(e);
      } finally {
        SmartLoading.hide();
      }
    }
  });
}

// ---------------------------- 跳转 ----------------------------
let router = useRouter();

function operate ({ depreciationId }) {
  router.push({
    path: '/fixed-asset/depreciation-operate',
    query: {
      depreciationId
    }
  });
}

function detail ({ depreciationId }) {
  router.push({
    path: '/fixed-asset/depreciation-detail',
    query: {
      depreciationId
    }
  });
}

</script>

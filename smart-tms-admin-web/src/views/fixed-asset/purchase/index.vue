<!--
  * 固定资产 - 采购
  *
  * @Author:    lidoudou
  * @Date:      2023-03-20 11:15:14
-->
<template>
  <!---------- 查询表单form begin ----------->
  <a-form class="smart-query-form" v-privilege="'purchase:query'">
    <a-row class="smart-query-form-row">
      <a-form-item label="关键字" class="smart-query-form-item">
        <a-input style="width: 150px" v-model:value="queryForm.keywords" placeholder="编号/创建人"/>
      </a-form-item>
      <a-form-item class="smart-query-form-item" label="资产来源">
        <SmartDictSelect keyCode="ASSET-SOURCE" v-model:value="queryForm.sourceId" placeholder="请选择资产来源"
                         style="width:200px"/>
      </a-form-item>
      <a-form-item class="smart-query-form-item" label="创建时间">
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
        <a-button @click="operateAsset" v-privilege="'purchase:add'" type="primary" size="small">
          <template #icon>
            <PlusOutlined/>
          </template>
          新建
        </a-button>
      </div>
      <div class="smart-table-setting-block">
        <SmartTableOperator v-model="columns" :tableId="TABLE_ID_CONST.ASSET_PURCHASE" :refresh="queryData"/>
      </div>
    </a-row>
    <!---------- 表格操作行 end ----------->

    <!---------- 表格 begin ----------->
    <a-table
        :scroll="{x:1000}"
        size="small"
        :dataSource="tableData"
        :columns="columns"
        rowKey="assetId"
        bordered
        :loading="tableLoading"
        :pagination="false"
    >
      <template #bodyCell="{ text, record, column }">
        <template v-if="column.dataIndex === 'assetName' || column.dataIndex === 'assetNo'">
          <a @click="toDetail(record.assetId)">{{ record[column.dataIndex] }}</a>
        </template>
        <template v-if="column.dataIndex === 'sourceId'">
          {{ record.sourceId[0].valueName }}
        </template>
        <template v-if="column.dataIndex === 'status'">
          {{ $smartEnumPlugin.getDescByValue('ASSET_STATUS_ENUM', record.status) }}
        </template>
        <template v-if="column.dataIndex === 'action'">
          <div class="smart-table-operate">
            <a-button @click="showAssetModal(record)" type="link">明细</a-button>
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
import LocationSelect from '/@/components/fixed-asset/locaton-select/index.vue';
import DepartmentTreeSelect from '/@/components/department-tree-select/index.vue';
import CategoryTreeSelect from '/@/components/fixed-asset/category-tree-select/index.vue';
import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';
import SmartDictSelect from '/@/components/smart-dict-select/index.vue';
import AssetListModal from '/@/components/fixed-asset/asset-table-modal/asset-modal.vue'

import { TABLE_ID_CONST } from '/@/constants/table-id-const';
import { reactive, ref, onMounted } from 'vue';
import { assetPurchaseApi } from '/@/api/fixed-asset/asset/purchase-api';
import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import { useRouter } from 'vue-router';
import { defaultTimeRanges } from '/@/lib/default-time-ranges';
// ---------------------------- 表格列 ----------------------------

const columns = ref([
  {
    title: '编号',
    dataIndex: 'purchaseNo',
  },
  {
    title: '资产来源',
    dataIndex: 'sourceId',
  },
  {
    title: '备注',
    dataIndex: 'remark',
  },
  {
    title: '创建人',
    dataIndex: 'createUserName',
    width: 90
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
    width: 50
  },
]);

// ---------------------------- 查询数据表单和方法 ----------------------------

const queryFormState = {
  keywords: '',
  startTime: null,
  endTime: null,
  sourceId: null,
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
    let queryResult = await assetPurchaseApi.queryPage(queryForm);
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

// ---------------------------- 跳转 ----------------------------
let router = useRouter();

function operateAsset ({ assetId }) {
  router.push({
    path: '/fixed-asset/purchase-operate',
    query: {
      assetId
    }
  });
}

// ---------------------------- 展示采购详情 ----------------------------
const assetListRef = ref();
function showAssetModal ({ assetList }) {
  assetListRef.value.showModal(assetList)
}

</script>

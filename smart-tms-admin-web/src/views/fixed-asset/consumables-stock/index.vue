<template>
  <!---------- 查询表单form begin ----------->
  <a-form class="smart-query-form" v-privilege="'consumablesStock:query'">
    <a-row class="smart-query-form-row">
      <a-form-item label="关键字" class="smart-query-form-item">
        <a-input style="width: 150px" v-model:value="queryForm.keywords" placeholder="编号/名称"/>
      </a-form-item>
      <a-form-item label="所属分类" class="smart-query-form-item">
        <CategoryTreeSelect v-model:value="queryForm.categoryId" placeholder="请选择所属分类"
                            :categoryType="CATEGORY_TYPE_ENUM.CONSUMABLES.value"/>
      </a-form-item>
<!--      <a-form-item class="smart-query-form-item" label="创建时间">
        <a-range-picker v-model:value="createDateRange" :ranges="defaultTimeRanges" style="width: 100%"
                        @change="changeDate"/>
      </a-form-item>-->
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
        <a-button @click="operate" v-privilege="'consumablesStock:add'" type="primary" size="small">
          <template #icon>
            <PlusOutlined/>
          </template>
          新建
        </a-button>
      </div>
      <div class="smart-table-setting-block">
        <SmartTableOperator v-model="columns" :tableId="TABLE_ID_CONST.ASSET_CHECK" :refresh="queryData"/>
      </div>
    </a-row>
    <!---------- 表格操作行 end ----------->

    <!---------- 表格 begin ----------->
    <a-table
        :scroll="{ x: 1300 }"
        size="small"
        :dataSource="tableData"
        :columns="columns"
        rowKey="consumablesId"
        bordered
        :loading="tableLoading"
        :pagination="false"
    >
      <template #bodyCell="{ text, record, column }">
        <template v-if="column.dataIndex === 'consumablesNo' || column.dataIndex === 'consumablesName'">
          <a @click="toDetail(record.consumablesId)">{{ record[column.dataIndex] }}</a>
        </template>
        <template v-if="column.dataIndex === 'stockCount'">
          <template v-if="record.stockWarningValue > record.stockCount">
            <span style="color:red">{{ record.stockCount }}</span>
          </template>
          <template v-else>
            {{ record.stockCount }}
          </template>
        </template>
        <template v-if="column.dataIndex === 'action'">
          <a-button @click="operate(record)" size="small" type="link">编辑</a-button>
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
    <StockModal ref="operateModalRef" @reloadList="queryData"/>
  </a-card>
</template>
<script setup>
import SmartTableOperator from '/@/components/smart-table-operator/index.vue';
import StockModal from './components/stock-modal.vue';
import EnterpriseSelect from '/@/components/enterprise-select/index.vue';
import CategoryTreeSelect from '/@/components/fixed-asset/category-tree-select/index.vue';

import { TABLE_ID_CONST } from '/@/constants/table-id-const';
import { reactive, ref, onMounted } from 'vue';
import { consumablesStockApi } from '/@/api/fixed-asset/asset/consumables-stock-api';
import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import { useRouter } from 'vue-router';
import { defaultTimeRanges } from '/@/lib/default-time-ranges';
import { CATEGORY_TYPE_ENUM } from '/@/constants/business/category-const';
// ---------------------------- 表格列 ----------------------------

const columns = ref([
  {
    title: '耗材编号',
    dataIndex: 'consumablesNo',
  },
  {
    title: '所属分类',
    dataIndex: 'categoryName',
    ellipsis: true,
  },
  {
    title: '耗材名称',
    dataIndex: 'consumablesName',
    ellipsis: true,
  },
  {
    title: '所属公司',
    dataIndex: 'enterpriseName',
    ellipsis: true,
  },
  {
    title: '均价',
    dataIndex: 'averagePrice',
    width: 100
  },
  {
    title: '库存',
    dataIndex: 'stockCount',
    width: 60
  },
  {
    title: '库存成本',
    dataIndex: 'totalAmount',
    width: 100
  },
  {
    title: '预警库存',
    dataIndex: 'stockWarningValue',
    width: 80
  },
  {
    title: '备注',
    dataIndex: 'remark'
  },
  {
    title: '创建人',
    dataIndex: 'createUserName',
    width: 70
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
  }
]);

// ---------------------------- 查询数据表单和方法 ----------------------------

const queryFormState = {
  keywords: '',
  locationId: null,
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
    let queryResult = await consumablesStockApi.queryPage(queryForm);
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

const operateModalRef = ref();

function operate (record) {
  operateModalRef.value.showModal(record);
}

function toDetail (consumablesId) {
  router.push({
    path: '/fixed-asset/consumables-stock-detail',
    query: {
      consumablesId
    }
  });
}

// ---------------------------- 导出 ----------------------------
function exportExcel () {
  console.log('导出Excel');
}

</script>

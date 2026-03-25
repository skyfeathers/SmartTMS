<template>
  <!---------- 查询表单form begin ----------->
  <a-form class="smart-query-form" v-privilege="'depreciation:query'">
    <a-row class="smart-query-form-row">
      <a-form-item label="关键字" class="smart-query-form-item">
        <a-input style="width: 150px" v-model:value="queryForm.keywords" placeholder="资产编号/资产名称"/>
      </a-form-item>
      <a-form-item label="所属分类" class="smart-query-form-item">
        <CategoryTreeSelect v-model:value="queryForm.categoryId" placeholder="请选择所属分类"
                            :categoryType="CATEGORY_TYPE_ENUM.FIXED_ASSET.value"/>
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
      <div class="smart-table-setting-block">
      </div>
      <div>
        <SmartTableOperator v-model="columns" :tableId="TABLE_ID_CONST.ASSET_DEPRECIATION_REPORT" :refresh="queryData"/>
      </div>
    </a-row>
    <!---------- 表格操作行 end ----------->

    <!---------- 表格 begin ----------->
    <a-table
        :scroll="{x:1500}"
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
          {{record[column.dataIndex]}}
        </template>
        <template v-if="column.type === 'number'">
          ¥{{record[column.dataIndex]}}
        </template>
        <template v-if="column.dataIndex === 'residualValueRate'">
          5%
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


  </a-card>
</template>
<script setup>
import SmartTableOperator from '/@/components/smart-table-operator/index.vue';
import CategoryTreeSelect from '/@/components/fixed-asset/category-tree-select/index.vue';
import EnterpriseSelect from '/@/components/enterprise-select/index.vue';

import { TABLE_ID_CONST } from '/@/constants/table-id-const';
import { reactive, ref, onMounted } from 'vue';
import { reportApi } from '/@/api/fixed-asset/report/index';
import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import { CATEGORY_TYPE_ENUM } from '/@/constants/business/category-const';
// ---------------------------- 表格列 ----------------------------

const columns = ref([
  {
    title: '资产编号',
    dataIndex: 'assetNo',
    fixed: 'right'
  },
  {
    title: '所属分类',
    dataIndex: 'categoryName',
  },
  {
    title: '资产名称',
    dataIndex: 'assetName',
  },
  {
    title: '预计使用期限(月)',
    dataIndex: 'monthCount',
    width: 130
  },
  {
    title: '已使用期限（月）',
    dataIndex: 'usedMonthCount',
    width: 130
  },
  {
    title: '剩余使用期限（月）',
    dataIndex: 'surplusMonthCount',
    width: 150
  },
  {
    title: '原值',
    dataIndex: 'price',
    type: 'number',
    width: 90
  },
  {
    title: '累计折旧',
    dataIndex: 'totalDepreciation',
    type: 'number',
    width: 100
  },
  {
    title: '剩余净值',
    dataIndex: 'residualValue',
    type: 'number',
    width: 100
  },
  {
    title: '残值率',
    dataIndex: 'residualValueRate',
    width: 90
  },
]);

// ---------------------------- 查询数据表单和方法 ----------------------------

const queryFormState = {
  keywords:'',
  status:null,
  locationId: null,
  departmentId: null,
  categoryId: null,
  pageNum: 1,
  pageSize: 10,
};
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
  queryData();
}

// 查询数据
async function queryData () {
  tableLoading.value = true;
  try {
    let queryResult = await reportApi.queryPage(queryForm);
    tableData.value = queryResult.data.list;
    total.value = queryResult.data.total;
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}

onMounted(()=>{
  queryData();
});

</script>

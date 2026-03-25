<!--
  * 固定资产 - 盘点单
  *
  * @Author:    lidoudou
  * @Date:      2023-03-24 14:21:14
-->
<template>
  <!---------- 查询表单form begin ----------->
  <a-form class="smart-query-form" v-privilege="'check:query'">
    <a-row class="smart-query-form-row">
      <a-form-item label="关键字" class="smart-query-form-item">
        <a-input style="width: 150px" v-model:value="queryForm.keywords" placeholder="编号/创建人"/>
      </a-form-item>
      <a-form-item class="smart-query-form-item" label="盘点位置">
        <LocationSelect v-model:value="queryForm.locationId" placeholder="请选择盘点位置" style="width:200px"/>
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
        <a-button @click="operate" v-privilege="'check:add'" type="primary" size="small">
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
        :scroll="{ x: 1500 }"
        size="small"
        :dataSource="tableData"
        :columns="columns"
        rowKey="checkId"
        bordered
        :loading="tableLoading"
        :pagination="false"
    >
      <template #bodyCell="{ text, record, column }">
        <template v-if="column.dataIndex === 'checkNo' || column.dataIndex === 'checkName'">
          <a @click="toDetail(record.checkId)">{{ record[column.dataIndex] }}</a>
        </template>
        <template v-if="column.dataIndex === 'completeFlag'">
          {{ record.completeFlag ? '已完成' : '未完成' }}
        </template>
        <template v-if="column.dataIndex === 'checkType'">
          {{ $smartEnumPlugin.getDescByValue('ASSET_CHECK_TYPE_ENUM', record.checkType) }}
        </template>
        <template v-if="column.dataIndex === 'action'">
          <div class="smart-table-operate">
            <a-button @click="exportExcel" type="link">导出</a-button>
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
    <CheckModal ref="checkModalRef" @reloadList="queryData"/>
  </a-card>
</template>
<script setup>
import SmartTableOperator from '/@/components/smart-table-operator/index.vue';
import LocationSelect from '/@/components/fixed-asset/locaton-select/index.vue';
import CheckModal from './components/check-modal.vue';
import EnterpriseSelect from '/@/components/enterprise-select/index.vue';

import { TABLE_ID_CONST } from '/@/constants/table-id-const';
import { reactive, ref, onMounted } from 'vue';
import { assetCheckApi } from '/@/api/fixed-asset/asset/check-api';
import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import { useRouter } from 'vue-router';
import { defaultTimeRanges } from '/@/lib/default-time-ranges';
// ---------------------------- 表格列 ----------------------------

const columns = ref([
  {
    title: '盘点编号',
    dataIndex: 'checkNo',
  },
  {
    title: '盘点名称',
    dataIndex: 'checkName',
  },
  {
    title: '盘点类型',
    dataIndex: 'checkType',
    width: 100
  },
  {
    title: '盘点位置',
    dataIndex: 'locationName',
  },
  {
    title: '盘点状态',
    dataIndex: 'completeFlag',
    width: 100
  },
  {
    title: '完成时间',
    dataIndex: 'completeTime',
  },
  {
    title: '备注',
    dataIndex: 'remark',
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
  // {
  //   title: '操作',
  //   dataIndex: 'action',
  //   fixed: 'right',
  //   width: 130
  // },
]);

// ---------------------------- 查询数据表单和方法 ----------------------------

const queryFormState = {
  keywords: '',
  locationId: null,
  startTime: null,
  endTime: null,
  enterpriseId: null,
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
    let queryResult = await assetCheckApi.queryPage(queryForm);
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

const checkModalRef = ref();

function operate () {
  checkModalRef.value.showModal();
}

function toDetail (checkId) {
  router.push({
    path: '/fixed-asset/check-detail',
    query: {
      checkId
    }
  });
}

// ---------------------------- 导出 ----------------------------
function exportExcel () {
  console.log('导出Excel');
}

</script>

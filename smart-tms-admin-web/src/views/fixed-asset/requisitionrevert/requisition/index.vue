<!--
  * 固定资产 - 领用
  *
  * @Author:    lidoudou
  * @Date:      2023-03-21 10:45:14
-->
<template>
  <!---------- 查询表单form begin ----------->
  <a-form class="smart-query-form" v-privilege="'requisition:query'">
    <a-row class="smart-query-form-row">
      <a-form-item label="关键字" class="smart-query-form-item">
        <a-input style="width: 150px" v-model:value="queryForm.keywords" placeholder="编号/创建人"/>
      </a-form-item>
      <a-form-item class="smart-query-form-item" label="所属部门">
        <DepartmentTreeSelect ref="departmentTreeSelect" width="100%" :init="false" placeholder="请选择使用部门"
                              v-model:value="queryForm.departmentId" style="width:200px"/>
      </a-form-item>
      <a-form-item class="smart-query-form-item" label="领用时间">
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
        <a-button @click="operate" v-privilege="'requisition:add'" type="primary" size="small">
          <template #icon>
            <PlusOutlined/>
          </template>
          新建
        </a-button>
      </div>
      <div class="smart-table-setting-block">
        <SmartTableOperator v-model="columns" :tableId="TABLE_ID_CONST.ASSET_REQUISITION" :refresh="queryData"/>
      </div>
    </a-row>
    <!---------- 表格操作行 end ----------->

    <!---------- 表格 begin ----------->
    <a-table
        :scroll="{x:1400}"
        size="small"
        :dataSource="tableData"
        :columns="columns"
        rowKey="requisitionRevertId"
        bordered
        :loading="tableLoading"
        :pagination="false"
    >
      <template #bodyCell="{ text, record, column }">
        <template v-if="column.dataIndex === 'requisitionRevertNo'">
          <a @click="detail(record)">{{record.requisitionRevertNo}}</a>
        </template>
        <template v-if="column.dataIndex === 'status'">
          {{ $smartEnumPlugin.getDescByValue('REQUISITION_STATUS_ENUM', record.status) }}
        </template>
        <template v-if="column.dataIndex === 'action'">
          <div class="smart-table-operate">
            <a-button @click="showAssetModal(record)" type="link">明细</a-button>
            <template v-if="record.status == REQUISITION_STATUS_ENUM.WAIT.value">
              <a-button @click="confirmComplete(record)" v-privilege="'requisition:pass'" type="link">通过</a-button>
              <a-button @click="confirmReject(record)" v-privilege="'requisition:reject'" type="link" danger>驳回</a-button>
            </template>
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
import DepartmentTreeSelect from '/@/components/department-tree-select/index.vue';
import AssetListModal from '/@/components/fixed-asset/asset-table-modal/asset-modal.vue';
import EnterpriseSelect from '/@/components/enterprise-select/index.vue';

import { TABLE_ID_CONST } from '/@/constants/table-id-const';
import { reactive, ref, onMounted } from 'vue';
import { assetRequisitionApi } from '/@/api/fixed-asset/asset/requisition-api';
import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import { REQUISITION_STATUS_ENUM } from '/@/constants/fixed-asset/asset-const';
import { useRouter } from 'vue-router';
import { defaultTimeRanges } from '/@/lib/default-time-ranges';
import { Modal, message } from 'ant-design-vue';
import { SmartLoading } from '/@/components/smart-loading';
// ---------------------------- 表格列 ----------------------------

const columns = ref([
  {
    title: '领用编号',
    dataIndex: 'requisitionRevertNo',
    width: 150
  },
  {
    title: '领用日期',
    dataIndex: 'useTime',
  },
  {
    title: '领用说明',
    dataIndex: 'remark',
  },
  {
    title: '领用部门',
    dataIndex: 'departmentName',
  },
  {
    title: '领用人',
    dataIndex: 'userName',
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 80
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
    width: 130
  },
]);

// ---------------------------- 查询数据表单和方法 ----------------------------

const queryFormState = {
  keywords: '',
  departmentId: null,
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
    let queryResult = await assetRequisitionApi.queryPage(queryForm);
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
function confirmComplete ({ requisitionRevertId }) {
  Modal.confirm({
    title: '提示',
    content: '确认要通过领用申请吗',
    onOk: async () => {
      await requestComplete(requisitionRevertId);
    }
  });
}

async function requestComplete (requisitionRevertId) {
  try {
    SmartLoading.show();
    await assetRequisitionApi.completeRequisition(requisitionRevertId);
    message.success('通过成功');
    queryData();
  } catch (e) {
    console.log(e);
  } finally {
    SmartLoading.hide();
  }
}

function confirmReject ({ requisitionRevertId }) {
  Modal.confirm({
    title: '提示',
    content: '确认要驳回领用申请吗',
    onOk: async () => {
      await rejectRequisition(requisitionRevertId);
    }
  });
}

async function rejectRequisition (requisitionRevertId) {
  try {
    SmartLoading.show();
    await assetRequisitionApi.rejectRequisition(requisitionRevertId);
    message.success('通过成功');
    queryData();
  } catch (e) {
    console.log(e);
  } finally {
    SmartLoading.hide();
  }
}

// ---------------------------- 跳转 ----------------------------
let router = useRouter();

function operate ({ requisitionRevertId }) {
  router.push({
    path: '/fixed-asset/requisition-operate',
    query: {
      requisitionRevertId
    }
  });
}

function detail ({ requisitionRevertId }) {
  router.push({
    path: '/fixed-asset/requisition-detail',
    query: {
      requisitionRevertId
    }
  });
}

// ---------------------------- 展示资产详情 ----------------------------
const assetListRef = ref();

function showAssetModal ({ assetList }) {
  assetListRef.value.showModal(assetList);
}

</script>

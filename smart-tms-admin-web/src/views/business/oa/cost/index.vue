<template>
  <!---------- 查询表单form begin ----------->
  <a-form class="smart-query-form" v-privilege="'costApply:query'">
    <a-row class="smart-query-form-row">
      <a-form-item label="关键字" class="smart-query-form-item">
        <a-input style="width: 200" v-model:value="queryForm.keywords" placeholder="请输入申请编号" />
      </a-form-item>
      <a-form-item label="申请时间" class="smart-query-form-item">
        <a-range-picker v-model:value="queryForm.businessDate" :presets="defaultTimeRanges" style="width: 200" @change="onChangeBusinessDate" />
      </a-form-item>
      <a-form-item class="smart-query-form-item" label="部门">
        <DepartmentTreeSelect ref="departmentTreeSelect" :multiple="true" width="200px" :init="false" placeholder="请选择部门"
                              v-model:value="queryForm.departmentIdList" style="width:200px"/>
      </a-form-item>
      <a-form-item class="smart-query-form-item">
        <a-button type="primary" @click="queryData">
          <template #icon>
            <ReloadOutlined />
          </template>
          查询
        </a-button>
        <a-button @click="resetQuery" class="smart-margin-left10">
          <template #icon>
            <SearchOutlined />
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
        <a-button @click="operate" v-privilege="'costApply:add'" type="primary" size="small">
          <template #icon>
            <PlusOutlined />
          </template>
          新建
        </a-button>
        <a-button @click="batchPass" v-privilege="'costApply:pass'" type="primary" size="small">
          <template #icon>
            <check-outlined />
          </template>
          审核通过
        </a-button>
        <a-button @click="batchReject" v-privilege="'costApply:reject'" type="primary" danger size="small">
          <template #icon>
            <close-outlined />
          </template>
          审核拒绝
        </a-button>
      </div>
      <div class="smart-table-setting-block"></div>
    </a-row>
    <!---------- 表格操作行 end ----------->

    <!---------- 表格 begin ----------->
    <a-table
        size="small"
        :dataSource="tableData"
        :columns="columns"
        rowKey="applyId"
        :row-selection="{
          selectedRowKeys: selectedRowKeyList,
          onChange: onSelectChange ,
          getCheckboxProps: (record) => ({
              disabled: record.status != COST_APPLY_STATUS_ENUM.AUDIT.value,
            })
        }"
        bordered
        :loading="tableLoading"
        :pagination="false"
    >
      <template #bodyCell="{ text, record, column }">
        <template v-if="column.dataIndex === 'applyNo'">
          <a @click="showDetail(record)" >{{ record.applyNo }}</a><SmartCopyIcon :value="text" />
        </template>
        <template v-if="column.dataIndex === 'status'">
          {{ $smartEnumPlugin.getDescByValue('COST_APPLY_STATUS_ENUM', record.status) }}
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

    <!---------- 详情 ----------->
    <CostApplyDetail ref="detailRef"/>
  </a-card>
</template>
<script setup>
import { reactive, ref, onMounted } from 'vue';
import { message, Modal } from 'ant-design-vue';
import { SmartLoading } from '/@/components/smart-loading';
import { smartSentry } from '/@/lib/smart-sentry';
import TableOperator from '/@/components/smart-table-operator/index.vue';
import { defaultTimeRanges } from '/@/lib/default-time-ranges';
import CostApplyDetail from './cost-apply-detail.vue';
import { COST_APPLY_STATUS_ENUM } from '/@/constants/business/cost-apply-const';
import DepartmentTreeSelect from '/@/components/department-tree-select/index.vue';
import { costApplyApi } from '/@/api/business/oa/cost-apply-api';
import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import { useRouter } from 'vue-router';
// ---------------------------- 表格列 ----------------------------

const columns = ref([
  {
    title: '申请编号',
    dataIndex: 'applyNo',
    width: 200,
  },
  {
    title: '状态',
    dataIndex: 'status',
  },
  {
    title: '所在部门',
    dataIndex: 'departmentName',
  },
  {
    title: '申请人',
    dataIndex: 'applyUserName',
    ellipsis: true,
  },
  {
    title: '申请说明',
    dataIndex: 'remark',
    ellipsis: true,
  },
  {
    title: '申请日期',
    dataIndex: 'applyDate',
    ellipsis: true,
  },
  {
    title: '申请金额',
    dataIndex: 'totalAmount',
    ellipsis: true,
  },
  {
    title: '创建时间',
    width: 150,
    dataIndex: 'createTime',
    ellipsis: true,
  },
]);

// ---------------------------- 查询数据表单和方法 ----------------------------

const queryFormState = {
  keywords: undefined, //关键字
  startTime: undefined, //申请时间 开始
  endTime: undefined, //申请时间 结束
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
function resetQuery() {
  let pageSize = queryForm.pageSize;
  Object.assign(queryForm, queryFormState);
  queryForm.pageSize = pageSize;
  queryData();
}

// 查询数据
async function queryData () {
  selectedRowKeyList.value = [];
  selectedDataList.value = [];
  tableLoading.value = true;
  try {
    let result = await costApplyApi.queryPage(queryForm);
    tableData.value = result.data.list;
    total.value = result.data.total;
  } catch (e) {
    smartSentry.captureError(e);
  } finally {
    tableLoading.value = false;
  }
}

function onChangeBusinessDate(dates, dateStrings) {
  queryForm.businessDateBegin = dateStrings[0];
  queryForm.businessDateEnd = dateStrings[1];
}

onMounted(queryData);

// ---------------------------- 批量操作 ----------------------------

const selectedRowKeyList = ref([]);
const selectedDataList = ref([]);

function onSelectChange(keyArray, dataArray) {
  selectedRowKeyList.value = keyArray;
  selectedDataList.value = dataArray;
}

// ---------------------------- 审核通过 ----------------------------
function batchPass() {
  if (selectedDataList.value.length < 1) {
    message.warning('请选择申请单据哦');
    return;
  }

  Modal.confirm({
    title: `进行审核`,
    closable: true,
    okText: '确认审核通过吗？',
    okType: 'primary',
    onOk: async () => {
      try {
        SmartLoading.show();
        await costApplyApi.pass(selectedRowKeyList.value);
        message.success('操作成功');
        queryData();
      } catch (e) {
        smartSentry.captureError(e);
      } finally {
        SmartLoading.hide();
      }
    },
    cancelText: '取消',
    cancelType: 'warning',
  });
}
// ---------------------------- 审核驳回 ----------------------------
function batchReject() {
  if (selectedDataList.value.length < 1) {
    message.warning('请选择申请单据哦');
    return;
  }

  Modal.confirm({
    title: `确认审核驳回吗？`,
    okText: '审核驳回',
    okType: 'primary',
    onOk: async () => {
      try {
        SmartLoading.show();
        await costApplyApi.reject(selectedRowKeyList.value);
        message.success('操作成功');
        queryData();
      } catch (e) {
        smartSentry.captureError(e);
      } finally {
        SmartLoading.hide();
      }
    },
    cancelText: '取消',
  });
}

// ---------------------------- 详情 ----------------------------
const detailRef = ref();

function showDetail(data) {
  detailRef.value.show(data.applyId);
}

// ---------------------------- 新建 ----------------------------
let router = useRouter();

function operate ({ transferId }) {
  router.push({
    path: '/oa/cost-apply/operate',
    query: {
      transferId
    }
  });
}
</script>

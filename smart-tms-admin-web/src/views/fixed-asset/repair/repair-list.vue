<!--
  * 固定资产-维修登记
  *
  * @Author:    卓大
  * @Date:      2023-03-23 15:01:51
  * @Copyright  1024创新实验室 （ https://1024lab.net ）
-->
<template>
  <!---------- 查询表单form begin ----------->
  <a-form class="smart-query-form" v-privilege="'repair:query'">
    <a-row class="smart-query-form-row">
      <a-form-item label="关键字" class="smart-query-form-item">
        <a-input style="width: 200" v-model:value="queryForm.keywords" placeholder="请输入领用人/单号" />
      </a-form-item>
      <a-form-item label="业务日期" class="smart-query-form-item">
        <a-range-picker v-model:value="queryForm.businessDate" :ranges="defaultTimeRanges" style="width: 200" @change="onChangeBusinessDate" />
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
        <a-button @click="showForm" v-privilege="'repair:add'" type="primary" size="small">
          <template #icon>
            <PlusOutlined />
          </template>
          新建维修登记
        </a-button>
        <a-button @click="batchPass" v-privilege="'repair:pass'" type="primary" size="small">
          <template #icon>
            <check-outlined />
          </template>
          审核通过
        </a-button>
        <a-button @click="batchReject" v-privilege="'repair:reject'" type="primary" danger size="small">
          <template #icon>
            <close-outlined />
          </template>
          审核拒绝
        </a-button>
        <a-button @click="batchFinish" v-privilege="'repair:finish'" type="primary" size="small">
          <template #icon>
            <tool-outlined />
          </template>
          完成维修
        </a-button>
      </div>
      <div class="smart-table-setting-block"></div>
      <div>
        <SmartTableOperator v-model="columns" :tableId="TABLE_ID_CONST.ASSET_REPAIR" :refresh="ajaxQuery" />
      </div>
    </a-row>
    <!---------- 表格操作行 end ----------->

    <!---------- 表格 begin ----------->
    <a-table
      :scroll="{x:1400}"
      size="small"
      :dataSource="tableData"
      :columns="columns"
      rowKey="repairId"
      :row-selection="{ selectedRowKeys: selectedRowKeyList, onChange: onSelectChange }"
      bordered
      :loading="tableLoading"
      :pagination="false"
    >
      <template #bodyCell="{ text, record, column }">
        <template v-if="column.dataIndex === 'repairCode'">
          <a @click="showDetail(record)" href="javascript:void(0)">{{ record.repairCode }}</a>
        </template>
        <template v-if="column.dataIndex === 'status'">
          {{ $smartEnumPlugin.getDescByValue('ASSET_REPAIR_STATUS_ENUM', record.status) }}
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

    <!---------- 表单 ----------->
    <RepairForm ref="formRef" @reloadList="queryData" />

    <!---------- 详情 ----------->
    <RepairDetail ref="detailRef" @reloadList="queryData" />
  </a-card>
</template>
<script setup>
import { reactive, ref, onMounted } from 'vue';
import { message, Modal } from 'ant-design-vue';
import { SmartLoading } from '/@/components/smart-loading';
import { repairApi } from '/@/api/fixed-asset/asset/repair-api';
import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import { smartSentry } from '/@/lib/smart-sentry';
import SmartTableOperator from '/@/components/smart-table-operator/index.vue';
import { defaultTimeRanges } from '/@/lib/default-time-ranges';
import RepairForm from './repair-form.vue';
import RepairDetail from './repair-detail.vue';
import { ASSET_REPAIR_STATUS_ENUM } from '/@/constants/fixed-asset/repair-const';
import DepartmentTreeSelect from '/@/components/department-tree-select/index.vue';
import EnterpriseSelect from '/@/components/enterprise-select/index.vue';
import { TABLE_ID_CONST } from '/@/constants/table-id-const';

// ---------------------------- 表格列 ----------------------------

const columns = ref([
  {
    title: '维修单号',
    dataIndex: 'repairCode',
    width: 160,
  },
  {
    title: '维修厂家',
    dataIndex: 'repairCompanyName',
    ellipsis: true,
  },
  {
    title: '单据状态',
    dataIndex: 'status',
    ellipsis: true,
  },
  {
    title: '业务日期',
    dataIndex: 'businessDate',
    ellipsis: true,
  },
  {
    title: '维修内容',
    dataIndex: 'content',
    ellipsis: true,
  },
  {
    title: '申请维修人员',
    dataIndex: 'applyUserName',
    ellipsis: true,
  },
  {
    title: '维修花费',
    dataIndex: 'repairCost',
    ellipsis: true,
  },
  {
    title: '备注',
    dataIndex: 'remark',
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
  businessDate: [], //业务日期
  departmentIdList:[],//部门集合
  businessDateBegin: undefined, //业务日期 开始
  businessDateEnd: undefined, //业务日期 结束
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
async function queryData() {
  tableLoading.value = true;
  try {
    let queryResult = await repairApi.queryPage(queryForm);
    queryResult.data.list.forEach((e) => {
      if (e.repairCompany && e.repairCompany.length > 0) {
        e.repairCompanyName = e.repairCompany[0].valueName;
      }
    });

    tableData.value = queryResult.data.list;
    total.value = queryResult.data.total;
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
    message.warning('请选择维修登记哦');
    return;
  }

  for (const iterator of selectedDataList.value) {
    if (iterator.status !== ASSET_REPAIR_STATUS_ENUM.AUDIT.value) {
      message.error('单号【' + iterator.repairCode + '】不是待审核状态，' + '请选择待审核的维修单');
      return;
    }
  }

  Modal.confirm({
    title: `进行审核`,
    closable: true,
    okText: '确认审核通过吗？',
    okType: 'primary',
    onOk: async () => {
      try {
        SmartLoading.show();
        await repairApi.pass(selectedRowKeyList.value);
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
    message.warning('请选择维修登记哦');
    return;
  }

  for (const iterator of selectedDataList.value) {
    if (iterator.status !== ASSET_REPAIR_STATUS_ENUM.AUDIT.value) {
      message.error('单号【' + iterator.repairCode + '】不是待审核状态，' + '请选择待审核的维修单');
      return;
    }
  }

  Modal.confirm({
    title: `确认审核驳回吗？`,
    okText: '审核驳回',
    okType: 'primary',
    onOk: async () => {
      try {
        SmartLoading.show();
        await repairApi.reject(selectedRowKeyList.value);
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

// ---------------------------- 完成维修 ----------------------------
function batchFinish() {
  if (selectedDataList.value.length < 1) {
    message.warning('请选择维修登记哦');
    return;
  }

  for (const iterator of selectedDataList.value) {
    if (iterator.status !== ASSET_REPAIR_STATUS_ENUM.REPAIRING.value) {
      message.error('单号【' + iterator.repairCode + '】不是维修中，' + '请选择维修中的单据');
      return;
    }
  }

  Modal.confirm({
    title: `确认完成维修吗？`,
    okText: '维修完成',
    okType: 'primary',
    onOk: async () => {
      try {
        SmartLoading.show();
        await repairApi.finish(selectedRowKeyList.value);
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

// ---------------------------- 添加/修改 ----------------------------
const formRef = ref();

function showForm(data) {
  formRef.value.show(data);
}

// ---------------------------- 详情 ----------------------------
const detailRef = ref();

function showDetail(data) {
  detailRef.value.show(data.repairId);
}
</script>

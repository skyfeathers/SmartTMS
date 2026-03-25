<!--
 * @Description: file content
 * @Author: yandy
 * @Date: 2022-07-13 14:40:01
 * @LastEditors:
 * @LastEditTime: 2022-07-13 14:40:01
-->
<template>
  <a-form class="smart-query-form" v-privilege="'audit:query'">
    <a-row class="smart-query-form-row">
      <a-form-item label="关键字" class="smart-query-form-item">
        <a-input style="width: 320px" v-model:value.trim="queryForm.searchWord" placeholder="申请备注/申请人/审核单号/客商名称/司机信息"/>
      </a-form-item>

      <a-form-item label="类型" class="smart-query-form-item">
        <smart-enum-select v-model:value="queryForm.flowType" placeholder="请选择类型"
                           enum-name="FLOW_TYPE_ENUM"/>
      </a-form-item>

      <a-form-item label="创建时间">
        <a-range-picker
            v-model:value="createDateRange"
            :presets="defaultTimeRanges"
            style="width: 100%"
            @change="changeDate"
        />
      </a-form-item>

      <a-form-item label="审核状态" class="smart-query-form-item" v-if="!handleFlag">
        <a-radio-group v-model:value="queryForm.auditStatus" @change="search">
          <a-radio-button :value="undefined">全部</a-radio-button>
          <a-radio-button :value="FLOW_AUDIT_STATUS_ENUM.PASS.value">审核通过</a-radio-button>
          <a-radio-button :value="FLOW_AUDIT_STATUS_ENUM.PROCESSING.value">审核中</a-radio-button>
          <a-radio-button :value="FLOW_AUDIT_STATUS_ENUM.CANCEL.value">已撤销</a-radio-button>
          <a-radio-button :value="FLOW_AUDIT_STATUS_ENUM.REJECT.value">审核驳回</a-radio-button>
        </a-radio-group>
      </a-form-item>

      <a-form-item class="smart-query-form-item smart-margin-left10">
        <a-button type="primary" @click="search">
          <template #icon>
            <SearchOutlined/>
          </template>
          查询
        </a-button>
        <a-button @click="resetQuery">
          <template #icon>
            <ReloadOutlined/>
          </template>
          重置
        </a-button>

        <a-button v-if="handleFlag" v-privilege="'audit:batch'" type="primary" @click="batchAudit"  class="smart-margin-left10">
          批量审核
        </a-button>
      </a-form-item>
    </a-row>
  </a-form>

  <a-alert v-if="handleFlag && $privilege('audit:batch')" message="批量审核请谨慎操作！如若操作失败，请仔细查看返回信息，再对对应单据进行单独审核。" type="warning" show-icon class="smart-margin-bottom5"/>

  <a-table
      :scroll="{ x: '1400px', y: '600px' }"
      size="small"
      :dataSource="tableData"
      :columns="columns"
      rowKey="instanceId"
      :row-selection="{ selectedRowKeys: selectedRowKeyList, onChange: onSelectChange }"
      :pagination="false"
      bordered
  >
    <template #bodyCell="{ text, record, index, column }">
      <!-- 审核类型列 -->
      <template v-if="column.dataIndex === 'flowType'">
        <span>{{ $smartEnumPlugin.getDescByValue('FLOW_TYPE_ENUM', text) }}</span>
      </template>
      <!-- 审核单号列 -->
      <template v-else-if="column.dataIndex === 'businessCode'">
        <a @click="toOrderDetail(record)">{{ text }}</a>
      </template>
      <!-- 摘要列 -->
      <template v-else-if="column.dataIndex === 'extendInfo'">
        <div class="flow-extend">
          <p v-if="record.extendField1">{{ record.extendFieldName1 }}：{{ record.extendField1 }}</p>
          <p v-if="record.extendField2">{{ record.extendFieldName2 }}：{{ record.extendField2 }}</p>
          <p v-if="record.extendField3">{{ record.extendFieldName3 }}：{{ record.extendField3 }}</p>
          <p v-if="record.extendField4">{{ record.extendFieldName4 }}：{{ record.extendField4 }}</p>
          <p v-if="record.extendField5">{{ record.extendFieldName5 }}：{{ record.extendField5 }}</p>
          <p v-if="record.extendField6">{{ record.extendFieldName6 }}：{{ record.extendField6 }}</p>
          <p v-if="record.extendField7">{{ record.extendFieldName7 }}：{{ record.extendField7 }}</p>
          <p v-if="record.extendField8">{{ record.extendFieldName8 }}：{{ record.extendField8 }}</p>
          <p v-if="record.extendField9">{{ record.extendFieldName9 }}：{{ record.extendField9 }}</p>
          <p v-if="record.extendField10">{{ record.extendFieldName10 }}：{{ record.extendField10 }}</p>
        </div>
      </template>
      <!-- 审核状态列 -->
      <template v-else-if="column.dataIndex === 'auditStatus'">
        <span>{{ $smartEnumPlugin.getDescByValue('FLOW_AUDIT_STATUS_ENUM', record.auditStatus) }}</span>
      </template>
      <template v-else-if="column.dataIndex === 'action'">
        <a-button class="table-btn" @click="showCommentModal(record)" v-privilege="'audit:comment'" type="link">评论</a-button>
        <a-button class="table-btn" @click="showAuditModal(record)" v-privilege="'audit:audit'" v-if="handleFlag" type="link">审核</a-button>
        <a-button class="table-btn" @click="toOrderDetail(record)" v-privilege="'audit:detail'" type="link">详情</a-button>
        <!-- 我发起的 & 审核状态==审核中 -->
        <a-button class="table-btn" @click="confirmCancel(record.instanceId)" v-privilege="'audit:revoke'" v-if="initiateFlag && record.auditStatus == FLOW_AUDIT_STATUS_ENUM.PROCESSING.value" type="link">撤销</a-button>
      </template>
    </template>

  </a-table>

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
        @change="queryList"
        @showSizeChange="queryList"
        :show-total="(total) => `共${total}条`"
    />
  </div>
  <!-- 审核 -->
  <flow-audit-modal ref="flowAuditRef" @refresh="auditHandleFinish"/>
  <!-- 评论 -->
  <flow-comment-modal ref="flowCommentRef"/>
</template>
<script setup>
import { reactive, ref, computed } from 'vue';
import { message, Modal } from 'ant-design-vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import SmartEnumSelect from '/@/components/smart-enum-select//index.vue';
import { flowApi } from '/@/api/business/flow/flow-api';
import { AUDIT_STATUS_ENUM, PAGE_SIZE, PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import {
  FLOW_INSTANCE_QUERY_TYPE_ENUM,
  FLOW_AUDIT_STATUS_ENUM,
  FLOW_TYPE_ENUM
} from '/@/constants/business/flow-const';
import FlowAuditModal from '/@/components/flow-audit/index.vue';
import FlowCommentModal from '/@/components/flow-comment/index.vue';
import { useRouter } from 'vue-router';
import { defaultTimeRanges } from '/@/lib/default-time-ranges';

// ------------- 定义props -------
let props = defineProps({
  queryType: {
    type: Number,
    require: true
  }
})

// ------------- 定义计算属性 start -------
// 待我处理的
const handleFlag = computed(() => {
  return props.queryType == FLOW_INSTANCE_QUERY_TYPE_ENUM.MY_HANDLE.value;
});

// 我收到的
const receivedFlag = computed(() => {
  return props.queryType == FLOW_INSTANCE_QUERY_TYPE_ENUM.MY_RECEIVED.value;
});

// 我发起的
const initiateFlag = computed(() => {
  return props.queryType == FLOW_INSTANCE_QUERY_TYPE_ENUM.MY_INITIATE.value;
});

// ------------- 定义计算属性 end -------

const columns = reactive([
  {
    title: '审核类型',
    dataIndex: 'flowType',
    width: 120,
  },
  {
    title: '审核单号',
    dataIndex: 'businessCode',
    width: 144,
  },
  {
    title: '摘要',
    dataIndex: 'extendInfo',
    width: 300,
  },
  {
    title: '申请备注',
    dataIndex: 'remark',
    width: 100,
  },
  {
    title: '申请人',
    dataIndex: 'initiatorName',
    width: 80,
  },
  {
    title: '审核状态',
    dataIndex: 'auditStatus',
    width: 80,
  },
  {
    title: '当前处理人',
    dataIndex: 'currentHandlerNames',
    width: 120,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 140,
  },
  {
    title: '完成时间',
    dataIndex: 'finishTime',
    width: 140,
  },
  {
    title: '操作',
    dataIndex: 'action',
    fixed: 'right',
    width: 125,
  },
]);

const queryFormState = {
  queryType: undefined,
  searchWord: '',
  flowType: undefined,
  auditStatus: undefined,
  auditStatusList: [],
  pageNum: 1,
  pageSize: PAGE_SIZE,
  searchCount: true,
  submitStartDate: null,
  submitEndDate: null
};
const queryForm = reactive({...queryFormState});
const tableLoading = ref(false);
const tableData = ref([]);
const total = ref(0);
const createDateRange = ref([]);

function search () {
  queryForm.pageNum = 1;
  queryList();
}

async function queryList () {
  try {
    queryForm.queryType = props.queryType;
    if (queryForm.auditStatus) {
      queryForm.auditStatusList = [queryForm.auditStatus];
    } else {
      queryForm.auditStatusList = [];
    }
    tableLoading.value = true;
    let result = await flowApi.queryFlowList(queryForm);
    const list = result.data.list;
    total.value = result.data.total;
    tableData.value = list;
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
};

function resetQuery() {
  Object.assign(queryForm, queryFormState);
  queryForm.queryType = props.queryType;
  createDateRange.value = [];
  queryList();
}

function changeDate (dates, dateStrings) {
  queryForm.submitStartDate = dateStrings[0];
  queryForm.submitEndDate = dateStrings[1];
}

const selectedRowKeyList = ref([]);

function onSelectChange (selectedRowKeys) {
  selectedRowKeyList.value = selectedRowKeys;
}

function clearSelected () {
  selectedRowKeyList.value = [];
}


// ------------- 审核 -------
const flowAuditRef = ref();
function showAuditModal(record) {
  const flowId = record.instanceId;
  flowAuditRef.value.showModal(flowId);
}

function batchAudit () {
  if (selectedRowKeyList.value.length == 0) {
    message.error('请至少选择一个审批单据');
    return;
  }
  flowAuditRef.value.showBatchModal(selectedRowKeyList.value);
}

function auditHandleFinish () {
  clearSelected();
  queryList();
}

// ------------- 评论 -------
const flowCommentRef = ref();
function showCommentModal(record) {
  flowCommentRef.value.showModal(record.instanceId, record.currentTaskId);
}

// ------------- 撤销 -------
function confirmCancel(instanceId) {
  Modal.confirm({
    title: '撤销',
    content: '该单据正在审核中，您还要继续吗？',
    okText: '继续',
    okType: 'danger',
    onOk() {
      cancel(instanceId);
    },
    cancelText: '取消',
    onCancel() {
    },
  });
}

async function cancel(instanceId) {
  try {
    useSpinStore().show();
    await flowApi.cancelFlow(instanceId);
    message.success('撤销成功');
    queryList();
  } catch (e) {
    console.log(e);
  } finally {
    useSpinStore().hide();
  }
}
// ------------- 跳转到单据详情 -------
const router = useRouter();
function toOrderDetail(record) {
  console.log(record);
  // 运单审核
  if (record.flowType == FLOW_TYPE_ENUM.WAY_BILL_AUDIT.value) {
    router.push({
      path: '/waybill/waybill-detail',
      query: {
        waybillId: record.businessId,
      },
    });
    return;
  }
  // 付款审核
  if (record.flowType == FLOW_TYPE_ENUM.PAY_AUDIT.value) {
    router.push({
      path: '/pay/pay-order-detail',
      query: {
        payOrderId: record.businessId,
      },
    });
    return;
  }
  // 收款审核
  if (record.flowType == FLOW_TYPE_ENUM.RECEIVE_AUDIT.value) {
    router.push({
      path: '/receive-order/detail',
      query: {
        receiveOrderId: record.businessId,
      },
    });
    return;
  }
  // 油卡充值审批
  if (record.flowType == FLOW_TYPE_ENUM.OIL_CARD_AUDIT.value) {
    router.push({
      path: '/oil-card/recharge',
      query: {
        rechargeApplyId: record.businessId,
      },
    });
    return;
  }
}

// ------------- 定义 -------
defineExpose({
  resetQuery,
  queryList,
  queryForm,
});
</script>
<style scoped lang="less">
.flow-extend > p {
  margin-bottom: 0;
  line-height: 18px;
  font-size: 12px;
}

.table-btn {
  padding: 4px 8px;
}

</style>

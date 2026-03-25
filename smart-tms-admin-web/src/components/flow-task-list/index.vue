<!--
 * @Description: file content
 * @Author: yandy
 * @Date: 2022-07-13 15:25:13
 * @LastEditors: zhuoda
 * @LastEditTime: 2022-08-02
-->
<template>
  <a-card
    v-if="taskList.length > 0"
    :bordered="false"
    class="smart-margin-top10"
    size="small"
    :title="$smartEnumPlugin.getDescByValue('FLOW_TYPE_ENUM', props.flowType)"
  >
    <div>
      <slot name="content"></slot>
      <a-timeline class="smart-margin-top10">
        <a-timeline-item v-for="(item, index) in taskList" :key="index" :color="item.handleFlag ? 'blue' : 'gray'">
          <p>
            <span
              >{{ item.auditRecordHandlerNames || item.handlerNames }}
              <template v-if="item.taskType == FLOW_TASK_TYPE_ENUM.APPROVE.value">
                <a-tag :color="$smartEnumPlugin.getFieldByValue('FLOW_AUDIT_STATUS_ENUM', item.auditStatus, 'color')">
                  （{{ $smartEnumPlugin.getDescByValue('FLOW_AUDIT_STATUS_ENUM', item.auditStatus) }}）
                </a-tag>
              </template>
              <template v-else>
                <a-tag :color="$smartEnumPlugin.getFieldByValue('FLOW_TASK_TYPE_ENUM', item.taskType, 'color')">
                  （{{ $smartEnumPlugin.getDescByValue('FLOW_TASK_TYPE_ENUM', item.taskType) }}）
                </a-tag>
              </template>
            </span>
            <span class="smart-margin-left20">{{ item.finishTime }}</span>
          </p>
          <p v-if="item.taskType == FLOW_TASK_TYPE_ENUM.APPROVE.value">审核备注：{{ item.auditRemark }}</p>
          <!-- 评论 -->
          <task-comment v-if="!$lodash.isEmpty(item.commentList)" :commentList="item.commentList" />
        </a-timeline-item>
      </a-timeline>

      <div class="audit-box">
        <a-button class="smart-margin-left20" @click="showCommentModal()" type="primary" ghost>评论</a-button>
        <a-button class="smart-margin-left20" v-if="canAuditFlag" @click="showAuditModal" type="primary">审核</a-button>
        <a-button v-if="canCancelFlag" @click="confirmCancel" type="primary" class="smart-margin-left20">撤销</a-button>
      </div>
    </div>
  </a-card>
  <!-- 审核弹窗 -->
  <FlowAuditModal ref="flowAuditRef" @refresh="init(props.instanceId)" />
  <FlowCommentModal ref="flowCommentRef" @finish="completeComment" />
</template>
<script setup>
import { watch, ref, computed } from 'vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import { useUserStore } from '/@/store/modules/system/user';
import { flowApi } from '/@/api/business/flow/flow-api';
import { message, Modal } from 'ant-design-vue';
import { FLOW_TASK_TYPE_ENUM } from '/@/constants/business/flow-const';
import { AUDIT_STATUS_ENUM } from '/@/constants/common-const';
import { useRoute, useRouter } from 'vue-router';
import FlowAuditModal from '/@/components/flow-audit/index.vue';
import FlowCommentModal from '/@/components/flow-comment/index.vue';
import TaskComment from './task-comment.vue';
import _ from 'lodash';
import emitter from '/@/views/business/pay/pay-order-detail';

// ========= 定义 props ===============
const props = defineProps({
  instanceId: Number,
  processingFlag: Boolean,
  flowType: Number,
});
// ========= 定义 watch 监听 ===============
watch(
  () => props.instanceId,
  (e) => {
    if (e) {
      init(e, false);
    }
  },
  { immediate: true }
);
// 触发页面刷新
const emit = defineEmits(['refresh','setTaskList']);

function init(instanceId, refreshFlag = true) {
  queryTaskList(instanceId);
  getHandlerList(instanceId);
  if (refreshFlag) {
    emit('refresh');
  }
}

emitter.on('updateExtraData', updateExtraData);
let extraData = null;

function updateExtraData (data) {
  extraData = data;
}

// 任务列表
const taskList = ref([]);

// 查询审核流程列表
async function queryTaskList(instanceId) {
  try {
    useSpinStore().show();
    let result = await flowApi.getFlowTaskList(instanceId);
    taskList.value = result.data;
  } catch (e) {
    console.log(e);
  } finally {
    useSpinStore().hide();
  }
}

// 是否可以审核
const canAuditFlag = ref(false);

// 流程处理人
async function getHandlerList(instanceId) {
  try {
    useSpinStore().show();
    let result = await flowApi.handlerFlowTask(instanceId);
    let handlerIdList = result.data;
    canAuditFlag.value = handlerIdList.includes(useUserStore().employeeId);
  } catch (e) {
    console.log(e);
  } finally {
    useSpinStore().hide();
  }
}

// 是否可以撤销
const canCancelFlag = computed(() => {
  // 如果存在待审核的节点，则可以撤销
  let auditList = taskList.value.filter((e) => e.taskType == FLOW_TASK_TYPE_ENUM.APPROVE.value);
  if (_.isEmpty(auditList.filter(e => e.auditStatus == AUDIT_STATUS_ENUM.WAIT_AUDIT.value))) {
    return false;
  }
  //开始节点发起人
  let find = taskList.value.find((e) => e.taskType == FLOW_TASK_TYPE_ENUM.START.value);
  if (find) {
    let handlerUserIds = find.handlers?.split(',');
    return handlerUserIds?.map((e) => Number(e)).includes(useUserStore().employeeId) && props.processingFlag;
  }
  return false;
});

// 审核
const flowAuditRef = ref();

function showAuditModal() {
  flowAuditRef.value.showModal(props.instanceId, extraData);
}

// 撤销
function confirmCancel() {
  Modal.confirm({
    title: '撤销',
    content: '该单据正在审核中，您还要继续吗？',
    okText: '继续',
    okType: 'danger',
    onOk() {
      cancel();
    },
    cancelText: '取消',
    onCancel() {},
  });
}

async function cancel() {
  try {
    useSpinStore().show();
    await flowApi.cancelFlow(props.instanceId);
    message.success('撤销成功');
    init(props.instanceId);
  } catch (e) {
    console.log(e);
  } finally {
    useSpinStore().hide();
  }
}

// 评论
const flowCommentRef = ref();

function showCommentModal() {
  let noHandleTaskList = taskList.value?.filter((item) => !item.handleFlag);
  let currentTaskId = taskList.value[taskList.value.length - 1].taskId;
  if (!_.isEmpty(noHandleTaskList)) {
    currentTaskId = noHandleTaskList[0].taskId;
  }
  flowCommentRef.value.showModal(props.instanceId, currentTaskId);
}

// 评论完成 处理任务刷新
function completeComment() {
  init(props.instanceId);
}


defineExpose({
  canAuditFlag
})
// 返回
let route = useRoute();
let router = useRouter();

</script>
<style lang="less" scoped>
.audit-box {
  width: 100%;
  border-top: 1px solid #e9e9e9;
  padding: 10px 16px;
  background: #fff;
  text-align: right;
  z-index: 1;
}

.commentHeader {
  font-weight: 500;
  font-size: 16px;
}

:deep(.ant-card-head-title) {
  &::before {
    content: '';
    position: absolute;
    top: 3px;
    left: 0;
    width: 3px;
    height: 30px;
    background-color: @primary-color;
  }
}
</style>

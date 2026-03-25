<template>
  <div>
    <a-drawer title="流程配置（配置完成后，请点击【保存】以防数据丢失）" v-model:open="visible" width="50%" @close="onClose">
      <a-tabs v-model:activeKey="activeKey"  @change="changeTab">
        <a-tab-pane :key="item.flowId" :tab="item.flowName" v-for="(item, index) in flowList">
        </a-tab-pane>
      </a-tabs>
      <div v-if="flowConfig && !$lodash.isEmpty(flowConfig.taskList)">
        <a-steps direction="vertical">
          <a-step status="process"  v-for="(item, index) in flowConfig.taskList" :key="index">

            <template v-slot:title>
              ({{$smartEnumPlugin.getDescByValue('FLOW_TASK_TYPE_ENUM', item.taskType)}}){{item.taskName}}
              <a-button v-if="!item.editFlag" type="link" @click="taskEdit(item)">编辑</a-button>
              <a-button v-if="item.editFlag" type="link" @click="taskCancelEdit(item)">取消编辑</a-button>
              <a-button v-if="index > 0" type="link" danger @click="taskDel(index)">删除</a-button>
              <a-button v-if="index >= 1" type="link" @click="addTask(item.taskType, item.taskName, index)">添加{{ $smartEnumPlugin.getDescByValue('FLOW_TASK_TYPE_ENUM', item.taskType) }}节点</a-button>
              <a-button v-if="index >= 2" type="link" @click="taskMoveUp(index)">上移</a-button>
              <a-button v-if="index >= 1 && index < flowConfig.taskList.length - 1" type="link" @click="taskMoveDown(index)">下移</a-button>
            </template>
            <template v-slot:subTitle>

            </template>
            <template v-slot:description>
              <div v-if="item.editFlag" class="task-config">
                <a-form-item label="任务名称" required>
                  <a-input v-model:value="item.taskName" placeholder="请输入任务名称"/>
                </a-form-item>
                <a-form-item label="任务说明">
                  <a-input v-model:value="item.taskDesc" placeholder="请输入任务说明" />
                </a-form-item>
                <div v-if="item.taskType === FLOW_TASK_TYPE_ENUM.APPROVE.value || item.taskType == FLOW_TASK_TYPE_ENUM.CC.value" style="display: flex;flex-direction: column">
                  <a-form-item label="审批人类型" required>
                    <SmartEnumRadio :isButton="true" v-model:value="item.taskConfigObj.handlerType" enumName="FLOW_TASK_HANDLER_TYPE_ENUM" placeholder="请选择处理人类型" width="200px" @change="handlerChange(item)"/>
                  </a-form-item>
                  <a-form-item label="审批人" required v-if="item.taskConfigObj.handlerType == FLOW_TASK_HANDLER_TYPE_ENUM.ASSIGN_USER.value">
                    <EnterpriseEmployeeSelect v-model:value="item.taskConfigObj.configIdList"  mode="multiple" placeholder="请选择审批人" style="width:400px;" :enterpriseId="enterpriseIdRef"/>
                  </a-form-item>
                  <a-form-item label="审批人（角色）" required v-if="item.taskConfigObj.handlerType == FLOW_TASK_HANDLER_TYPE_ENUM.ASSIGN_ROLE.value">
                    <RoleSelect v-model:value="item.taskConfigObj.configIdList" mode="multiple" placeholder="请选择审批人角色" style="width:400px;" :api="roleApi.getRoleByEnterpriseId" :params="enterpriseIdRef"/>
                  </a-form-item>
                  <a-form-item label="审批人（部门）" required v-if="item.taskConfigObj.handlerType == FLOW_TASK_HANDLER_TYPE_ENUM.ASSIGN_DEPARTMENT.value">
                    <DepartmentSelect v-model:value="item.taskConfigObj.configIdList" mode="multiple" placeholder="请选择审批人部门" style="width:400px;" :api="departmentApi.queryDepartmentListByEnterpriseId" :enterpriseId="enterpriseIdRef"/>
                  </a-form-item>
                  <a-form-item label="审批人（服务）" required v-if="item.taskConfigObj.handlerType == FLOW_TASK_HANDLER_TYPE_ENUM.CUSTOM_SERVICE.value">
                    <a-input v-model:value="item.taskConfigObj.serviceName" placeholder="请输入服务名称" style="width:400px;"/>
                  </a-form-item>
                  <a-form-item label="任务监听（非必填）">
                    <FlowListenerSelect v-model:value="item.taskListener" placeholder="请输入任务监听" style="width:400px;"/>
                  </a-form-item>
                  <a-form-item label="操作代码（非必填）">
                    <a-input v-model:value="item.operateCode" placeholder="请输入操作代码" style="width:400px;"/>
                  </a-form-item>
                </div>
              </div>
              <div v-else>
                {{item.taskDesc}}
              </div>
            </template>
          </a-step>
        </a-steps>
      </div>
      <a-button @click="addTask(FLOW_TASK_TYPE_ENUM.APPROVE.value, '审批节点')">新增审批节点</a-button>
      <a-button @click="addTask(FLOW_TASK_TYPE_ENUM.CC.value, '抄送节点')" class="smart-margin-left10">新增抄送节点</a-button>
      <a-button type="primary" @click="onSubmit" class="smart-margin-left10">保存</a-button>
      <a-button type="primary" @click="onClose" class="smart-margin-left10">取消</a-button>
    </a-drawer>
  </div>
</template>
<script setup>
import { FLOW_TASK_TYPE_ENUM,FLOW_TASK_HANDLER_TYPE_ENUM } from '/@/constants/business/flow-const';
import { reactive, ref, onMounted, computed, watch } from 'vue';
import { message } from 'ant-design-vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import SmartEnumRadio from '/@/components/smart-enum-radio/index.vue';
import RoleSelect from '/@/components/role-select/index.vue';
import DepartmentSelect from '/@/components/department-select/index.vue';
import EnterpriseEmployeeSelect from '/@/components/enterprise-employee-select/index.vue';
import FlowListenerSelect from '/@/components/flow-listener/index.vue';
import { flowApi } from '/@/api/business/flow/flow-api';
import _ from 'lodash';
import {roleApi} from "/@/api/system/role/role-api";
import {departmentApi} from "/@/api/system/department/department-api";

const visible = ref(false)


let enterpriseIdRef = ref(null);
function showDrawer(enterpriseId){
  visible.value = true;
  enterpriseIdRef.value = enterpriseId
  queryFlowList();
}

let flowList = ref([]);
async function queryFlowList() {
  try {
    let resp = await flowApi.getFlowList();
    flowList.value = resp.data;
    if (!_.isEmpty(flowList.value)) {
      activeKey.value = flowList.value[0].flowId;
      queryFlowConfig();
    }

  } catch (e) {
    console.log(e);
  } finally {
  }
}


let activeKey = ref(null);
function changeTab (value) {
  console.log( activeKey.value, value)
  queryFlowConfig();

}

let flowConfig = ref(null);
async function queryFlowConfig() {
  try {
    let resp = await flowApi.getFlowDetail(activeKey.value, enterpriseIdRef.value);
    flowConfig.value = resp.data;
    flowConfig.value.taskList.forEach(e=>{
      if (e.taskConfig) {
        e.taskConfigObj = JSON.parse(e.taskConfig);
      }else {
        e.taskConfigObj = {};
      }
      e.editFlag = false;
    })
  } catch (e) {
    console.log(e);
  } finally {
  }
}

function handlerChange(item) {
  item.taskConfigObj.configIdList = [];
  item.taskConfigObj.serviceName = '';
}
let startTask = {
  taskName: '开始节点',
  taskType: FLOW_TASK_TYPE_ENUM.START.value,
  taskConfig: '',
  taskConfigObj: {},
  taskListener: '',
  operateCode: '',
  taskDesc: '',
};

function addTask(taskType, taskName, currentIndex = flowConfig.value.taskList.length - 1){
  let task = {
    taskName: taskName,
    taskType: taskType,
    editFlag: true,
    taskConfigObj: {
      handlerType: FLOW_TASK_HANDLER_TYPE_ENUM.ASSIGN_USER.value,
      configIdList: [],
      serviceName: '',
    },
    taskListener: '',
    operateCode: '',
    taskDesc: '',
  }
  task.taskConfig = JSON.stringify(task.taskConfigObj);

  if (_.isEmpty(flowConfig.value.taskList)){
    flowConfig.value.taskList=[startTask, task];
    return;
  }
  flowConfig.value.taskList.splice(currentIndex + 1, 0, task);
}

function taskEdit(item) {
  item.editFlag = true;
}

function taskCancelEdit(item) {
  item.editFlag = false;
  item.taskConfigObj = JSON.parse(item.taskConfig);
}

function taskDel(index){
  flowConfig.value.taskList.splice(index, 1)
}

// 提交货主信息
async function onSubmit() {
  useSpinStore().show();
  try {
    let param =  _.cloneDeep(flowConfig.value);
    param.enterpriseId = enterpriseIdRef.value;
    param.taskList.forEach(e=>{
      e.taskConfig = JSON.stringify(e.taskConfigObj);
    })
    await flowApi.flowConfig(param);
    message.success('更新成功');
    onClose();
  } catch (error) {
    console.log(error);
  } finally {
    useSpinStore().hide();
  }
}


// 关闭
function onClose() {
  flowConfig.value = null;
  activeKey.value = null;
  flowList.value = [];
  visible.value = false

}


// 上移
function taskMoveUp(index){
  let item = flowConfig.value.taskList[index];
  flowConfig.value.taskList.splice(index, 1);
  flowConfig.value.taskList.splice(index - 1, 0, item);
}

// 下移
function taskMoveDown(index){
  let item = flowConfig.value.taskList[index];
  flowConfig.value.taskList.splice(index, 1);
  flowConfig.value.taskList.splice(index + 1, 0, item);
}



defineExpose({
  showDrawer
})

</script>
<style lang="less" scoped>
.task-config {
  background-color: #f6f6f6;
  border-radius: 6px;
  padding: 10px;
}
</style>

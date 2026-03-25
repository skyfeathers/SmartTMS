<template>
  <div>
    <a-card title="基本信息">
      <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 7 }">
        <a-row>
          <a-col :span="8">
            <a-form-item label="申请日期" name="applyDate">
              <a-date-picker valueFormat="YYYY-MM-DD" v-model:value="form.applyDate" style="width: 200px"
                             placeholder="请选择申请日期"/>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="所属部门" name="departmentId">
              <DepartmentTreeSelect ref="departmentTreeSelect" width="100%" :init="false" placeholder="请选择使用部门"
                                    v-model:value="form.departmentId" style="width:200px"/>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="申请人" name="applyUserId">
              <EmployeeSelect v-model:value="form.applyUserId" :leaveFlag="false" placeholder="请选择申请人"
                              style="width: 90%"/>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="申请说明" name="remark">
              <a-textarea style="width: 90%" v-model:value="form.remark" placeholder="请输入申请说明"/>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-card>

    <a-card title="明细">
      <template #extra>
        <a-button @click="addItem" size="small" type="primary">新增</a-button>
      </template>
      <a-table
          size="small"
          :dataSource="form.itemList"
          :columns="columns"
          bordered
          :pagination="false"
      >
        <template #bodyCell="{ text, record, column ,index}">
          <template v-if="column.dataIndex === 'index'">
            {{index + 1}}
          </template>
          <template v-if="column.dataIndex === 'applyItemName'">
            <template v-if="record.operateFlag">
              <a-input v-model:value="record.applyItemName"/>
            </template>
            <template v-else>{{record.applyItemName}}</template>
          </template>
          <template v-if="column.dataIndex === 'applyAmount'">
            <template v-if="record.operateFlag">
              <a-input-number v-model:value="record.applyAmount" :min="0"/>
            </template>
            <template v-else>{{record.applyAmount}}</template>
          </template>
          <template v-if="column.dataIndex === 'remark'">
            <template v-if="record.operateFlag">
              <a-input v-model:value="record.remark"/>
            </template>
            <template v-else>{{record.remark}}</template>
          </template>
          <template v-if="column.dataIndex === 'action'">
            <a-button @click="removeItem(index)" size="small" danger>移除</a-button>
          </template>
        </template>
      </a-table>
    </a-card>

    <a-card class="smart-margin-top5 flex-end" size="small">
      <a-space>
        <a-button @click="onClose">取消</a-button>
        <a-button type="primary" @click="onSubmit">保存</a-button>
      </a-space>
    </a-card>
  </div>

</template>
<script setup>
import EmployeeSelect from '/@/components/employee-select/index.vue';
import EnterpriseSelect from '/@/components/enterprise-select/index.vue';
import DepartmentTreeSelect from '/@/components/department-tree-select/index.vue';

import { reactive, ref, nextTick } from 'vue';
import _ from 'lodash';
import { message } from 'ant-design-vue';
import { SmartLoading } from '/@/components/smart-loading';
import {costApplyApi } from '/@/api/business/oa/cost-apply-api';
import { smartSentry } from '/@/lib/smart-sentry';
import { useUserStore } from '/@/store/modules/system/user';
import { useRoute, useRouter } from 'vue-router';

// ------------------------ 事件 ------------------------

const emits = defineEmits(['reloadList']);

// ------------------------ 显示与隐藏 ------------------------
// 是否显示
const visibleFlag = ref(false);

function show (rowData) {
  Object.assign(form, formDefault);
  if (rowData && !_.isEmpty(rowData)) {
    Object.assign(form, rowData);
  }
  visibleFlag.value = true;
  nextTick(() => {
    formRef.value.clearValidate();
  });
}

let route = useRoute();
let router = useRouter();
function onClose () {
  useUserStore().closePage(route, router);
}

// ------------------------ 表单 ------------------------

// 组件ref
const formRef = ref();
const formDefault = {
  applyDate: null,
  departmentId: null,
  applyUserId: null,
  remark: null,
  itemList: [],//资产id列表
};

let form = reactive({ ...formDefault });

const rules = {
  applyDate: [{ required: true, message: '请选择申请日期' }],
  departmentId: [{ required: true, message: '请选择所属部门' }],
  applyUserId: [{ required: true, message: '请选择申请人' }],
};

// 点击确定，验证表单
async function onSubmit () {
  try {
    await formRef.value.validateFields();

    if (_.isEmpty(form.itemList)) {
      message.error('请填写费用申请明细');
      return;
    }
    save();
  } catch (err) {
    message.error('参数验证错误，请仔细填写表单数据!');
  }
}

// 新建、编辑API
async function save () {
  SmartLoading.show();
  try {
    await costApplyApi.addCostApply(form);
    message.success('新建成功');
    onClose();
  } catch (err) {
    smartSentry.captureError(err);
  } finally {
    SmartLoading.hide();
  }
}

// ------------------------ 表格操作 ------------------------
function removeItem (index) {
  form.itemList.splice(index, 1);
}

function addItem () {
  form.itemList.push({
    applyItemName: '',
    remark: '',
    applyAmount: 0,
    operateFlag: true
  });
}

const columns = ref([
  {
    title: '序号',
    dataIndex: 'index',
  },
  {
    title: '报销项目',
    dataIndex: 'applyItemName',
  },
  {
    title: '报销金额',
    dataIndex: 'applyAmount',
  },
  {
    title: '摘要',
    dataIndex: 'remark',
  },
  {
    title: '操作',
    dataIndex: 'action',
    width: 80
  },
]);
</script>

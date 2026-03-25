<template>
  <a-modal title="费用明细" width="1000px" :open="visibleFlag" @cancel="onClose" :maskClosable="false"
           :destroyOnClose="true">
    <a-row>
      <a-col :span="8">
        申请日期： {{ form.applyDate }}
      </a-col>

      <a-col :span="8">
        所属部门：{{ form.departmentName }}
      </a-col>
      <a-col :span="8">
        申请人：{{ form.applyUserName }}
      </a-col>
      <a-col :span="8">
        申请金额：{{ form.totalAmount }}
      </a-col>
      <a-col :span="8">
        申请说明：{{ form.remark }}
      </a-col>
      <a-col :span="8">
        创建时间：{{ form.createTime }}
      </a-col>
    </a-row>

    <a-card class="smart-margin-top10" size="small">
      <a-tabs>
        <a-tab-pane key="supplier" tab="明细">
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
            </template>
          </a-table>
        </a-tab-pane>
        <a-tab-pane key="operateLog" tab="操作记录">
          <DataTracerList
              :business-id="form.applyId"
              :business-type="DATA_TRACER_BUSINESS_TYPE_ENUM.COST_APPLY.value"/>
        </a-tab-pane>
      </a-tabs>
    </a-card>

    <template #footer>
      <a-space>
        <a-button class="smart-margin-left20" @click="onClose">关闭</a-button>
      </a-space>
    </template>
  </a-modal>
</template>
<script setup>
import DataTracerList from '/@/components/data-tracer/index.vue';
import { DATA_TRACER_BUSINESS_TYPE_ENUM } from '/@/constants/support/data-tracer-const';

import { reactive, ref, nextTick } from 'vue';

import { SmartLoading } from '/@/components/smart-loading';
import { costApplyApi } from '/@/api/business/oa/cost-apply-api';


// ------------------------ 显示与隐藏 ------------------------
// 是否显示
const visibleFlag = ref(false);

function show (applyId) {
  Object.assign(form, formDefault);
  visibleFlag.value = true;
  getDetail(applyId);
}

function onClose () {
  Object.assign(form, formDefault);
  visibleFlag.value = false;
}

// ------------------------ 详情 ------------------------
async function getDetail (applyId) {
  SmartLoading.show();
  try {
    let detail = await costApplyApi.getDetail(applyId);
    Object.assign(form, detail.data);
  } catch (e) {
    console.log(e);
  } finally {
    SmartLoading.hide();
  }
}

// ------------------------ 表单 ------------------------

// 组件ref
const formRef = ref();
const formDefault = {
  applyDate: null,
  enterpriseId: null,
  departmentId: null,
  applyUserId: null,
  remark: null,
  itemList: [],//资产id列表
};

let form = reactive({ ...formDefault });

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
]);

defineExpose({
  show,
});
</script>

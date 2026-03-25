<template>
  <a-form class="smart-query-form" v-privilege="'capitalFlow:query'">
    <a-row class="smart-query-form-row">
      <a-form-item class="smart-query-form-item" label="日期">
        <a-range-picker v-model:value="verificationDateRange" :presets="defaultTimeRanges" @change="changeVerificationTime"/>
      </a-form-item>

      <a-form-item class="smart-query-form-item" label="创建时间">
        <a-range-picker v-model:value="createDateRange" :presets="defaultTimeRanges" @change="changeCreateTime"/>
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
      </a-form-item>
    </a-row>
  </a-form>

  <a-card :bordered="false" :hoverable="true" size="small" class="smart-margin-top10 smart-margin-bottom10">
    <a-row>
      <a-col :span="8">
        <a-statistic title="应收已核销金额(元)" prefix="¥" :precision="2" :value="statisticInfo.receiveAmount"
                     style="margin-right: 50px"/>
      </a-col>
      <a-col :span="8">
        <a-statistic title="应付已核销金额(元)" prefix="¥" :precision="2" :value="statisticInfo.payAmount"/>
      </a-col>
    </a-row>
  </a-card>

  <a-card :bordered="false" :hoverable="true" size="small">
    <a-tabs v-model:activeKey="tabType" @change="ajaxQuery">
      <a-tab-pane :key="1" tab="应收流水">
        <ReceiveFlowList :queryForm="queryForm" ref="tableRef"/>
      </a-tab-pane>
      <a-tab-pane :key="2" tab="应付流水">
        <PayFlowList :queryForm="queryForm" ref="tableRef"/>
      </a-tab-pane>
    </a-tabs>
  </a-card>
</template>
<script setup>
import { reactive, ref, onMounted, nextTick } from 'vue';
import { defaultTimeRanges } from '/@/lib/default-time-ranges';
import ReceiveFlowList from './components/receive-flow-list.vue';
import PayFlowList from './components/pay-flow-list.vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import { capitalFlowApi } from '/@/api/business/capital-flow/capital-flow-api';
import { PAGE_SIZE } from '/@/constants/common-const';
// --------------------- 列表查询 ------------------------
let tabType = ref(1);
const queryFormDefault = {
  pageNum: 1,
  pageSize: PAGE_SIZE,
  startTime: null,
  endTime: null,
  verificationEndTime: null,
  verificationStartTime: null
};
const queryForm = reactive({ ...queryFormDefault });
const createDateRange = ref([]);
const verificationDateRange = ref([]);
const tableRef = ref();

function search () {
  queryForm.pageNum = 1;
  ajaxQuery();
}

function ajaxQuery () {
  try {
    nextTick(() => {
      tableRef.value.queryList();
      statistic();
    });
  } catch (e) {
    console.log(e);
  }
}

let statisticInfo = ref({
  payAmount: 0,
  receiveAmount: 0
});

async function statistic () {
  try {
    useSpinStore().show();
    let responseModel = await capitalFlowApi.statistic(queryForm);
    const data = responseModel.data;
    statisticInfo.value = data;
  } catch (e) {
    console.log(e);
  } finally {
    useSpinStore().hide();
  }
}

function resetQuery () {
  Object.assign(queryForm, queryFormDefault);
  createDateRange.value = [];
  verificationDateRange.value = [];
  tabType.value = 1;
  ajaxQuery();
}

function changeCreateTime (dates, dateStrings) {
  queryForm.startTime = dateStrings[0];
  queryForm.endTime = dateStrings[1];
}

function changeVerificationTime (dates, dateStrings) {
  queryForm.verificationStartTime = dateStrings[0];
  queryForm.verificationEndTime = dateStrings[1];
}

onMounted(() => {
  ajaxQuery();
  statistic();
});
</script>
<style lang="less" scoped>
.form-width {
  width: 240px;
}

.expired {
  color: red;
}
</style>

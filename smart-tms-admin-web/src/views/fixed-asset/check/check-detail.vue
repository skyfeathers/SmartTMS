<template>
  <a-card size="small">
    <a-page-header title="基本信息">
      <template #extra >
        <a-button type="primary" @click="showCompleteModal" v-if="!checkInfo.completeFlag && $privilege('check:complete')" size="small">完成盘点</a-button>
      </template>
      <div class="content">
        <div class="main">
          <a-descriptions :column="3" size="small">
            <a-descriptions-item label="盘点编号">{{ checkInfo.checkNo }}</a-descriptions-item>
            <a-descriptions-item label="盘点名称">{{ checkInfo.checkName }}</a-descriptions-item>
            <a-descriptions-item label="盘点类型">{{ $smartEnumPlugin.getDescByValue('ASSET_CHECK_TYPE_ENUM',checkInfo.checkType) }}</a-descriptions-item>
            <a-descriptions-item label="盘点位置">{{ checkInfo.locationName }}</a-descriptions-item>
            <a-descriptions-item label="盘点人员">{{ checkEmployee }}</a-descriptions-item>
            <a-descriptions-item label="盘点完成时间">{{ checkInfo.completeTime }}</a-descriptions-item>
            <a-descriptions-item label="备注">{{ checkInfo.remark }}</a-descriptions-item>
          </a-descriptions>
        </div>
        <div class="extra">
          <div class="status">
            <div class="label">状态</div>
            <div class="value">{{ checkInfo.completeFlag?'已完成':'未完成' }}</div>
          </div>
        </div>
      </div>
    </a-page-header>
  </a-card>
  <a-card class="smart-margin-top10" size="small">
    <div style="display:flex;justify-content:space-around">
      <a-statistic title="全部" :value="checkInfo.assetList.length"/>
      <a-statistic title="已盘点" :value="checkedCount"/>
      <a-statistic title="未盘点" :value="checkInfo.notCheckCount"/>
      <a-statistic title="盘盈" :value="checkInfo.profitCount"/>
      <a-statistic title="盘亏" :value="checkInfo.lossCount"/>
    </div>
  </a-card>
  <a-card class="smart-margin-top10" size="small">
    <a-tabs>
      <a-tab-pane key="supplier" tab="盘点清单">
        <CheckAssetList ref="assetTableSelectRef" :canCheckFlag="canCheckFlag" :assetList="checkInfo.assetList"
                        @refresh="getDetail"/>
      </a-tab-pane>
      <a-tab-pane key="operateLog" tab="操作记录">
        <DataTracerList
            :business-id="Number(checkId)"
            :business-type="DATA_TRACER_BUSINESS_TYPE_ENUM.ASSET_CHECK.value"/>
      </a-tab-pane>
    </a-tabs>
  </a-card>
  <CheckCompleteModal ref="completeRef" @refresh="getDetail"/>
</template>
<script setup>
import Upload from '/@/components/upload/index.vue';
import DataTracerList from '/@/components/data-tracer/index.vue';
import CheckAssetList from './components/check-asset-list.vue';
import CheckCompleteModal from './components/check-complete-modal.vue';

import { DATA_TRACER_BUSINESS_TYPE_ENUM } from '/@/constants/support/data-tracer-const';
import { ref, onMounted, computed } from 'vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import { useRoute, useRouter } from 'vue-router';
import { assetCheckApi } from '/@/api/fixed-asset/asset/check-api';
import { useUserStore } from '/@/store/modules/system/user';

const route = useRoute();
const router = useRouter();

// -------------- 计算属性 --------------
const checkedCount = computed(()=>{
  return checkInfo.value.assetList.length - checkInfo.value.notCheckCount;
})

const checkEmployee = computed(() => {
  let employeeList = checkInfo.value.employeeList || [];
  let employeeNameList = employeeList.map(e => e.actualName);
  return employeeNameList.join(',');
});

// 是否允许盘点
const canCheckFlag = computed(() => {
  if(checkInfo.value.completeFlag){
    return false;
  }
  let employeeList = checkInfo.value.employeeList || [];
  let employeeIdList = employeeList.map(e => e.employeeId);
  console.log(employeeIdList.includes(useUserStore().employeeId))
  return employeeIdList.includes(useUserStore().employeeId);
});
// -------------- 获取盘点详情 --------------
let checkId = route.query.checkId;
let checkInfo = ref({
  notCheckCount: 0,
  profitCount: 0,
  lossCount: 0,
  assetList: [],
  completeFlag: false
});

async function getDetail () {
  if (!checkId) {
    return;
  }
  try {
    useSpinStore().show();
    const { data } = await assetCheckApi.getDetail(checkId);
    checkInfo.value = data || {};
  } catch (e) {
    console.log(e);
  } finally {
    useSpinStore().hide();
  }
}

// -------------- 完成盘点 --------------
const completeRef = ref();

function showCompleteModal () {
  completeRef.value.showModal(checkId);
}

onMounted(() => {
  getDetail();
});
</script>
<style lang="less" scoped>
.ant-page-header {
  padding: 0;
}

.content-card {
  ::v-deep(.ant-card-body) {
    padding-top: 0px;
  }
}
.content {
  display: flex;
}

.status {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  width: max-content;
  margin-right: 30px;

  .label {
    font-size: 15px;
    color: #8c8c8c;
  }

  .value {
    font-size: 25px;
    color: #1f1f1f;
  }
}

</style>

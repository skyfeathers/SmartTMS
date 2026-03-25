<template>
  <a-card size="small" title="基本信息">
    <a-page-header>
      <div class="content">
        <div class="main">
          <a-descriptions :column="3" size="small">
            <a-descriptions-item label="退还编号">{{ requisitionInfo.requisitionRevertNo }}</a-descriptions-item>
            <a-descriptions-item label="退还日期">{{ requisitionInfo.useTime }}</a-descriptions-item>
            <a-descriptions-item label="退还部门">{{ requisitionInfo.departmentName }}</a-descriptions-item>
            <a-descriptions-item label="退还人">{{ requisitionInfo.userName }}</a-descriptions-item>
          </a-descriptions>
        </div>
        <div class="extra">
          <div class="status">
            <div class="label">状态</div>
            <div class="value">{{ $smartEnumPlugin.getDescByValue('ALLOCATION_STATUS_ENUM',requisitionInfo.status) }}</div>
          </div>
        </div>
      </div>
    </a-page-header>

  </a-card>
  <a-card class="smart-margin-top10" size="small">
    <a-tabs>
      <a-tab-pane key="supplier" tab="资产列表">
        <AssetTableSelect ref="assetTableSelectRef" :assetList="requisitionInfo.assetList"/>
      </a-tab-pane>
      <a-tab-pane key="operateLog" tab="操作记录">
        <DataTracerList
            :business-id="requisitionRevertId"
            :business-type="DATA_TRACER_BUSINESS_TYPE_ENUM.ASSET_REVERT.value"/>
      </a-tab-pane>
    </a-tabs>
  </a-card>

</template>
<script setup>
import DataTracerList from '/@/components/data-tracer/index.vue';
import AssetTableSelect from '/@/components/fixed-asset/asset-table-select/index.vue';

import { DATA_TRACER_BUSINESS_TYPE_ENUM } from '/@/constants/support/data-tracer-const';
import { ref, onMounted } from 'vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import { useRoute, useRouter } from 'vue-router';
import { assetRequisitionApi } from '/@/api/fixed-asset/asset/requisition-api';

const route = useRoute();
const router = useRouter();

// -------------- 获取订单详情 --------------
let requisitionRevertId = route.query.requisitionRevertId;
let requisitionInfo = ref({
  sourceId: []
});

async function getDetail () {
  if (!requisitionRevertId) {
    return;
  }
  try {
    useSpinStore().show();
    const { data } = await assetRequisitionApi.getDetail(requisitionRevertId);
    requisitionInfo.value = data;
  } catch (e) {
    console.log(e);
  } finally {
    useSpinStore().hide();
  }
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

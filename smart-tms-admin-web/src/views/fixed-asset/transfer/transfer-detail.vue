<template>
  <a-card size="small" title="基本信息">
    <a-page-header>
      <div class="content">
        <div class="main">
          <a-descriptions :column="3" size="small">
            <a-descriptions-item label="转移编号">{{ transferInfo.transferNo }}</a-descriptions-item>
            <a-descriptions-item label="转出位置">{{ transferInfo.fromLocationName }}</a-descriptions-item>
            <a-descriptions-item label="转入位置">{{ transferInfo.toLocationName }}</a-descriptions-item>
            <a-descriptions-item label="备注">{{ transferInfo.remark }}</a-descriptions-item>
          </a-descriptions>
        </div>
        <div class="extra">
          <div class="status">
            <div class="label">状态</div>
            <div class="value">{{ $smartEnumPlugin.getDescByValue('TRANSFER_STATUS_ENUM',transferInfo.status) }}</div>
          </div>
        </div>
      </div>
    </a-page-header>

  </a-card>
  <a-card class="smart-margin-top10" size="small">
    <a-tabs>
      <a-tab-pane key="supplier" tab="资产列表">
        <AssetTableSelect ref="assetTableSelectRef" :assetList="transferInfo.assetList"/>
      </a-tab-pane>
      <a-tab-pane key="operateLog" tab="操作记录">
        <DataTracerList
            :business-id="transferId"
            :business-type="DATA_TRACER_BUSINESS_TYPE_ENUM.ASSET_TRANSFER.value"/>
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
import { assetTransferApi } from '/@/api/fixed-asset/asset/transfer-api';

const route = useRoute();
const router = useRouter();

// -------------- 获取订单详情 --------------
let transferId = route.query.transferId;
let transferInfo = ref({
  sourceId: []
});

async function getDetail () {
  if (!transferId) {
    return;
  }
  try {
    useSpinStore().show();
    const { data } = await assetTransferApi.getDetail(transferId);
    transferInfo.value = data;
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

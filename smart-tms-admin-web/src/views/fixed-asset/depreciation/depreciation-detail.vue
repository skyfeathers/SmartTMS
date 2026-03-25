<template>
  <a-card size="small" title="基本信息">
    <a-page-header>
      <div class="content">
        <div class="main">
          <a-descriptions :column="3" size="small">
            <a-descriptions-item label="折旧编号">{{ depreciationInfo.depreciationNo }}</a-descriptions-item>
            <a-descriptions-item label="计提日期">{{
                (depreciationInfo.depreciationDate || '').substring(0, 7)
              }}
            </a-descriptions-item>
            <a-descriptions-item label="备注">{{ depreciationInfo.remark }}</a-descriptions-item>
          </a-descriptions>
        </div>
      </div>
    </a-page-header>

  </a-card>
  <a-card class="smart-margin-top10" size="small">
    <a-tabs>
      <a-tab-pane key="supplier" tab="资产列表">
        <AssetList :assetList="depreciationInfo.assetList"/>
      </a-tab-pane>
      <a-tab-pane key="operateLog" tab="操作记录">
        <DataTracerList
            :business-id="depreciationId"
            :business-type="DATA_TRACER_BUSINESS_TYPE_ENUM.ASSET_DEPRECIATION.value"/>
      </a-tab-pane>
    </a-tabs>
  </a-card>

</template>
<script setup>
import DataTracerList from '/@/components/data-tracer/index.vue';
import AssetList from './components/depreciation-asset-list.vue';
import { DATA_TRACER_BUSINESS_TYPE_ENUM } from '/@/constants/support/data-tracer-const';
import { ref, onMounted } from 'vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import { useRoute, useRouter } from 'vue-router';
import { assetDepreciationApi } from '/@/api/fixed-asset/asset/depreciation-api';

const route = useRoute();
const router = useRouter();

// -------------- 获取订单详情 --------------
let depreciationId = route.query.depreciationId;
let depreciationInfo = ref({
  assetList: []
});

async function getDetail () {
  if (!depreciationId) {
    return;
  }
  try {
    useSpinStore().show();
    const { data } = await assetDepreciationApi.getDetail(depreciationId);
    depreciationInfo.value = data;
  } catch (e) {
    console.log(e);
  } finally {
    useSpinStore().hide();
  }
}

onMounted(() => {
  getDetail();
});

// ---------------------------- 表格列 ----------------------------
const columns = ref([
  {
    title: '资产名称',
    dataIndex: 'assetName',
  },
  {
    title: '资产编号',
    dataIndex: 'assetNo',
  },
  {
    title: '原值',
    dataIndex: 'price',
    width: 60
  },
  {
    title: '期初累计折旧',
    dataIndex: 'initialDepreciationAmount',
  },
  {
    title: '本月折旧',
    dataIndex: 'depreciationAmount',
    width: 90
  },
  {
    title: '月折旧率',
    dataIndex: 'residualValueRate',
  },
  {
    title: '期末累计折旧',
    dataIndex: 'endingDepreciationAmount',
  },
]);

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

</style>

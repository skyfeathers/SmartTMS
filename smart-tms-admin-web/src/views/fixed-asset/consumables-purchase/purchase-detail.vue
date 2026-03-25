<template>
  <a-card size="small">
    <a-page-header title="基本信息">
      <div class="content">
        <div class="main">
          <a-descriptions :column="3" size="small">
            <a-descriptions-item label="采购编号">{{ purchaseInfo.purchaseNo }}</a-descriptions-item>
            <a-descriptions-item label="所属位置">{{ purchaseInfo.locationName }}</a-descriptions-item>
            <a-descriptions-item label="来源">{{ purchaseInfo.sourceId[0].valueName }}</a-descriptions-item>
            <a-descriptions-item label="备注">{{ purchaseInfo.remark }}</a-descriptions-item>
          </a-descriptions>
        </div>
      </div>
    </a-page-header>
  </a-card>
  <a-card class="smart-margin-top10" size="small">
    <a-tabs>
      <a-tab-pane key="supplier" tab="采购列表">
        <!---------- 表格 begin ----------->
        <a-table
            :scroll="{x:1000}"
            size="small"
            :dataSource="purchaseInfo.itemList"
            :columns="columns"
            bordered
            :pagination="false"
        >
          <template #bodyCell="{ text, record, column }">
            <template v-if="column.dataIndex === 'consumablesNo' || column.dataIndex === 'consumablesName'">
              <a @click="toDetail(record.consumablesId)">{{ record[column.dataIndex] }}</a>
            </template>
            <template v-if="column.dataIndex === 'sourceId'">
              {{ record.sourceId[0].valueName }}
            </template>
          </template>
        </a-table>
        <!---------- 表格 end ----------->
      </a-tab-pane>
      <a-tab-pane key="operateLog" tab="操作记录">
        <DataTracerList
            :business-id="Number(purchaseId)"
            :business-type="DATA_TRACER_BUSINESS_TYPE_ENUM.CONSUMABLES_STOCK_PURCHASE.value"/>
      </a-tab-pane>
    </a-tabs>
  </a-card>
</template>
<script setup>
import DataTracerList from '/@/components/data-tracer/index.vue';

import { DATA_TRACER_BUSINESS_TYPE_ENUM } from '/@/constants/support/data-tracer-const';
import { ref, onMounted } from 'vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import { useRoute, useRouter } from 'vue-router';
import { consumablesPurchaseApi } from '/@/api/fixed-asset/asset/consumables-purchase-api';

const route = useRoute();
const router = useRouter();

// -------------- 获取盘点详情 --------------
let purchaseId = route.query.purchaseId;
let purchaseInfo = ref({
  itemList: [],
  sourceId: [{}]
});

async function getDetail () {
  if (!purchaseId) {
    return;
  }
  try {
    useSpinStore().show();
    const { data } = await consumablesPurchaseApi.getDetail(purchaseId);
    purchaseInfo.value = data || {};
  } catch (e) {
    console.log(e);
  } finally {
    useSpinStore().hide();
  }
}

onMounted(() => {
  getDetail();
});
function toDetail (consumablesId) {
  router.push({
    path: '/fixed-asset/consumables-stock-detail',
    query: {
      consumablesId
    }
  });
}
// ---------------------------- 表格列 ----------------------------

const columns = ref([
  {
    title: '耗材编号',
    dataIndex: 'consumablesNo',
  },
  {
    title: '所属分类',
    dataIndex: 'categoryName',
    ellipsis: true,
  },
  {
    title: '耗材名称',
    dataIndex: 'consumablesName',
    ellipsis: true,
  },
  {
    title: '采购价格',
    dataIndex: 'price'
  },
  {
    title: '采购数量',
    dataIndex: 'count'
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

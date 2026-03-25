<template>
  <a-card size="small">
    <a-page-header title="基本信息">
      <div class="content">
        <div class="main">
          <a-descriptions :column="3" size="small">
            <a-descriptions-item label="耗材编号">{{ consumablesInfo.consumablesNo }}</a-descriptions-item>
            <a-descriptions-item label="所属分类">{{ consumablesInfo.categoryName }}</a-descriptions-item>
            <a-descriptions-item label="耗材名称">{{ consumablesInfo.consumablesName }}</a-descriptions-item>
            <a-descriptions-item label="均价">{{ consumablesInfo.averagePrice }}</a-descriptions-item>
            <a-descriptions-item label="库存">{{ consumablesInfo.stockCount }}</a-descriptions-item>
            <a-descriptions-item label="预警库存">{{ consumablesInfo.stockWarningValue }}</a-descriptions-item>
            <a-descriptions-item label="备注">{{ consumablesInfo.remark }}</a-descriptions-item>
            <a-descriptions-item label="创建人">{{ consumablesInfo.createUserName }}</a-descriptions-item>
            <a-descriptions-item label="创建时间">{{ consumablesInfo.createTime }}</a-descriptions-item>
            <a-descriptions-item label="附件">
              <file-preview :fileList="consumablesInfo.attachment"/>
            </a-descriptions-item>

          </a-descriptions>
        </div>
      </div>
    </a-page-header>
  </a-card>
  <a-card class="smart-margin-top10" size="small">
    <a-tabs>
      <a-tab-pane key="stockList" tab="库存">
        <a-table
            size="small"
            :dataSource="consumablesInfo.stockList"
            :columns="columns"
            rowKey="locationId"
            :pagination="false"
            bordered
        >
        </a-table>
      </a-tab-pane>
      <a-tab-pane key="stockRecord" tab="库存变动记录">
        <ConsumablesStockRecordList :consumablesId="consumablesId" :enterpriseId="consumablesInfo.enterpriseId"/>
      </a-tab-pane>
      <a-tab-pane key="operateLog" tab="操作记录">
        <DataTracerList
            :business-id="Number(consumablesId)"
            :business-type="DATA_TRACER_BUSINESS_TYPE_ENUM.CONSUMABLES_STOCK.value"/>
      </a-tab-pane>
    </a-tabs>
  </a-card>
</template>
<script setup>
import DataTracerList from '/@/components/data-tracer/index.vue';
import FilePreview from '/@/components/file-preview/index.vue';
import ConsumablesStockRecordList from './components/stock-record-list.vue';

import { DATA_TRACER_BUSINESS_TYPE_ENUM } from '/@/constants/support/data-tracer-const';
import { ref, onMounted, computed, reactive } from 'vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import { useRoute, useRouter } from 'vue-router';
import { consumablesStockApi } from '/@/api/fixed-asset/asset/consumables-stock-api';
import { useUserStore } from '/@/store/modules/system/user';

const route = useRoute();
const router = useRouter();

// -------------- 获取盘点详情 --------------
let consumablesId = route.query.consumablesId;
let consumablesInfo = ref({});

async function getDetail () {
  if (!consumablesId) {
    return;
  }
  try {
    useSpinStore().show();
    const { data } = await consumablesStockApi.getDetail(consumablesId);
    consumablesInfo.value = data || {};
  } catch (e) {
    console.log(e);
  } finally {
    useSpinStore().hide();
  }
}

const columns = reactive([
  {
    title: '所属位置',
    dataIndex: 'locationName',
    width: 160,
  },
  {
    title: '平均价格',
    dataIndex: 'averagePrice',
    width: 80,
  },
  {
    title: '库存数量',
    dataIndex: 'stockCount',
    width: 80,
  },
  {
    title: '库存成本',
    dataIndex: 'totalAmount',
    width: 80,
  }
]);

onMounted(() => {
  getDetail();
});
</script>
<style lang="less" scoped>
.ant-page-header {
  padding: 0;
}

</style>

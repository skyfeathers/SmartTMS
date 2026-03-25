<!--
 * @Description: 低值易耗品库存变动记录
 * @version:
 * @Author: lidoudou
 * @Date: 2023-04-19 11:01:48
 * @LastEditors: lidoudou
 * @LastEditTime: 2023-04-19
-->
<template>
  <!---------- 查询表单form begin ----------->
  <a-form class="smart-query-form" v-privilege="'consumablesStock:query'">
    <a-row class="smart-query-form-row">
      <a-form-item class="smart-query-form-item">
        <a-form-item label="所属位置" name="locationId">
          <LocationSelect @change="ajaxQuery" placeholder="请选择所属位置" style="width:200px"/>
        </a-form-item>
      </a-form-item>
    </a-row>
  </a-form>
  <!---------- 查询表单form end ----------->
  <a-card size="small" :bordered="false">
    <a-table
        size="small"
        :scroll="{x:1100}"
        :dataSource="tableData"
        :columns="columns"
        rowKey="id"
        :pagination="false"
        bordered
    >
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'orderNo'">
          <a-button @click="toDetail(record)" type="link">{{record.orderNo}}</a-button>
        </template>
      </template>
    </a-table>

    <div class="smart-query-table-page">
      <a-pagination
          showSizeChanger
          showQuickJumper
          show-less-items
          :pageSizeOptions="PAGE_SIZE_OPTIONS"
          :defaultPageSize="queryForm.pageSize"
          v-model:current="queryForm.pageNum"
          v-model:pageSize="queryForm.pageSize"
          :total="total"
          @change="ajaxQuery"
          @showSizeChange="ajaxQuery"
          :show-total="(total) => `共${total}条`"
      />
    </div>
  </a-card>
</template>
<script setup>
import LocationSelect from '/@/components/fixed-asset/locaton-select/index.vue';

import { reactive, onMounted, ref, watch } from 'vue';
import { PAGE_SIZE, PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import { stockRecordApi } from '/@/api/fixed-asset/asset/consumables-stock-record-api';
import { CONSUMABLES_STOCK_RECORD_TYPE_ENUM } from '/@/constants/fixed-asset/consumables-const';
import { useRouter } from 'vue-router';

let props = defineProps({
  consumablesId: {
    type: Number,
  },
  enterpriseId: {
    type: Number,
  },
});

const columns = reactive([
  {
    title: '操作时间',
    dataIndex: 'createTime',
    width: 160,
  },
  {
    title: '操作人',
    dataIndex: 'createUserName',
    width: 80,
  },
  {
    title: '所属位置',
    dataIndex: 'locationName',
    ellipsis: true,
    width: 120,
  },
  {
    title: '变动类型',
    dataIndex: 'recordTypeDesc',
    ellipsis: true,
    width: 100,
  },
  {
    title: '单据编号',
    dataIndex: 'orderNo',
    ellipsis: true,
    width: 170,
  },
  {
    title: '变动前库存数量',
    dataIndex: 'beforeCount',
    width: 120,
  },
  {
    title: '变动数量',
    dataIndex: 'changeCount',
    width: 100,
  },
  {
    title: '变动后库存数量',
    dataIndex: 'afterCount',
    width: 140,
  },
  {
    title: '变动前平均采购价',
    dataIndex: 'beforeAveragePrice',
    width: 140,
  },
  {
    title: '本次采购价',
    dataIndex: 'price',
    width: 110,
  },
  {
    title: '变动后采购价格',
    dataIndex: 'afterAveragePrice',
    width: 140,
  },
]);

const queryFormState = {
  pageNum: 1,
  pageSize: PAGE_SIZE,
  consumablesId: props.consumablesId,
  locationId: null,
};
const queryForm = reactive({ ...queryFormState });
const tableLoading = ref(false);
const tableData = ref([]);
const total = ref(0);

function resetQuery () {
  Object.assign(queryForm, queryFormState);
  ajaxQuery();
}

async function ajaxQuery (locationId) {
  try {
    tableLoading.value = true;
    let params = Object.assign(queryForm, { locationId });
    let responseModel = await stockRecordApi.queryPage(params);
    const list = responseModel.data.list;
    total.value = responseModel.data.total;
    tableData.value = list;
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}

let router = useRouter();
function toDetail ({ recordType, orderId }) {
  if(recordType === CONSUMABLES_STOCK_RECORD_TYPE_ENUM.PURCHASE.value){
    router.push({
      path: '/fixed-asset/consumables-purchase-detail',
      query: {
        purchaseId: orderId
      }
    });
  }
  if(recordType === CONSUMABLES_STOCK_RECORD_TYPE_ENUM.REQUISITION.value){
    router.push({
      path: '/fixed-asset/consumables-requisition-detail',
      query: {
        requisitionId: orderId
      }
    });
  }
  if(recordType === CONSUMABLES_STOCK_RECORD_TYPE_ENUM.CHECK.value){
    router.push({
      path: '/fixed-asset/consumables-check-detail',
      query: {
        checkId: orderId
      }
    });
  }

}

// ========= 定义 watch 监听 ===============
watch(
    () => props.consumablesId,
    (e) => {
      if (e) {
        queryForm.consumablesId = e;
        ajaxQuery();
      }
    },
    { immediate: true }
);


</script>
<style scoped lang="less">
.operate-content {
  line-height: 20px;
  margin: 5px 0px;
}
</style>

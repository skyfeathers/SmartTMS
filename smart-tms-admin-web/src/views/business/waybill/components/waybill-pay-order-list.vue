<template>
  <a-card size="small"
          :bordered="false">
    <a-table size="small"
             :scroll="{ x: 3500 }"
             :dataSource="tableData"
             :columns="columns"
             :rowKey="(record,index)=>{return index}"
             :pagination="false"
             bordered>
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'payOrderNumber'">
          <a-button @click="payOrderDetail(record.payOrderId)" type="link">{{ text }}</a-button>
        </template>
        <template v-if="column.dataIndex === 'settleType'">
          {{ $smartEnumPlugin.getDescByValue('SETTLE_TYPE_ENUM', record.settleType) }}
        </template>
        <template v-if="column.dataIndex === 'payOrderStatus'">
          <a-tag :color="$smartEnumPlugin.getFieldByValue('PAY_ORDER_STATUS_ENUM', record.payOrderStatus, 'color')">{{
              $smartEnumPlugin.getDescByValue('PAY_ORDER_STATUS_ENUM', record.payOrderStatus)
            }}
          </a-tag>
        </template>
        <template v-if="column.dataIndex === 'auditStatus'">
          <a-tag :color="$smartEnumPlugin.getFieldByValue('FLOW_AUDIT_STATUS_ENUM', record.auditStatus, 'color')">{{
              $smartEnumPlugin.getDescByValue('FLOW_AUDIT_STATUS_ENUM', record.auditStatus)
            }}
          </a-tag>
        </template>
        <template v-if="column.dataIndex === 'payBankAccount'">
          {{record.paymentVO ? record.paymentVO.payAccountName :''}}/{{record.paymentVO ? record.paymentVO.payBankAccount :''}}
        </template>
        <template v-if="column.dataIndex === 'receiveBankAccount'">
          {{record.receiveVO ? record.receiveVO.receiveAccountName :''}}/{{record.receiveVO ? record.receiveVO.receiveBankAccount :''}}
        </template>
        <template v-if="column.dataIndex === 'payUserName'">
          {{record.paymentVO ? record.paymentVO.createUserName :''}}
        </template>
        <template v-if="column.dataIndex === 'payTime'">
          {{record.paymentVO ? record.paymentVO.createTime :''}}
        </template>
        <template v-if="column.dataIndex === 'verificationUserName'">
          {{record.verificationVO ? record.verificationVO.createUserName :''}}
        </template>
        <template v-if="column.dataIndex === 'verificationTime'">
          {{record.verificationVO ? record.verificationVO.createTime :''}}
        </template>
      </template>
    </a-table>
  </a-card>
</template>
<script setup>
import {reactive, onMounted, ref, watch} from 'vue';
import {payOrderApi} from '/@/api/business/pay/pay-order-api';
import {baseColumns, payColumns, verificationColumns} from '/@/views/business/pay/components/pay-order-list-table-column';
import {useRouter} from "vue-router";

let props = defineProps({
  waybillId: {
    type: Number,
  }
});

const columns = reactive([...baseColumns, ...payColumns, ...verificationColumns]);

const tableLoading = ref(false);
const tableData = ref([]);

async function ajaxQuery() {
  try {
    tableLoading.value = true;
    if (props.waybillId) {
      let responseModel = await payOrderApi.queryByWaybillId(props.waybillId);
      tableData.value = responseModel.data;
    }else{
      tableData.value = [];
    }
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}

// ----------- 详情页-----------
let router = useRouter();

function payOrderDetail(payOrderId) {
  router.push({path: '/pay/pay-order-detail', query: {payOrderId}});
}

// ========= 定义 watch 监听 ===============
watch(
    () => props.waybillId,
    (e) => {
      if (e) {
        ajaxQuery();
      }else {
        tableData.value = [];
      }
    },
    {immediate: true}
);

onMounted(ajaxQuery);
</script>

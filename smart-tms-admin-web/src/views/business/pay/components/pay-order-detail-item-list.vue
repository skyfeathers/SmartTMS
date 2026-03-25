<template>
  <a-table
      :scroll="{ x: 'max-content' }"
      :columns="columns"
      :dataSource="costList"
      bordered
      :pagination="false"
      size="small"
  >
    <template #bodyCell="{ text, record, index, column }">
      <template v-if="column.dataIndex === 'waybillNumber'">
        <div class="waybill-code">
          <a-button @click="waybillDetail(record.waybillId)" type="link">{{ text }}</a-button>
          <a v-if="!$lodash.isEmpty(record.contractFile)" @click="showContractAttachmentModal(record)" >
            <file-outlined />
          </a>
        </div>
      </template>
      <template v-if="column.dataIndex === 'orderType'">
        {{ $smartEnumPlugin.getDescByValue('ORDER_TYPE_ENUM', text) }}
      </template>

      <template v-if="column.dataIndex === 'splitTransportFlag'">
        <span>{{ text ? '是' : '否' }}</span>
      </template>

      <template v-if="column.dataIndex === 'driverName'">
        <div v-if="record.splitTransportFlag" style = "display: flex">
          <span>{{ text }}</span>
          <a @click="showSplitTransportModal(record.waybillId)">更多</a>
        </div>
        <span v-else>{{ text }}</span>
      </template>
    </template>
  </a-table>

  <WaybillSplitTransportModal ref="splitTransportModalRef" />

  <a-modal v-model:open="showContractAttachmentFlag" title="合同预览" :footer="null" width="500px">
    <a-card>
      <Upload :default-file-list="contractAttachmentList" listType="picture-card" :showUploadBtn="false" />
    </a-card>
  </a-modal>
</template>
<script setup>
import {getCurrentInstance, reactive, ref} from 'vue';
import FilePreview from '/@/components/file-preview/index.vue';
import Upload from '/@/components/upload/index.vue';
import WaybillSplitTransportModal from '/@/views/business/waybill/components/waybill-split-transport-modal.vue';
import {SETTLE_TYPE_ENUM} from '/@/constants/business/waybill-const';
import { ORDER_TYPE_ENUM } from '/@/constants/business/order-const';
import { COST_ITEM_CATEGORY_ENUM } from '/@/constants/business/cost-const';
import {useRouter} from "vue-router";

const props = defineProps({
  costList: {
    type: Object,
    default: () => {
      return [];
    }
  },
  payOrderDetail: {
    type: Object,
    default: () => {
      return {};
    }
  },
});
const columns = reactive([
  {
    title: '费用名称',
    width: 80,
    dataIndex: 'costItemName',
    fixed: 'left'
  },
  {
    title: '应付',
    width: 80,
    dataIndex: 'costAmount',
  },
]);

function getSettleObjectName(record){
  if (record.orderType == ORDER_TYPE_ENUM.NETWORK_FREIGHT.value && record.costItemCategory == COST_ITEM_CATEGORY_ENUM.NFT_COST.value){
    return props.payOrderDetail.nftEnterpriseName;
  }else {
    if (SETTLE_TYPE_ENUM.DRIVER.value == record.settleType) {
      return record.driverName;
    }
    return record.fleetName;
  }
}

const internalInstance = getCurrentInstance();
const smartEnumPlugin = internalInstance.appContext.config.globalProperties.$smartEnumPlugin;
function getSettleObjectType(record) {
  if (record.orderType == ORDER_TYPE_ENUM.NETWORK_FREIGHT.value && record.costItemCategory == COST_ITEM_CATEGORY_ENUM.NFT_COST.value){
    return '货运平台';
  }else {
    return smartEnumPlugin.getDescByValue('SETTLE_TYPE_ENUM', record.settleType);
  }
}

// ----------- 详情页-----------
let router = useRouter();
function waybillDetail(waybillId) {
  router.push({ path: '/waybill/waybill-detail', query: { waybillId } });
}

let showContractAttachmentFlag = ref(false);
let contractAttachmentList = ref([])

function showContractAttachmentModal(record) {
  showContractAttachmentFlag.value = true;
  contractAttachmentList.value = record.contractFile || [];
}

let splitTransportModalRef = ref();
function showSplitTransportModal(waybillId) {
  splitTransportModalRef.value.showModal(waybillId);
}

</script>
<style scoped lang="less">
.waybill-code{
  display: flex;
  justify-content: space-between;
}
</style>

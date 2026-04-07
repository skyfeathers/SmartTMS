<template>
  <a-card class="smart-margin-top10" size="small">
    <a-page-header :title="`收款单号：${detail?.receiveOrderNumber}`">
      <template #extra>
        <a-button @click="makeInvoiceExportExcel" v-privilege="'receiveOrderCheck:makeInvoiceExport'" type="primary" size="small"
        >导出财务开票
        </a-button>
        <a-button @click="confirmInvoice" v-privilege="'receiveOrderInvoice:invoice'" type="primary" size="small" v-if="detail?.makeInvoiceFlag  && detail?.invoiceStatus == INVOICE_STATUS_ENUM.WAIT_INVOICE.value && detail?.checkStatus == CHECK_STATUS_ENUM.CHECK.value"
        >开票
        </a-button>
        <a-button @click="waybillExportExcel" v-privilege="'receiveOrderCheck:waybillExport'" type="primary" size="small"
        >导出对账明细
        </a-button>
        <a-button @click="confirmCheck" v-privilege="'receiveOrderCheck:confirmCheck'" type="primary" size="small"
                  v-if="detail?.checkStatus == CHECK_STATUS_ENUM.WAIT_CHECK.value">确认核算
        </a-button>
        <a-button @click="confirmVerification" v-privilege="'receiveOrderVerification:verification'" type="primary" size="small"
                  v-if="detail?.checkStatus == CHECK_STATUS_ENUM.CHECK.value">确认核销
        </a-button>
      </template>
      <div class="content">
        <div class="main">
          <a-descriptions :column="3" size="small">
            <a-descriptions-item label="货主名称">{{ detail?.shortName }}</a-descriptions-item>
            <a-descriptions-item label="应收总额">{{ detail?.totalAmount }}</a-descriptions-item>
            <a-descriptions-item label="已销金额">{{ detail?.verificationAmount }}</a-descriptions-item>

            <a-descriptions-item label="未销金额">{{ detail?.unpaidAmount }}</a-descriptions-item>
            <a-descriptions-item label="应收对账备注">{{ detail?.remark }}</a-descriptions-item>

            <a-descriptions-item label="是否需要开票">{{ detail?.makeInvoiceFlag ? '需要' : '不需要' }}</a-descriptions-item>
            <a-descriptions-item v-if="detail?.makeInvoiceFlag" label="税点(%)">{{
                detail?.taxPoint
              }}
            </a-descriptions-item>

            <a-descriptions-item label="核算凭证">
              <file-preview :fileList="detail?.checkAttachment"/>
            </a-descriptions-item>
            <a-descriptions-item label="对账单">
              <file-preview :fileList="detail?.billAttachment"/>
            </a-descriptions-item>
            <a-descriptions-item label="核算附件">
              <file-preview :fileList="detail?.attachment"/>
            </a-descriptions-item>
            <a-descriptions-item label="业务时间">{{ (detail.businessDate || '').substring(0,7) }}</a-descriptions-item>
            <a-descriptions-item label="创建人">{{ detail?.createUserName }}</a-descriptions-item>
            <a-descriptions-item label="创建时间">{{ detail?.createTime }}</a-descriptions-item>
          </a-descriptions>
        </div>
      </div>
    </a-page-header>

  </a-card>

  <a-card class="smart-margin-top10" size="small" title="邮寄信息">
    <a-descriptions :column="3" size="small">
            <a-descriptions-item label="收货人">{{ detail?.mailAddressVO?.receivePerson }}</a-descriptions-item>
            <a-descriptions-item label="收货人电话">{{ detail?.mailAddressVO?.receivePersonPhone }}</a-descriptions-item>
            <a-descriptions-item label="邮箱">{{ detail?.mailAddressVO?.email }}</a-descriptions-item>
    </a-descriptions>
  </a-card>

  <a-card class="smart-margin-top10" size="small">
    <a-tabs>
      <a-tab-pane key="detail" tab="应收账款明细">
        <a-table :columns="waybillReceiveColumns" :dataSource="detail.waybillList" bordered :pagination="false" size="small"
                 :scroll="{ x: '100%' }">
          <template #headerCell="{ column }">
            <template v-if="column.dataIndex === 'taxAmount'">
        <span>
          税金
            <a-tooltip placement="top">
              <template #title>
                <span>税金 = 应收 * 税点</span>
              </template>
               <question-circle-outlined />
            </a-tooltip>
        </span>
            </template>
            <template v-if="column.dataIndex === 'profitAmount'">
        <span>
          本单利润
            <a-tooltip placement="top">
              <template #title>
                <span>利润 = 应收 - 应付 - (应收 * 税点)</span>
              </template>
               <question-circle-outlined />
            </a-tooltip>
        </span>
            </template>
            <template v-if="column.dataIndex === 'paidAmount'">
        <span>
          已付金额
            <a-tooltip placement="top">
              <template #title>
                <span>提交应付结算后财务已支付的金额</span>
              </template>
               <question-circle-outlined />
            </a-tooltip>
        </span>
            </template>
          </template>
          <template #bodyCell="{ text, record, index, column }">
            <template v-if="column.dataIndex === 'waybillNumber'">
              <div class="waybill-code">
                <a v-if="$privilege('waybill:detail')" @click="waybillDetail(record.waybillId)" type="link">{{ text }}</a>
                <span v-else>{{text}}</span>
                <a v-if="!$lodash.isEmpty(record.contractFile)" @click="showContractAttachmentModal(record)" >
                  <file-outlined />
                </a>
              </div>
            </template>
            <template v-if="column.dataIndex === 'orderNo'">
              <a @click="orderDetail(record.orderId)" type="link">{{ text }}</a>
            </template>
            <template v-if="column.dataIndex === 'orderType'">
              {{ $smartEnumPlugin.getDescByValue('ORDER_TYPE_ENUM', text) }}
            </template>
          </template>
        </a-table>
      </a-tab-pane>
      <a-tab-pane key="recordList" tab="核销列表">
        <a-table :columns="columns" :dataSource="detail.recordList" bordered :pagination="false" size="small">
          <template #attachment="{text}">
            <file-preview :fileList="text"/>
          </template>
        </a-table>
      </a-tab-pane>
      <a-tab-pane key="invoiceList" tab="开票列表">
        <InvoiceList :invoiceList="detail.invoiceList"/>
      </a-tab-pane>
      <a-tab-pane key="operate" tab="操作记录">
        <DataTracerList
            :business-id="detail.receiveOrderId"
            :business-type="DATA_TRACER_BUSINESS_TYPE_ENUM.RECEIVE_ORDER.value"/>
      </a-tab-pane>
    </a-tabs>
    <CheckModal ref="checkRef" @refresh="getDetail"/>
    <VerificationModal ref="verificationRef" @refresh="getDetail"/>

  </a-card>
  <a-modal v-model:open="showContractAttachmentFlag" title="合同预览" :footer="null" width="500px">
    <a-card>
      <Upload :default-file-list="contractAttachmentList" listType="picture-card" :showUploadBtn="false" />
    </a-card>
  </a-modal>
  <MakeInvoiceModal ref="makeInvoiceRef" @refresh="getDetail"/>
</template>
<script setup>
import FilePreview from '/@/components/file-preview/index.vue';
import VerificationModal from './components/verification-modal.vue';
import CheckModal from './components/check-modal.vue';
import InvoiceList from './components/receive-invoice-list.vue';
import DataTracerList from '/@/components/data-tracer/index.vue';
import Upload from '/@/components/upload/index.vue';
import { ref, onMounted, reactive, computed } from 'vue';
import { useSpinStore } from '/@/store/modules/system/spin';
import { receiveOrderApi } from '/@/api/business/receive-order/receive-order-api';
import { useRoute, useRouter } from 'vue-router';
import { waybillColumns } from './receive-order-columns';
import { PATH_TYPE_ENUM } from '/@/constants/business/transport-route-const';
import { CHECK_STATUS_ENUM, INVOICE_STATUS_ENUM } from '/@/constants/business/receive-order-const';
import { DATA_TRACER_BUSINESS_TYPE_ENUM } from '/@/constants/support/data-tracer-const';
import _ from 'lodash';
import MakeInvoiceModal from '/@/views/business/invoice/components/make-invoice-modal.vue';
// ----------------------- 核销记录展示 start -----------------------
const columns = reactive([
  {
    title: '核销日期',
    dataIndex: 'verificationTime'
  },
  {
    title: '核销金额',
    dataIndex: 'verificationAmount'
  },
  {
    title: '流水单号',
    dataIndex: 'sequenceCode',
  },
  {
    title: '凭证',
    dataIndex: 'attachment',
    slots: { customRender: 'attachment' },
  },
  {
    title: '备注',
    dataIndex: 'remark'
  },
  {
    title: '创建人',
    dataIndex: 'createUserName'
  },
  {
    title: '创建时间',
    dataIndex: 'createTime'
  },
]);

// ----------------------- 核销记录展示 end -----------------------

// ----------------------- 获取详情 end -----------------------
const route = useRoute();
let detail = ref({
  recordList: [],
  invoiceList: []
});

let receiveOrderId = route.query.receiveOrderId;

async function getDetail () {
  if (!receiveOrderId) {
    return;
  }
  try {
    useSpinStore().show();
    const { data } = await receiveOrderApi.getDetail(receiveOrderId);
    detail.value = data;
  } catch (e) {
    console.log(e);
  } finally {
    useSpinStore().hide();
  }
}

const waybillReceiveColumns = computed(() => {
  let columns = _.cloneDeep(waybillColumns);
  let findIndex = columns.findIndex(e => e.title == '应收金额');
  if (findIndex != -1) {
    let headerColumns = columns.splice(0, findIndex);
    headerColumns.push({
      title: '本次应收',
      dataIndex: 'thisReceiveAmount',
      width: 100
    });
    return headerColumns.concat(columns);
  }
  console.log(columns)
  return columns;
});
// ----------------------- 应收对账 操作 start -----------------------
const checkRef = ref();

function confirmCheck () {
  checkRef.value.showModal(receiveOrderId);
}

// 核销
const verificationRef = ref();

function confirmVerification () {
  verificationRef.value.showModal(detail.value, receiveOrderId);
}

// ----------------- 跳转 --------------------
let router = useRouter();

function orderDetail (orderId) {
  router.push({ path: '/order/detail', query: { orderId } });
}

function waybillDetail(waybillId) {
  router.push({ path: '/waybill/waybill-detail', query: { waybillId } });
}

let showContractAttachmentFlag = ref(false);
let contractAttachmentList = ref([])

function showContractAttachmentModal(record) {
  showContractAttachmentFlag.value = true;
  contractAttachmentList.value = record.contractFile || [];
}

// ----------- 导出 start -----------
function makeInvoiceExportExcel () {
  receiveOrderApi.makeInvoiceExportExcel(detail.value.receiveOrderNumber, receiveOrderId);
}

// ----------- 导出 start -----------
function waybillExportExcel () {
  receiveOrderApi.waybillExportExcel(detail.value.receiveOrderNumber, receiveOrderId);
}

// 确认开票
const makeInvoiceRef = ref();
function confirmInvoice () {
  makeInvoiceRef.value.showModal(detail.value, []);
}

onMounted(() => {
  getDetail();
});
</script>
<style lang="less" scoped>
.waybill-code {
  display: flex;
  justify-content: space-between;
}
</style>

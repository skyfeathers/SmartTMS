<template>
  <a-card title="受票方信息">
    <template #extra>
      <a @click="showInvoiceModal">新增发票信息</a>
    </template>
    <div style="font-weight: bold;color: #FF0000;">
      提交核算并开票后，运单应付应收费用将不可更改，请谨慎操作！
    </div>
    <a-row :gutter="16" class="smart-margin-bottom10">
      <a-col span="10">
        <a-alert type="info" show-icon>
          <template #message>
            应收金额：¥ {{invoiceAmount}}元
            开票金额：¥ {{invoiceTotalAmount}}元
          </template>
        </a-alert>
      </a-col>
      <a-col span="12">
        <a-alert type="error" show-icon v-if="invoiceAmount != invoiceTotalAmount">
          <template #message>
            开票金额与应收金额不一致
          </template>
        </a-alert>
      </a-col>
    </a-row>
    <a-table
        :columns="columns"
        :dataSource="tableData"
        bordered
        :pagination="false"
        :scroll="{ x: 1400  ,y:200}"
        rowKey="orderId"
        size="small"
    >
      <template #invoiceName="{ record ,index}">
        <ShipperInvoiceSelect ref="invoiceSelectRef" v-model:value="record.shipperInvoiceId" :shipperId="shipperId"
                              @change="(invoiceId,invoice)=>changeShipperInvoice(invoiceId,invoice,index)"
                              :showDefaultFlag="false" style="width: 100%;"/>
      </template>
      <template #invoiceType="{ record }">
        <smart-enum-select v-model:value="record.invoiceType" placeholder="请选择类型"
                           enum-name="INVOICE_TYPE_ENUM" style="width:100%"/>
      </template>
      <template #invoiceKindType="{ record }">
        <smart-enum-select v-model:value="record.invoiceKindType" placeholder="请选择种类"
                           enum-name="INVOICE_KIND_TYPE_ENUM" style="width:100%"/>
      </template>
      <template #invoiceAmount="{ record }">
        <a-input-number v-model:value="record.invoiceAmount" :max="99999999.99" :min="0" :precision="2"
                        placeholder="请输入金额"
                        style="width:100%"/>
      </template>
      <template #action="{record,index}">
        <div class="smart-table-operate">
          <a-button @click="removeInvoice(index)" type="link" v-if="index != 0">移除</a-button>
        </div>
      </template>
    </a-table>
<!--    <a-button block type="dashed" @click="addData">
      <template #icon>
        <plus-outlined/>
      </template>
      新增受票方
    </a-button>-->
    <InvoiceOperateModal ref="invoiceRef" @onSubmit="submitInvoice"/>

  </a-card>
</template>
<script setup>
import SmartEnumSelect from '/@/components/smart-enum-select/index.vue';
import InvoiceOperateModal from '/@/components/shipper-invoice-operate-modal/index.vue';
import ShipperInvoiceSelect from '/@/components/shipper-invoice-select/index.vue';

import { onMounted, reactive, ref, computed } from 'vue';
import { SmartLoading } from '/@/components/smart-loading';
import { shipperApi } from '/@/api/business/shipper/shipper-api';
import { message } from 'ant-design-vue';
import _ from 'lodash';
import { Decimal } from 'decimal.js';


const props = defineProps({
  shipperId: {
    type: Number
  },
  // 应收金额
  invoiceAmount:{
    type:Number,
    default: 0
  }
});

let tableData = ref([]);

const invoiceTotalAmount = computed(() => {
  let amountList = tableData.value.map(e => e.invoiceAmount || 0);
  if (_.isEmpty(amountList)) {
    return 0;
  }
  let totalAmount = amountList.reduce((a, b) => {
    return Decimal(a).add(Decimal(b)).toNumber();
  });
  return totalAmount;
});

defineExpose({
  validateTableData,
  tableData
});
onMounted(()=>{
  tableData.value = [];
  addData();
})
// ---------------- 受票方列表 ----------------
let columns = reactive([
  {
    title: '发票抬头',
    dataIndex: 'invoiceName',
    slots: { customRender: 'invoiceName' }
  },
  {
    title: '发票类型',
    dataIndex: 'invoiceType',
    slots: { customRender: 'invoiceType' },
    width: 130
  },
  {
    title: '发票种类',
    dataIndex: 'invoiceKindType',
    slots: { customRender: 'invoiceKindType' },
    width: 130
  },
  {
    title: '开票金额',
    dataIndex: 'invoiceAmount',
    slots: { customRender: 'invoiceAmount' },
    width: 120
  },
  {
    title: '开户行号',
    dataIndex: 'invoiceBankNo',
    width: 120
  },
  {
    title: '纳税人识别号',
    dataIndex: 'invoiceNo',
    width: 120
  },
  {
    title: '开票地址',
    dataIndex: 'invoiceAddress',
    width: 140
  },
  {
    title: '开票银行',
    dataIndex: 'invoiceBankName',
    width: 140
  },
  // {
  //   title:'操作',
  //   dataIndex: 'action',
  //   slots: { customRender: 'action' },
  //   width: 50,
  //   fixed: 'right'
  // }
]);

function changeShipperInvoice (shipperInvoiceId, invoice, index) {
  tableData.value[index] = Object.assign(tableData.value[index], invoice || {});
}

const defaultForm = {
  shipperInvoiceId: null,
  invoiceType: null,
  invoiceKindType: null,
  invoiceAmount: 0
};

function addData () {
  tableData.value.push(_.cloneDeep(defaultForm));
}

function removeInvoice (index) {
  tableData.value.splice(index, 1);
}

async function validateTableData () {
  let validateFlag = true;
  if (_.isEmpty(tableData.value)) {
    validateFlag = false;
    message.error('请选择受票方');
  }
  if (validateFlag && tableData.value.some(e => !e.shipperInvoiceId)) {
    validateFlag = false;
    message.error('请选择受票方发票抬头');
  }
  if (validateFlag && tableData.value.some(e => !e.invoiceType)) {
    validateFlag = false;
    message.error('请选择受票方发票类型');
  }
  if (validateFlag && tableData.value.some(e => !e.invoiceKindType)) {
    validateFlag = false;
    message.error('请选择受票方发票种类');
  }
  if (validateFlag && tableData.value.some(e => !e.invoiceAmount)) {
    validateFlag = false;
    message.error('请输入受票方开票金额');
  }
  if (!validateFlag) {
    return Promise.reject();
  }
  return Promise.resolve();
}

// ---------------- 新增发票信息 ----------------
let invoiceRef = ref();

function showInvoiceModal () {
  invoiceRef.value.showModal();
}

let invoiceSelectRef = ref();

async function submitInvoice (params) {
  try {
    SmartLoading.show();
    params.shipperId = props.shipperId;
    await shipperApi.saveInvoice(params);
    message.success('保存成功');
    invoiceSelectRef.value.queryData()
  } catch (error) {
    console.log(error);
  } finally {
    SmartLoading.hide();
  }
}
</script>

<style lang="css" scoped>
</style>

<template>
  <a-table :columns="invoiceColumns" :dataSource="invoiceList" bordered :pagination="false" size="small"
           :scroll="{x:1600}">
    <template #invoiceStatus="{text}">
      {{ $smartEnumPlugin.getDescByValue('INVOICE_STATUS_ENUM', text) }}
    </template>
    <template #invoiceType="{text}">
      {{ $smartEnumPlugin.getDescByValue('INVOICE_TYPE_ENUM', text) }}
    </template>
    <template #invoiceKindType="{text}">
      {{ $smartEnumPlugin.getDescByValue('INVOICE_KIND_TYPE_ENUM', text) }}
    </template>
    <template #invoiceAttachment="{text}">
      <file-preview :fileList="text"/>
    </template>
  </a-table>
</template>
<script setup>
import FilePreview from '/@/components/file-preview/index.vue'
// ----------------------- 开票记录展示 start -----------------------
import { reactive } from 'vue';

const props = defineProps({
  invoiceList: {
    type: Array
  }
});

const invoiceColumns = reactive([
  {
    title: '开票金额',
    dataIndex: 'invoiceAmount'
  },
  {
    title: '类型',
    dataIndex: 'invoiceType',
    slots: { customRender: 'invoiceType' },
  },
  {
    title: '发票种类',
    dataIndex: 'invoiceKindType',
    slots: { customRender: 'invoiceKindType' },
  },
  {
    title: '发票抬头',
    dataIndex: 'invoiceName'
  },
  {
    title: '纳税人识别号',
    dataIndex: 'invoiceNo'
  },
  {
    title: '开票地址',
    dataIndex: 'invoiceAddress'
  },
  {
    title: '开户行号',
    dataIndex: 'invoiceBankNo'
  },
  {
    title: '开票银行',
    dataIndex: 'invoiceBankName'
  },
  {
    title: '开票状态',
    dataIndex: 'invoiceStatus',
    slots: { customRender: 'invoiceStatus' }
  },
  {
    title: '开票时间',
    dataIndex: 'invoiceTime'
  },
  {
    title: '费用发票号',
    dataIndex: 'invoiceNumber'
  },
  {
    title: '快递单号',
    dataIndex: 'courierNumber'
  },
  {
    title: '开票附件',
    dataIndex: 'invoiceAttachment',
    slots: { customRender: 'invoiceAttachment' },
    width: 100
  },
  {
    title: '开票人',
    dataIndex: 'invoiceUserName'
  }
]);
// ----------------------- 开票记录展示 end -----------------------
</script>

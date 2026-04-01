import { reactive } from 'vue';

export const receiveOrderColumns = [
  {
    title: '收款单号',
    dataIndex: 'receiveOrderNumber',
    width: 200,
    fixed: 'left'
  },
  {
    title: '货主简称',
    dataIndex: 'shortName',
    width: 150,
    ellipsis: true,
  },
  {
    title: '开票金额',
    dataIndex: 'invoiceAmount',
    width: 80
  },
  {
    title: '类型',
    dataIndex: 'invoiceType',
    width: 80
  },
  {
    title: '发票种类',
    dataIndex: 'invoiceKindType',
    width: 80
  },
  {
    title: '税点',
    dataIndex: 'taxPoint',
    width: 50
  },
  {
    title: '受票方发票抬头',
    dataIndex: 'invoiceName',
    width: 150,
    ellipsis: true,
  },
  {
    title: '纳税人识别号',
    dataIndex: 'invoiceNo',
    width: 150,
  },
  {
    title: '开票地址',
    dataIndex: 'invoiceAddress',
    width: 150,
    ellipsis: true,
  },
  {
    title: '开户行号',
    dataIndex: 'invoiceBankNo',
    width: 150,
  },
  {
    title: '开票银行',
    dataIndex: 'invoiceAddress',
    width: 150,
    ellipsis: true,
  },
  {
    title: '对账单',
    dataIndex: 'billAttachment',
    width: 150,
  },
];

export const invoiceColumns = [
  {
    title: '费用发票号',
    dataIndex: 'invoiceNumber',
    width: 150,
    ellipsis: true,
  },
  {
    title: '附件',
    dataIndex: 'invoiceAttachment',
    width: 150,
  },
  {
    title: '开票人',
    dataIndex: 'invoiceUserName',
    width: 100,
    ellipsis: true,
  },
];

export const createColumns = [
  {
    title: '创建人',
    dataIndex: 'createUserName',
    width: 100,
    ellipsis: true,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 160,
  },
];

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
  },
  {
    title: '纳税人识别号',
    dataIndex: 'invoiceNo',
  },
  {
    title: '开票地址',
    dataIndex: 'invoiceAddress',
  },
  {
    title: '开户行号',
    dataIndex: 'invoiceBankNo',
  },
  {
    title: '开票银行',
    dataIndex: 'invoiceAddress',
  },
  {
    title: '对账单',
    dataIndex: 'billAttachment',
  },
];

export const invoiceColumns = [
  {
    title: '费用发票号',
    dataIndex: 'invoiceNumber',
  },
  {
    title: '附件',
    dataIndex: 'invoiceAttachment',
  },
  {
    title: '开票人',
    dataIndex: 'invoiceUserName',
    width: 60
  },
];

export const createColumns = [
  {
    title: '创建人',
    dataIndex: 'createUserName',
    width: 60,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 160,
  },
];

// 应收核算的表格列

export const checkColumns = [
  {
    title: '收款单号',
    dataIndex: 'receiveOrderNumber',
    width: 150,
    fixed: 'left'
  },
  {
    title: '货主简称',
    dataIndex: 'shortName',
    ellipsis: true,
    width: 150,
  },
  {
    title: '货主全称',
    dataIndex: 'consignor',
    ellipsis: true,
    width: 150,
  },
  // {
  //   title: '运费总额',
  //   dataIndex: 'freight',
  //   width: 80
  // },
  // {
  //   title: '异常费用总额',
  //   dataIndex: 'freight',
  //   width: 110
  // },
  {
    title: '本次应收总额',
    dataIndex: 'totalAmount',
    width: 130
  },
  {
    title: '业务日期',
    dataIndex: 'businessDate',
    width: 100,
  },
  {
    title: '是否需要开票',
    dataIndex: 'makeInvoiceFlag',
    width: 110,
  },
  {
    title: '受票方抬头',
    dataIndex: 'invoiceName',
    width: 180,
  },
  {
    title: '开票状态',
    dataIndex: 'invoiceStatus',
    width: 80
  },
  {
    title: '税点',
    dataIndex: 'taxPoint',
    width: 80
  },
  {
    title: '应收对账备注',
    dataIndex: 'remark',
    width: 110,
  },
  {
    title: '核算附件',
    dataIndex: 'attachment',
  }
];

export const checkCreateColumns = [
  {
    title: '创建人',
    dataIndex: 'createUserName',
    width: 80,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 170,
  },];

export const cancelColumns = [{
  title: '作废备注',
  dataIndex: 'cancelRemark',
}];

// 应收详情的运单列表
export const waybillColumns = [
  {
    title: '运单号',
    dataIndex: 'waybillNumber',
    width: 160,
    fixed: 'left',
  },
  {
    title: '订单号',
    dataIndex: 'orderNo',
    width: 150,
    fixed: 'left',
  },
  {
    title: '箱号',
    dataIndex: 'containerNumber',
    ellipsis: true,
  },
  {
    title: '铅封号',
    dataIndex: 'leadSealNumber',
    ellipsis: true,
  },
  {
    title: '柜型',
    dataIndex: 'cabinetName',
    ellipsis: true,
  },
  {
    title: '车牌号',
    dataIndex: 'vehicleNumber',
    width: 100
  },
  {
    title: '税点',
    dataIndex:'taxPoint',
    width: 80
  },
  {
    title: '应收金额',
    dataIndex:'receiveAmount',
    width: 80
  },
  {
    title: '税金',
    dataIndex:'taxAmount',
    width: 80
  },
  {
    title: '应付金额',
    dataIndex:'payableAmount',
    width: 80
  },
  {
    title: '本单利润',
    dataIndex: 'profitAmount',
    width: 100
  },
  {
    title: '装箱地点',
    dataIndex: 'containerLocation',
    ellipsis: true,
  },
  {
    title: '装货地点',
    dataIndex: 'placingLocation',
    ellipsis: true,
  },
  {
    title: '卸货地点',
    dataIndex: 'unloadingLocation',
    ellipsis: true,
  },
  {
    title: '还箱地点',
    dataIndex: 'returnContainerLocation',
    ellipsis: true,
  },
  {
    title: '创建人',
    dataIndex: 'createUserName',
    width: 80,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 160,
  },
];

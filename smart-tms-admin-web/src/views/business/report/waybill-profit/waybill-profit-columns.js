export const headerColumns = [
  {
    title: '序号',
    width: 50,
    dataIndex: 'index',
    fixed: 'left'
  },
  {
    title: '客户简称',
    width: 150,
    dataIndex: 'shortName',
    fixed: 'left',
    ellipsis: true,
  },
  {
    title: '订单号',
    dataIndex: 'orderNo',
    width: 150,
  },
  {
    title: '运单号',
    dataIndex: 'waybillNumber',
    width: 150,
  },
  {
    title: '业务类型',
    dataIndex: 'containerBusinessTypeName',
    width: 80
  },
  {
    title: '运输方式',
    dataIndex: 'transportMode',
    width: 100
  },
  {
    title: '业务时间',
    dataIndex: 'businessDate',
    width: 90
  },
  {
    title: '装货时间',
    dataIndex: 'loadTime',
    width: 150
  },
  {
    title: '卸货时间',
    dataIndex: 'unloadTime',
    width: 150
  },
  {
    title:'付款状态',
    dataIndex:'payStatus',
    width: 90
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 170
  },
  {
    title: '提箱地',
    dataIndex: 'containerLocation',
    ellipsis: true,
  },
  {
    title: '装货地',
    dataIndex: 'placingLocation',
    ellipsis: true,
  },
  {
    title: '卸货地',
    dataIndex: 'unloadingLocation',
    ellipsis: true,
  },
  {
    title: '落箱地',
    dataIndex: 'returnContainerLocation',
    ellipsis: true,
  },
  {
    title: '柜型',
    dataIndex: 'cabinetName'
  },
  {
    title: '重量（吨）',
    dataIndex: 'totalWeight',
    width: 90
  },
  {
    title: '发货时间',
    dataIndex: 'deliverGoodsTime'
  },
  {
    title: '收货时间',
    dataIndex: 'receiveGoodsTime'
  },
  {
    title: '车牌号',
    dataIndex: 'vehicleNumber',
    width: 100
  },
  {
    title: '司机姓名',
    dataIndex: 'driverName',
    width: 80
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
    title: '订单备注',
    dataIndex: 'orderRemark'
  },
  {
    title: '应收总额',
    dataIndex: 'receiveAmount'
  },
  ];

export const footerColumns = [
  {
    title: '应付总额',
    dataIndex: 'payableAmount'
  },
  {
    title: '税金',
    dataIndex: 'taxAmount',
    width: 100
  },
  {
    title: '司机工资',
    dataIndex:'salaryAmount',
    width: 100
  },
  {
    title: '在途费用',
    dataIndex:'carCostAmount',
    width: 100
  },
  {
    title: '利润',
    dataIndex: 'profitAmount',
    width: 90
  },
  {
    title: '合同号',
    dataIndex: 'contractCode'
  },
  {
    title: '业务',
    dataIndex: 'shipperManagerName',
    width: 80
  },
  {
    title: '调度',
    dataIndex: 'scheduleName',
    width: 80
  },
  {
    title: '客服',
    dataIndex: 'orderCreateUserName',
    width: 80
  }
];

/*
 * @Description:
 * @Author: zhuoda
 * @Date: 2022-07-07
 * @LastEditTime: 2022-08-02
 * @LastEditors: zhuoda
 */
export const baseColumns = [
  {
    title: '付款单号',
    dataIndex: 'payOrderNumber',
    width: 200,
    fixed: 'left',
  },
  {
    title: '运单号',
    width: 200,
    dataIndex: 'waybillNumber',
  },
  {
    title: '货主',
    dataIndex: 'shipperName',
    width: 200
  },
  {
    title: '司机',
    dataIndex: 'driverId',
    width: 200
  },
  {
    title: '车辆',
    dataIndex: 'vehicleNumber',
    width: 200
  },
  {
    title: '车队',
    dataIndex: 'fleetId',
    width: 200
  },
  {
    title: '箱号',
    dataIndex: 'containerNumber',
    width: 200
  },
  {
    title: '审核状态',
    dataIndex: 'auditStatus',
    width: 80
  },
  {
    title: '当前审批人',
    dataIndex: 'currentAuditor',
    width: 120,
    ellipsis: true,
  },
  {
    title: '付款状态',
    width: 80,
    dataIndex: 'payOrderStatus',
  },
  {
    title: '应付金额',
    width: 80,
    dataIndex: 'payableTotalAmount',
  },
  {
    title: '已付金额',
    width: 80,
    dataIndex: 'paidTotalAmount',
  },
  {
    title: '利润',
    width: 80,
    dataIndex: 'profitAmount',
  },
  {
    title: '回单',
    width: 100,
    dataIndex: 'receiptAttachment'
  },
  // {
  //   title: '派车单',
  //   width: 100,
  //   dataIndex: 'truckOrderAttachment'
  // },
  {
    title: '所属企业',
    dataIndex: 'enterpriseName',
    ellipsis: true,
    width: 160
  },
  {
    title: '备注',
    dataIndex: 'remark',
    width: 450
  },
  {
    title: '创建人',
    width: 80,
    dataIndex: 'createUserName',
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 160
  },
];

export const payColumns = [
  {
    title: '收款账户',
    dataIndex: 'receiveBankAccount',
    width: 300
  },
  {
    title: '付款账户',
    dataIndex: 'payBankAccount',
    width: 300
  },
  {
    title: '支付人',
    width: 80,
    dataIndex: 'payUserName',
  },
  {
    title: '支付时间',
    dataIndex: 'payTime',
    width: 180
  },
];

export const verificationColumns = [
  {
    title: '核销人',
    width: 80,
    dataIndex: 'verificationUserName',
  },
  {
    title: '核销时间',
    dataIndex: 'verificationTime',
    width: 160
  },
];

export const actionColumns = [
  {
    title: '操作',
    dataIndex: 'action',
    width: 50,
    fixed: 'right'
  }
];

/*
 * @Description:
 * @Author: zhuoda
 * @Date: 2022-07-07
 * @LastEditTime: 2022-08-21
 * @LastEditors: zhuoda
 */

import useDragTable from '/@/hook/use-drag-table/index';
import { TABLE_ID_CONST } from '/@/constants/table-id-const';
export const { columnsData:columns, tableWidth } = useDragTable([
  {
    title: '车牌号',
    dataIndex: 'vehicleNumber',
    fixed: 'left',
    width: 150,
    filterOptions:{
        type:'input',
    }
  },
  {
    title: '审核',
    dataIndex: 'auditStatus',
    width: 150,
    filterOptions:{
        type:'enum-select',
        enumName:'AUDIT_STATUS_ENUM'
    }
  },
  {
    title: '状态',
    dataIndex: 'disabledFlag',
    width: 150,
    filterOptions:{
        type:'disabled-select'
    }
  },
  {
    title: '车辆类型',
    dataIndex: 'vehicleType',
    width: 150,
    filterOptions:{
        type:'dict-select',
        keyCode:'vehicleType',
    }
  },
  {
    title: '经营方式',
    dataIndex: 'businessMode',
    width: 150,
    filterOptions:{
        type:'enum-select',
        enumName:'VEHICLE_BUSINESS_MODE_ENUM'
    }
  },
  // {
  //   title: '所属人性质',
  //   dataIndex: 'ownerType',
  // },
  {
    title: '关联挂车',
    dataIndex: 'bracketNo',
    width: 150,
    filterOptions:{
        type:'input',
        key:'bracketNo'
    }
  },
  {
    title: '关联司机',
    dataIndex: 'driverList',
    width: 150
  },
  // {
  //   title: '行驶证到期日期',
  //   dataIndex: 'expireDate',
  // },
  {
    title: '道路运输证号',
    dataIndex: 'roadTransportCertificateNumber',
    width: 150,
    filterOptions:{
        type:'input',
    }
  },
  {
    title: '道运证有效期结束日期',
    dataIndex: 'roadTransportCertificateExpireDate',
    width: 200,
    filterOptions:{
      type:'date-range',
      ranges:true
    }
  },
  {
    title: '车审时间',
    dataIndex: 'vehicleAuditExpireDate',
    width: 200,
    filterOptions:{
      type:'date-range',
      ranges:true
    }
  },
  {
    title: '保养',
    dataIndex: 'maintenance',
    width: 200
  },
  // {
  //   title: '挂靠企业',
  //   dataIndex: 'relyEnterpriseName',
  //   width: 140,
  //   ellipsis: true,
  // },
  {
    title: '挂靠到期时间',
    dataIndex: 'relyEnterpriseExpireDate',
    width: 200,
    filterOptions:{
      type:'date-range',
      ranges:true
    }
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 200,
    filterOptions:{
      type:'date-range',
      ranges:true
    }
  },
  {
    title: '备注',
    dataIndex: 'remark',
    width: 150,
    ellipsis: true,
  },
  {
    title: '操作',
    dataIndex: 'operate',
    fixed: 'right',
    width: 50,
    filterOptions:{
      type:'submit',
      btnType:'link'
    }
  },
],TABLE_ID_CONST.VEHICLE);

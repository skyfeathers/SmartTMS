/*
 * @Description:
 * @Author: zhuoda
 * @Date: 2022-07-07
 * @LastEditTime: 2022-08-02
 * @LastEditors: zhuoda
 */
import ellipsisRender from '/@/utils/ellipsis-render/index.js'
import { SYSTEM_CONFIG_KEY_ENUM } from '/@/constants/system/system-config-const';
export const columns = [
  {
    title: '运单号',
    dataIndex: 'waybillNumber',
    width: 190,
    fixed: 'left',
    filterOptions:{
        type:'input',
    }
  },
  {
    title: '订单号',
    dataIndex: 'orderNo',
    width: 190,
    fixed: 'left',
    filterOptions:{
        type:'input',
    }
  },
  {
    title: '业务时间',
    dataIndex: 'businessDate',
    width: 200,
    filterOptions:{
      type:'month'
  }
  },
  {
    title: '货主',
    dataIndex: 'shortName',
    ellipsis: true,
    width: 150,
    customRender:ellipsisRender,
    filterOptions:{
        type:'shipper-select',
        key:'shipperId'
    }
  },
  {
    title: '司机',
    dataIndex: 'driverName',
    width: 150,
    filterOptions:{
        type:'driver-select',
        key:'driverIdList',
        multiple:true,
    }
  },
  {
    title: '车辆',
    dataIndex:'vehicleNumber',
    width: 150,
    filterOptions:{
        type:'vehicle-select',
        multiple:true,
        key:'vehicleIdList'
    }
  },
  {
    title: '车队',
    dataIndex: 'fleetName',
    width: 150,
    filterOptions:{
      type:'fleet-select',
      key:'fleetId'
  }
  },
  {
    title: '运单状态',
    dataIndex: 'waybillStatus',
    width: 150,
    filterOptions:{
      type:'enum-select',
      enumName:'WAYBILL_STATUS_ENUM',
      key:'waybillStatusList',
      multiple:true
    }
  },
  {
    title: '箱号',
    dataIndex: 'containerNumber',
    width: 150,
    ellipsis: true,
    filterOptions:{
        type:'input',
    }
  },
  {
    title: '铅封号',
    dataIndex: 'leadSealNumber',
    width: 150,
    ellipsis: true,
    filterOptions:{
        type:'input',
    }
  },
  {
    title: '柜型',
    dataIndex: 'cabinetName',
    width: 150,
    ellipsis: true,
    filterOptions:{
        type:'cabinet-select',
        key:'cabinetId'
    }
  },
  {
    title: '结算方式',
    dataIndex: 'settleMode',
    width: 150,
    filterOptions:{
        type:'enum-select',
        enumName:'SELECT_SETTLE_MODE_ENUM'
    }
  },
  {
    title: '运输方式',
    dataIndex: 'transportMode',
    width: 150,
    filterOptions:{
      type:'enum-select',
      enumName:'WAYBILL_TRANSPORT_MODE_ENUM'
  }
  },
  {
    title: '应付现金',
    dataIndex:'payableCashAmount',
    width: 150
  },
  {
    title: '应付油卡',
    dataIndex:'payableOilCardAmount',
    width: 150
  },
  {
    title: '合计应付',
    dataIndex:'payableAmount',
    width: 150
  },
  {
    title: '合计应收',
    dataIndex:'receiveAmount',
    width: 150
  },
  {
    title: '税点',
    dataIndex:'taxPoint',
    width: 150
  },
  {
    title: '税金',
    dataIndex:'taxAmount',
    width: 150
  },
  {
    title: '司机工资',
    dataIndex:'salaryAmount',
    width: 150
  },
  {
    title: '在途费用',
    dataIndex:'carCostAmount',
    width: 150
  },
  {
    title: '利润',
    dataIndex:'profitAmount',
    width: 150
  },
  {
    title: '装货磅重',
    dataIndex:'loadingPoundListQuantity',
    width: 170
  },
  {
    title: '卸货磅重',
    dataIndex:'unloadingPoundListQuantity',
    width: 170
  },
  
  {
    title: '回单',
    width: 150,
    dataIndex: 'receiptAttachment'
  },
  {
    title: '装箱地点',
    dataIndex: 'containerLocation',
    customRender:ellipsisRender,
    ellipsis: true,
    width: 150,
    filterOptions:{
        type:'input',
    }
  },
  {
    title: '装货地点',
    dataIndex: 'placingLocation',
    customRender:ellipsisRender,
    ellipsis: true,
    width: 150,
    filterOptions:{
      type:'input',
  }
  },
  {
    title: '卸货地点',
    dataIndex: 'unloadingLocation',
    customRender:ellipsisRender,
    ellipsis: true,
    width: 150,
    filterOptions:{
      type:'input',
  }
  },
  {
    title: '还箱地点',
    dataIndex: 'returnContainerLocation',
    customRender:ellipsisRender,
    ellipsis: true,
    width: 150,
    filterOptions:{
      type:'input',
    }
  },
  {
    title: '调度',
    dataIndex: 'scheduleName',
    width: 150,
    filterOptions:{
      type:'role-employee-select',
      systemConfigKey:SYSTEM_CONFIG_KEY_ENUM.SCHEDULE_ROLE_CODE.value,
      key:'scheduleIdList',
      multiple:true,
  }
  },
  {
    title: '销售',
    dataIndex: 'orderManagerName',
    width: 150,
    filterOptions:{
        type:'role-employee-select',
        systemConfigKey:SYSTEM_CONFIG_KEY_ENUM.SALES_ROLE_CODE.value,
        multiple:true,
        key:'managerIdList'
    }
  },
  {
    title: '客服',
    dataIndex: 'orderCreateUserName',
    width: 150,
    filterOptions:{
      type:'role-employee-select',
      systemConfigKey:SYSTEM_CONFIG_KEY_ENUM.CUSTOMER_SERVICE_ROLE_CODE.value,
      key:'customerIdList',
      multiple:true,
  }
  },
  {
    title: '创建人',
    dataIndex: 'createUserName',
    width: 150,
    filterOptions:{
        type:'role-select',
        key:'createUserRoleId'
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
];

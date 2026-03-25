/*
 * @Description:
 * @Author: zhuoda
 * @Date: 2022-08-02
 * @LastEditTime: 2022-08-02
 * @LastEditors: zhuoda
 */
import {reactive, ref} from 'vue';
import { TABLE_ID_CONST } from '/@/constants/table-id-const';
import useDragTable from '/@/hook/use-drag-table/index';
import { SYSTEM_CONFIG_KEY_ENUM } from '/@/constants/system/system-config-const';
import ellipsisRender from '/@/utils/ellipsis-render/index.js'
export const {columnsData:columns,tableWidth} = useDragTable([
  {
    title: '订单号',
    dataIndex: 'orderNo',
    width: 180,
    fixed: 'left',
    filterOptions:{
        type:'input',
    }
  },
  {
    title: '订单状态',
    dataIndex: 'status',
    width: 150,
    filterOptions:{
        type:'enum-select',
        enumName:'ORDER_STATUS_ENUM'
    }
  },
  {
    title: '调度员',
    dataIndex: 'scheduleName',
    width: 150,
    filterOptions:{
        type:'role-employee-select',
        systemConfigKey:SYSTEM_CONFIG_KEY_ENUM.SCHEDULE_ROLE_CODE.value,
        key:'scheduleId'
    }
  },
  {
    title: '销售',
    dataIndex: 'managerName',
    width: 150,
    filterOptions:{
        type:'role-employee-select',
        systemConfigKey:SYSTEM_CONFIG_KEY_ENUM.SALES_ROLE_CODE.value,
        multiple:true,
        key:'managerIdList'
    }
  },
  {
    title: '货主简称',
    dataIndex: 'shortName',
    width: 150,
    ellipsis: true,
    filterOptions:{
      type:'shipper-select',
      key:'shipperId'
    }
  },
  {
    title: '货主全称',
    dataIndex: 'consignor',
    ellipsis: true,
    customRender:ellipsisRender,
    width: 150,
    filterOptions:{
      type:'input',
    }
  },
  {
    title: '货物(名称 总量/剩余量)',
    dataIndex: 'goodsList',
    ellipsis: true,
    width: 200,
  },
  {
    title: '是否分段',
    dataIndex: 'splitTransportFlag',
    width: 150,
    filterOptions:{
        type:'boolean-select',
    }
  },
  {
    title: '装货时间',
    dataIndex: 'loadTime',
    width: 200,
    filterOptions:{
        type:'date-range',
        ranges:true
    }
  },
  {
    title: '卸货时间',
    dataIndex: 'unloadTime',
    width: 200,
    filterOptions:{
        type:'date-range',
        ranges:true
    }
  },
  {
    title: '最迟提箱时间',
    dataIndex: 'latestPackingTime',
    width: 200,
    filterOptions:{
        type:'date-range',
        ranges:true
    }
  },
  {
    title: '所属区域',
    dataIndex: 'areaDesc',
    width: 150,
    filterOptions:{
        type:'dict-select',
        keyCode:'AREA',
        key:'areaId'
    }
  },
  {
    title: '运输类型',
    dataIndex: 'businessTypeCode',
    width: 150,
    filterOptions:{
        type:'enum-select',
        enumName:'TRANSPORTATION_TYPE_ENUM',
    }
  },
  {
    title: '业务类型',
    dataIndex: 'containerBusinessTypeName',
    width: 150,
    filterOptions:{
      type:'business-type-select',
      key:'containerBusinessTypeId'
    }
  },
  {
    title: '柜型',
    dataIndex: 'cabinetName',
    width: 150,
    filterOptions:{
      type:'cabinet-select',
      key:'cabinetId'
    }
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
    title: '创建人',
    width: 150,
    dataIndex: 'createUserName',
    filterOptions:{
        type:'input',
    }
  },
  {
    title: '创建时间',
    width: 200,
    dataIndex: 'createTime',
    filterOptions:{
        type:'date-range',
        ranges:true
    }
  },
],TABLE_ID_CONST.ORDER);

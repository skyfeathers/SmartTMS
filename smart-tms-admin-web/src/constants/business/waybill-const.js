/*
 * @Description:
 * @Author: zhuoda
 * @Date: 2022-07-05
 * @LastEditTime: 2022-08-02
 * @LastEditors: zhuoda
 */

/**
 * 运单状态
 */
export const WAYBILL_STATUS_ENUM = {
  WAIT_RECEIVE: {
    value: 10,
    desc: '待接单',
    color: 'warning',
  },
  TRANSPORT: {
    value: 20,
    desc: '运输中',
    color: 'success',
  },
  COMPLETE: {
    value: 30,
    desc: '运输完成',
    color: 'success',
  },

  CANCEL: {
    value: 50,
    desc: '作废',
    color: 'error',
  },
};

/**
 * 凭证类型
 */
export const WAYBILL_VOUCHER_TYPE_ENUM = {
  LOAD: {
    value: 1,
    desc: '装货',
  },
  UNLOAD: {
    value: 2,
    desc: '卸货',
  },
  LOAD_CONTAINER: {
    value: 101,
    desc: '装箱',
  },
  UNLOAD_CONTAINER: {
    value: 102,
    desc: '卸箱',
  },
};

// 结算对象
export const SETTLE_TYPE_ENUM = {
  DRIVER: {
    value: 10,
    desc: '司机',
  },
  FLEET: {
    value: 20,
    desc: '车队',
  },
};

// 结算方式
export const SETTLE_MODE_ENUM = {
  ARRIVE_PAY: {
    value: 10,
    desc: '到付',
  },
  MONTH_PAY: {
    value: 40,
    desc: '到付'
  },
  MONTH_SETTLE: {
    value: 20,
    desc: '挂靠车月结',
  },
  SELF_VEHICLE_SETTLE: {
    value: 30,
    desc: '自有车结算'
  },
  ARRIVE_MONTH_PAY: {
    value: 50,
    desc: '外调车月结'
  }
};

/**
 * 结算方式
 * 这个的存在单纯是为了下拉框不会出现两个到付
 */
export const SELECT_SETTLE_MODE_ENUM = {
  ARRIVE_PAY: {
    value: 10,
    desc: '到付',
  },
  MONTH_SETTLE: {
    value: 20,
    desc: '挂靠车月结',
  },
  SELF_VEHICLE_SETTLE: {
    value: 30,
    desc: '自有车结算'
  },
  ARRIVE_MONTH_PAY: {
    value: 50,
    desc: '外调车月结'
  }
};

// 运输方式
export const  WAYBILL_TRANSPORT_MODE_ENUM =
    {
      LONG_DISTANCE:{
        value:10,
        desc:'长途'
      },
      SHORT_BARGE_INNER:{
        value:20,
        desc:'短驳（内部）'
      },
      SHORT_BARGE_OUTSIDE:{
        value:30,
        desc:'短驳（外部）'
      },
      ADDITIONAL:{
        value:40,
        desc:'补单'
      }
    }
export const
WAYBILL_EXCEPTION_TYPE_ENUM =
    {
      LOAD_EXCEPTION:{
        value:10,
        desc:'装货地点位置异常'
      },
      UNLOAD_EXCEPTION:{
        value:20,
        desc:'卸货地点位置异常'
      }
    }

export default {
  WAYBILL_STATUS_ENUM,
  WAYBILL_VOUCHER_TYPE_ENUM,
  SETTLE_TYPE_ENUM,
  SETTLE_MODE_ENUM,
  SELECT_SETTLE_MODE_ENUM,
  WAYBILL_TRANSPORT_MODE_ENUM,
  WAYBILL_EXCEPTION_TYPE_ENUM,
};

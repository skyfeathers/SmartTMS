/*
 * @Description: file content
 * @Author: yandy
 * @Date: 2022-07-13 11:33:36
 * @LastEditors: zhuoda
 * @LastEditTime: 2022-08-02
 */
// 审核状态
export const FLOW_AUDIT_STATUS_ENUM = {
  WAIT: {
    value: 1,
    desc: '待审核',
    color: 'warning',
  },
  PROCESSING: {
    value: 2,
    desc: '审核中',
    color: 'warning',
  },
  PASS: {
    value: 3,
    desc: '审核通过',
    color: 'success',
  },
  REJECT: {
    value: 4,
    desc: '审核驳回',
    color: 'error',
  },
  ALREADY_CC: {
    value: 5,
    desc: '已抄送',
    color: 'default',
  },
  CANCEL: {
    value: 9,
    desc: '审核撤销',
    color: 'default',
  },
};
// 流程类型
export const FLOW_TYPE_ENUM = {
  WAY_BILL_AUDIT: {
    value: 1,
    desc: '运单调度审核',
  },
  OIL_CARD_AUDIT: {
    value: 2,
    desc: '油卡充值审核',
  },
  PAY_AUDIT: {
    value: 3,
    desc: '付款审核',
  },
  RECEIVE_AUDIT: {
    value: 4,
    desc: '收款审核',
  },
};

// 查询类型
export const FLOW_INSTANCE_QUERY_TYPE_ENUM = {
  MY_HANDLE: {
    value: 1,
    desc: '待我处理',
  },
  MY_RECEIVED: {
    value: 3,
    desc: '我收到的',
  },
  MY_INITIATE: {
    value: 2,
    desc: '我发起的',
  },
};

// 任务类型
export const FLOW_TASK_TYPE_ENUM = {
  START: {
    value: 1,
    desc: '发起',
    color: 'processing',
  },
  APPROVE: {
    value: 2,
    desc: '审核',
    color: 'warning',
  },
  GATEWAY: {
    value: 3,
    desc: '条件网关',
    color: 'warning',
  },
  CC: {
    value: 4,
    desc: '抄送',
    color: 'default',
  },
};
// 任务类型
export const FLOW_TASK_HANDLER_TYPE_ENUM = {
  ALL: {
    value: 1,
    desc: '所有人',
  },
  ASSIGN_USER: {
    value: 2,
    desc: '指定人员',
  },
  ASSIGN_ROLE: {
    value: 3,
    desc: '指定角色',
  },
  ASSIGN_DEPARTMENT: {
    value: 4,
    desc: '指定部门',
  },
  INITIATOR_MANAGER: {
    value: 5,
    desc: '发起人部门主管',
  },
  INITIATOR: {
    value: 6,
    desc: '发起人自己',
  },
  CUSTOM_SERVICE: {
    value: 20,
    desc: '自定义服务',
  },
};

export default {
  FLOW_AUDIT_STATUS_ENUM,
  FLOW_TYPE_ENUM,
  FLOW_TASK_HANDLER_TYPE_ENUM,
  FLOW_INSTANCE_QUERY_TYPE_ENUM,
  FLOW_TASK_TYPE_ENUM,
};

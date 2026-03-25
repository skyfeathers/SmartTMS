/*
 * @Description: file content
 * @Author: yandy
 * @Date: 2022-07-13 11:29:28
 * @LastEditors:
 * @LastEditTime: 2022-07-13 11:29:28
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const flowApi = {
  // 审批实例-审核 by yandy
  auditFlow(param) {
    return postRequest('/flow/instance/audit', param);
  },
  // 审批实例-审核 by yandy
  batchAuditFlow(param) {
    return postRequest('/flow/instance/batchAudit', param);
  },
  // 审批实例-评论 by lihaifan
  submitComment(param) {
    return postRequest('/flow/instance/comment', param);
  },
  // 审批实例-撤销 by yandy
  cancelFlow(instanceId) {
    return getRequest(`/flow/instance/cancel/${instanceId}`);
  },
  // 审批实例-分页查询 by yandy
  queryFlowList(param) {
    return postRequest('/flow/instance/query', param);
  },
  // 审批实例-当前处理人id集合 by yandy
  handlerFlowTask(instanceId) {
    return getRequest(`/flow/instance/task/handler/${instanceId}`);
  },
  // 审批实例-任务处理流程信息 by yandy
  getFlowTaskList(instanceId) {
    return getRequest(`/flow/instance/task/list/${instanceId}`);
  },
  // 审批实例-查询待我审核的总数 by yandy
  getWaitAuditNum() {
    return getRequest(`/flow/instance/wait/audit/num`);
  },
  // 审批实例-获取当前流程所在的任务节点 by yandy
  getCurrentTaskInstance(instanceId) {
    return getRequest(`/flow/instance/current/task/${instanceId}`);
  },
  getFlowList() {
    return getRequest(`/flow/list`);
  },
  getFlowDetail(flowId, enterpriseId) {
    return getRequest(`/flow/detail/${flowId}/${enterpriseId}`);
  },
  getFlowListener(){
    return getRequest(`/flow/listener/list`);
  },
  flowConfig(param){
    return postRequest(`/flow/task/config`, param);
  }
};

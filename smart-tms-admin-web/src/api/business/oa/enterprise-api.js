/*
 * @Description: file content
 * @Author: yandy
 * @Date: 2022-07-07 09:51:43
 * @LastEditors:
 * @LastEditTime: 2022-07-07 09:51:43
*/
import { postRequest, getRequest } from '/@/lib/axios';

export const enterpriseApi = {

    // 新建企业 @author lihaifan
    create: (param) => {
        return postRequest('/oa/enterprise/create', param);
    },

    // 删除企业 @author lihaifan
    delete: (enterpriseId) => {
        return getRequest(`/oa/enterprise/delete/${enterpriseId}`);
    },

    // 初始企业账号 @author lihaifan
    initEnterpriseAccount: (enterpriseId) => {
        return getRequest(`/oa/enterprise/initAccount/${enterpriseId}`);
    },

    // 查询企业详情 @author lihaifan
    detail: (enterpriseId) => {
        return getRequest(`/oa/enterprise/get/${enterpriseId}`);
    },

    // 分页查询企业模块 @author lihaifan
    pageQuery: (param) => {
        return postRequest('/oa/enterprise/page/query', param);
    },

    //企业列表查询 含数据范围 @author lihaifan
    queryList: (type) => {
      let query = '';
      if (type) {
        query = `?type=${type}`;
      }
      return getRequest(`/oa/enterprise/query/list${query}`);
    },

    // 查询网络货运公司 @author lihaifan
    queryNftList: () => {
      return getRequest('/oa/enterprise/nft/query');
    },

    // 编辑企业 @author lihaifan
    update: (param) => {
        return postRequest('/oa/enterprise/update', param);
    },
    // 编辑企业站点信息 @author lihaifan
    updateWebsiteDesc: (param) => {
        return postRequest('/oa/enterprise/updateWebsiteDesc', param);
    },
    // 编辑企业域名 @author lihaifan
    updateDomainName: (param) => {
        return postRequest('/oa/enterprise/updateDomainName', param);
    },
    // 员工List @author yandy
    employeeList: (enterpriseId) => {
        let query = '';
        if (enterpriseId) {
            query = `?enterpriseId=${enterpriseId}`;
        }
        return getRequest(`/oa/enterprise/employee/list${query}`);
    },
    // 添加员工 @author yandy
    addEmployee: (param) => {
        return postRequest('/oa/enterprise/add/employee', param);
    },

    // 删除员工 @author yandy
    deleteEmployee: (param) => {
        return postRequest('/oa/enterprise/delete/employee', param);
    },

    // 根据货主以及登录人查询企业列表 @author lidoudou
    queryByShipperId: (shipperId) => {
      return getRequest(`/oa/enterprise/shipper/list/${shipperId}`);
    },

    saveDingDingConfig: (params) => {
      return postRequest('/dingtalk/config/save', params);
    },
    getDingDingConfig: (enterpriseId) => {
      return getRequest(`/dingtalk/config/${enterpriseId}`)
    }

};

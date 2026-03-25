/*
 * @Description: file content
 * @Author: yandy
 * @Date: 2022-07-07 09:51:43
 * @LastEditors:
 * @LastEditTime: 2022-07-07 09:51:43
*/
import { postRequest, getRequest } from '/@/lib/axios';

export const bankApi = {

    // 新建银行信息 @author lihaifan
    create: (param) => {
        return postRequest('/oa/bank/create', param);
    },

    // 删除银行信息 @author lihaifan
    delete: (bankId) => {
        return getRequest(`/oa/bank/delete/${bankId}`);
    },

    // 查询银行信息详情 @author lihaifan
    detail: (bankId) => {
        return getRequest(`/oa/bank/get/${bankId}`);
    },

    // 分页查询银行信息 @author lihaifan
    pageQuery: (param) => {
        return postRequest('/oa/bank/page/query', param);
    },

    // 编辑银行信息 @author lihaifan
    update: (param) => {
        return postRequest('/oa/bank/update', param);
    },

    // 根据企业ID查询不分页的银行列表 by lidoudou
    queryList:()=>{
      return getRequest(`/oa/bank/query/list`);
    }
};

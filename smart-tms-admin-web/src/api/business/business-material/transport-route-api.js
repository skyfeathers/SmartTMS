/*
 * @Description: file content
 * @Author: yandy
 * @Date: 2022-07-07 09:51:43
 * @LastEditors:
 * @LastEditTime: 2022-07-07 09:51:43
*/
import { postRequest, getRequest } from '/@/lib/axios';

export const transportRouteApi = {
    // 新建运输路线 @author lidoudou
    create: (param) => {
        return postRequest('/transport/route/create', param);
    },

    // 删除运输路线 @author lidoudou
    delete: (transportRouteId) => {
        return getRequest(`/transport/route/delete/${transportRouteId}`);
    },

    // 查询运输路线详情 @author lidoudou
    detail: (transportRouteId) => {
        return getRequest(`/transport/route/get/${transportRouteId}`);
    },

    // 分页查询运输路线模块 @author lidoudou
    pageQuery: (param) => {
        return postRequest('/transport/route/page/query', param);
    },

    // 编辑运输路线 @author lidoudou
    update: (param) => {
        return postRequest('/transport/route/update', param);
    },

    // 查询运输路线不分页列表 @author lidoudou
    queryList: (transportRouteType) => {
      return getRequest(`/transport/route/query/list/${transportRouteType}`);
    }
};

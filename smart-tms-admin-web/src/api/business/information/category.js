/*
 * @Description: 业务资料-分类管理
 * @version: 
 * @Author: 李云飞 qq:23983208
 * @Date: 2022-07-16 10:44:28
 * @LastEditors: 李云飞 qq:23983208
 * @LastEditTime: 2022-07-16 10:46:48
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const informationCategoryApi = {

  // 查询类目层级树 by listen
  queryCategoryTree (params) {
    return postRequest('/information/category/tree', params)
  }

  /*
POST /information/category/add
添加类目 by listen

GET /information/category/del/{categoryId}
删除类目 by listen


POST /information/category/update
更新类目 by listen

GET /information/category/{categoryId}
查询类目详情 by listen
  */
}
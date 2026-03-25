/*
 * @Description: 公告信息、企业动态
 * @version:
 * @Author: 李云飞 qq:23983208
 * @Date: 2022-07-16 10:34:36
 * @LastEditors: 李云飞 qq:23983208
 * @LastEditTime: 2022-07-18 15:54:41
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const newsApi = {

  // 资讯-分页查询 @listen
  queryNewsList (params) {
    return postRequest('/oa/news/manage/query', params)
  },

  // 资讯-添加 @listen
  addNews (params) {
    return postRequest('/oa/news/manage/add', params)
  },

  // 资讯-更新 @listen
  updateNews (params) {
    return postRequest('/oa/news/manage/update', params)
  },

  // 资讯-查询详情 @listen
  getNewsDetail (newsId) {
    return getRequest(`/oa/news/manage/${newsId}`)
  },

  // 资讯-删除 @listen
  delNews (newsId) {
    return getRequest(`/oa/news/manage/del/${newsId}`)
  },

  // 资讯-管理-浏览记录-分页查询 @listen
  queryNewsRecordList (params) {
    return postRequest('/oa/news/manage/view/record/query', params)
  },

  // 资讯-浏览-分页查询 @listen
  newsViewQuery (params) {
    return postRequest('/oa/news/view/query', params)
  },

  // 资讯-浏览-详情 @listen
  newsViewDetail (newsId) {
    return getRequest(`/oa/news/view/${newsId}`)
  }
}

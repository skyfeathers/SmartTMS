/*
 * @Description:表格列
 * @Author: zhuoda
 * @Date: 2021-08-26
 * @LastEditTime: 2022-06-20
 * @LastEditors: zhuoda
 */

import { reactive } from 'vue';
export const columns = reactive([
  {
    title: '名称',
    dataIndex: 'menuName',
    key: 'ID',
    width: 200,
  },
  {
    title: '类型',
    dataIndex: 'menuType',
    width: 80,
  },
  {
    title: '图标',
    dataIndex: 'icon',
    width: 50,
  },
  {
    title: '路径',
    dataIndex: 'path',
  },
  {
    title: '组件',
    dataIndex: 'component',
  },
  {
    title: '后端权限',
    dataIndex: 'apiPerms',
    ellipsis: true,
  },
  {
    title: '前端权限',
    dataIndex: 'permsIdentifier',
    ellipsis: true,
  },
  {
    title: '平台',
    dataIndex: 'platformFlag',
    width: 45,
  },
  {
    title: '外链',
    dataIndex: 'frameFlag',
    width: 45,
  },
  {
    title: '缓存',
    dataIndex: 'cacheFlag',
    width: 45,
  },
  {
    title: '禁用',
    dataIndex: 'disabledFlag',
    width: 45,
  },
  {
    title: '顺序',
    dataIndex: 'sort',
    width: 45,
  },
  {
    title: '操作',
    dataIndex: 'operate',
    width: 100,
  },
]);

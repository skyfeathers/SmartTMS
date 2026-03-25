/*
 * @Description: 帮助文档
 * @Author: zhuoda
 * @Date: 2021-08-03
 * @LastEditTime: 2022-08-25
 * @LastEditors: zhuoda
 */

import SmartHelpDocLayout from '/@/layout/smart-help-doc-layout.vue';

export const helpDocRouters = [
  {
    path: '/help-doc',
    name: 'HelpDoc',
    component: SmartHelpDocLayout,
    meta: {
      title: '帮助文档',
      hideInMenu: true,
    },
    children: [
      {
        path: '/help-doc/detail',
        component: () => import('/@/views/support/help-doc/user-view/help-doc-user-view.vue'),
      },
    ],
  },
];

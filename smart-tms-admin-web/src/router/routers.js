/*
 * @Description:
 * @Author: zhuoda
 * @Date: 2021-11-05
 * @LastEditTime: 2022-08-25
 * @LastEditors: zhuoda
 */
import { homeRouters } from './system/home';
import { loginRouters } from './system/login';
import { helpDocRouters } from './support/help-doc';
//import { statisticRouters } from './system/statistic'

export const routerArray = [...loginRouters, ...homeRouters, ...helpDocRouters];

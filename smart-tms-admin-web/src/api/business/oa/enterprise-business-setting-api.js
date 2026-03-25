import { postRequest, getRequest } from '/@/lib/axios';

export const enterpriseSettingApi = {
  // 查询配置详情
  queryByKey: (enterpriseId, settingKey) => {
    return getRequest(`/enterprise/setting/queryByKey/${enterpriseId}?settingKey=${settingKey}`);
  },
  // 保存配置
  saveSetting: (params) => {
    return postRequest('/enterprise/setting/save', params);
  }
};

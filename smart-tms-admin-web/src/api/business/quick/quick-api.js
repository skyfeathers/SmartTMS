import { getRequest, postRequest } from '/@/lib/axios';

export const quickApi = {
  search: (params) => {
    return postRequest('/quick/search', params);
  },
};

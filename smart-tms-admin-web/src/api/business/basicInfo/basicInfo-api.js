import { getRequest, postRequest } from '/@/lib/axios';

export const basicInfoApi = {
    confirm: (basicInfoId, confirmFlag) => {
        return getRequest(`/car/cost/basicInfo/confirm/update/${basicInfoId}/${confirmFlag}`);
    },
    del: (basicInfoId) => {
        return getRequest(`/car/cost/basicInfo/del/${basicInfoId}`);
    },
    detail: (waybillId) => {
        return getRequest(`/car/cost/basicInfo/detail/${waybillId}`);
    },
    page: (params) => {
        return postRequest(`/car/cost/basicInfo/page`,params);
    },
    update: (params) => {
        return postRequest(`/car/cost/basicInfo/update`,params);
    }
};

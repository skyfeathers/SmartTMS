import { getRequest, postRequest } from "/@/lib/axios";

export const mailAddressApi = {
    // 查询邮寄地址列表
    queryMailAddressList: (shipperId) => {
        return getRequest(`/shipper/mailAddress/query/${shipperId}`)
    },
    // 增加
    addMailAddress: (param) => {
        return postRequest('/shipper/mailAddress/save', param)
    },
    // 编辑
    updateMailAddress: (param) => {
        return postRequest('/shipper/mailAddress/update', param)
    },
    // 删除
    deleteMailAddress: (mailAddressId) => {
        return getRequest(`/shipper/mailAddress/delete/${mailAddressId}`)
    },
};
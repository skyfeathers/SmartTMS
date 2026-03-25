import { getRequest, postRequest } from "/@/lib/axios";

export const paymentWayApi = {
    // 查询付款方式列表
    queryPaymentWayList: (shipperId) => {
        return getRequest(`/shipper/paymentWay/query/${shipperId}`)
    },
    // 增加
    addPaymentWay: (param) => {
        return postRequest('/shipper/paymentWay/save', param)
    },
    // 编辑
    updatePaymentWay: (param) => {
        return postRequest('/shipper/paymentWay/update', param)
    },
    // 删除
    deletePaymentWay: (paymentWayId) => {
        return getRequest(`/shipper/paymentWay/delete/${paymentWayId}`)
    },
};
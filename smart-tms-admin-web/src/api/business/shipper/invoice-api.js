import { getRequest, postRequest } from "/@/lib/axios";

export const invoiceApi = {
    // 查询发票列表
    queryInvoiceList: (shipperId) => {
        return getRequest(`/shipper/invoice/${shipperId}`)
    },
    // 增加
    addInvoice: (param) => {
        return postRequest('/shipper/invoice/save', param)
    },
    // 编辑
    updateInvoice: (param) => {
        return postRequest('/shipper/invoice/update', param)
    },
    // 删除
    deleteInvoice: (invoiceId) => {
        return getRequest(`/shipper/invoice/delete/${invoiceId}`)
    },
};
import { getRequest, postRequest } from "/@/lib/axios";

export const contactApi = {
    // 查询联系人列表
    queryContactList: (shipperId) => {
        return getRequest(`/shipper/contact/query/${shipperId}`)
    },
    // 增加
    addContact: (param) => {
        return postRequest('/shipper/contact/save', param)
    },
    // 编辑
    updateContact: (param) => {
        return postRequest('/shipper/contact/update', param)
    },
    // 删除
    deleteContact: (contactId) => {
        return getRequest(`/shipper/contact/delete/${contactId}`)
    },
};
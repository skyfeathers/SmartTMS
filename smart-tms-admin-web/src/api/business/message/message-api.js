import {getRequest, postRequest} from "/@/lib/axios";

export const messageApi = {
    // 通知消息-分页查询 @listen
    queryMessage: (param) => {
        return postRequest('/msg/query', param);
    },
    // 通知消息-查询未读消息数 @yandy
    queryUnreadCount: () => {
        return getRequest('/msg/query/unread/count');
    },
    // 通知消息-标记已读 @listen
    updateReadFlag: (msgId) => {
        return getRequest(`/msg/update/read/${msgId}`);
    },
    // 通知消息-以及消息类型更新已读 @listen
    updateReadFlagByMsgType: (msgType) => {
        return getRequest(`/msg/update/read/msgType/${msgType}`);
    },
    // 通知消息 - 全部已读
    updateAllReadFlag: () => {
      return getRequest('/msg/update/all/read');
    }


};

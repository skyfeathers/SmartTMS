// 运输类型
export const TRANSPORTATION_TYPE_ENUM = {
    CONTAINER_TRANSPORT: {
        value: 1002998,
        desc: '集装箱运输'
    },
    GENERAL_GOODS_TRANSPORT: {
        value: 1002996,
        desc: '普货运输'
    }
}
// 运输路线类型
export const PATH_TYPE_ENUM = {
    CONTAINER_LOCATION: {
        value: 1,
        desc: '提箱地点'
    },
    PLACING_LOADING: {
        value: 2,
        desc: '装货地点'
    },
    UNLOADING_LOCATION: {
        value: 3,
        desc: '卸货地点'
    },
    RETURN_CONTAINER_LOCATION: {
        value: 4,
        desc: '还箱地点'
    }
}

export default {
    TRANSPORTATION_TYPE_ENUM,
    PATH_TYPE_ENUM
}

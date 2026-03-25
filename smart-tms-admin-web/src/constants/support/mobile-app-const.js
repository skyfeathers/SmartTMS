
export const MOBILE_APP_PLATFORM_TYPE_ENUM = {
  ANDROID: {
    value: 1,
    desc: 'ANDROID',
  },
  IOS: {
    value: 2,
    desc: 'IOS',
  },
};

export const MOBILE_APP_UPDATE_TYPE_ENUM = {
  NOT_NEED_UPDATE: {
    value: 1,
    desc: '无需更新',
  },
  FREEDOM_UPDATE: {
    value: 2,
    desc: '自由更新',
  },
  FORCE_UPDATE: {
    value: 3,
    desc: '强制更新',
  },
};


export default {
  MOBILE_APP_PLATFORM_TYPE_ENUM,
  MOBILE_APP_UPDATE_TYPE_ENUM
};

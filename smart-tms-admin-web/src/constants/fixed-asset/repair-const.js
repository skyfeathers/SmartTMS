/**
 * 固定资产-维修登记 枚举
 *
 * @Author:    卓大
 * @Date:      2023-03-23 15:01:51
 * @Copyright  1024创新实验室 （ https://1024lab.net ）
 */



export const ASSET_REPAIR_STATUS_ENUM = {
    AUDIT: {
      value: 1,
      desc: '待审核'
    },
    REJECT: {
      value: 2,
      desc: '审核驳回'
    },
    REPAIRING: {
      value: 11,
      desc: '维修中'
    },
    REPAIR_FINISH: {
      value: 12,
      desc: '维修完成'
    },
  };
  

  export default {
    ASSET_REPAIR_STATUS_ENUM,
  };
  
/**
 * 固定资产-报废 枚举
 *
 * @Author:    卓大
 * @Date:      2023-03-23 15:01:51
 * @Copyright  1024创新实验室 （ https://1024lab.net ）
 */



export const ASSET_SCRAP_STATUS_ENUM = {
    AUDIT: {
      value: 1,
      desc: '待审核'
    },
    REJECT: {
      value: 2,
      desc: '审核驳回'
    },
    PASS: {
      value: 3,
      desc: '审核通过'
    },
  };
  

  export default {
    ASSET_SCRAP_STATUS_ENUM,
  };
  
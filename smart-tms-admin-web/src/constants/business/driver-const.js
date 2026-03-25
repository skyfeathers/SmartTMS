export const
  DRIVER_STATUS_ENUM =
    {
      ACTIVE: {
        value: 1,
        desc: '启用'
      },
      DISABLED: {
        value: 2,
        desc: '禁用'
      }
    };

export const
  INSURANCE_TYPE_ENUM =
    {
      COMMERCIAL_INSURANCE: {
        value: 1,
        desc: '商业险'
      },
      COMPULSORY_TRAFFIC_INSURANCE: {
        value: 2,
        desc: '交强险'
      },
      OVERPAYMENT_INSURANCE: {
        value: 3,
        desc: '超赔险'
      },
      EMPLOYER_RESPONSIBILITY_INSURANCE: {
        value: 4,
        desc: '雇主责任险'
      },
      CAR_BOAT_TAX: {
        value: 5,
        desc: '车船税'
      },
      DRIVER_INSURANCE: {
        value: 6,
        desc: '驾乘险'
      }
    };

export const
  VEHICLE_CLASS_ENUM =
    {
      A1: {
        value: 1,
        desc: 'A1'
      },
      A2: {
        value: 2,
        desc: 'A2'
      },
      A3: {
        value: 3,
        desc: 'A3'
      },
      B1: {
        value: 4,
        desc: 'B1'
      },
      B2: {
        value: 5,
        desc: 'B2'
      },
      C1: {
        value: 6,
        desc: 'C1'
      },
      C2: {
        value: 7,
        desc: 'C2'
      },
      C3: {
        value: 8,
        desc: 'C3'
      },
      C4: {
        value: 9,
        desc: 'C4'
      },
      C5: {
        value: 10,
        desc: 'C5'
      },
      D: {
        value: 11,
        desc: 'D'
      }
    };

/**
 * 经营方式1内管车 2挂靠车 3外派车:
 */
export const DRIVER_BUSINESS_MODE_ENUM = {
  INNER_MANAGEMENT: {
    value: 1,
    desc: '内管',
  },
  RELY: {
    value: 2,
    desc: '挂靠',
  },
  ASSIGNMENT: {
    value: 3,
    desc: '外派',
  },
};

export default {
  DRIVER_STATUS_ENUM,
  INSURANCE_TYPE_ENUM,
  VEHICLE_CLASS_ENUM,
  DRIVER_BUSINESS_MODE_ENUM
};

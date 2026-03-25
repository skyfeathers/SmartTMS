
/**
 * 根据地区编码获取省市区编码
 */
export const getCodeByAdcode = (district) => {
  let adcodeStr = district.toString();
  if(!adcodeStr || adcodeStr.length !== 6){
    return null;
  }
  let province = Number(`${adcodeStr.substring(0, 2)}0000`);
  let city = Number(`${adcodeStr.substring(0, 4)}00`);
  return {
    province,
    city,
    district
  }
};

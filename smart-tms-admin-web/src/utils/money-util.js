/*
 * @Description:
 * @Author: zhuoda
 * @Date: 2021-12-27
 * @LastEditTime: 2022-02-09 11:17:41
 * @LastEditors: LiHaiFan
 */

export const formatMoney = (value) => {
  if (!value) {
    return '0';
  }

  // 是否负数
  const bMinus = value < 0;
  let num = value.toFixed(2) + '';
  var str = num.split('.')[0];
  if (bMinus) {
    str = str.substr(1);
  }

  var lis = num.split('.')[1];
  var siz = 0;
  var result = '';
  for (var i = str.length - 1; i >= 0; i--) {
    siz++;
    if (siz % 3 == 0) {
      siz = 0;
      result = ',' + str.substr(i, 3) + result;
    }
  }

  if (str.length % 3 != 0) {
    result = str.substr(0, str.length % 3) + result;
  } else {
    result = result.substring(1, result.length);
  }
  if (bMinus) {
    return '-' + result + '.' + lis;
  }
  return result + '.' + lis;
};

//小数精度问题，小数运算
export const  computeNumber= (a, type, b) =>{

  function getDecimalLength(n) {
      const decimal = n.toString().split(".")[1];
      return decimal ? decimal.length : 0;
  }

  const amend = (n, precision = 15) => parseFloat(Number(n).toPrecision(precision));
  const power = Math.pow(10, Math.max(getDecimalLength(a), getDecimalLength(b)));
  let result = 0;

  a = amend(a * power);
  b = amend(b * power);

  switch (type) {
      case "+":
          result = (a + b) / power;
          break;
      case "-":
          result = (a - b) / power;
          break;
      case "*":
          result = (a * b) / (power * power);
          break;
      case "/":
          result = a / b;
          break;
  }

  result = amend(result);

  return {
      /** 计算结果 */
      result,
      next(nextType, nextValue) {
          return computeNumber(result, nextType, nextValue);
      }
  };
}

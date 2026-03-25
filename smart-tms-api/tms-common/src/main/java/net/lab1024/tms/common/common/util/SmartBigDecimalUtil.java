package net.lab1024.tms.common.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 全局 BigDecimal 工具类
 *
 * @author 胡克
 * @date 2018/01/17 13:54
 */
public class SmartBigDecimalUtil {

    /**
     * 价格类型 保留小数点 2
     */
    public static final int PRICE_DECIMAL_POINT = 2;

    /**
     * 价格类型 保留小数点 6
     */
    public static final int SIX_PRICE_DECIMAL_POINT = 6;

    /**
     * 重量类型保留小数点 3
     */
    public static final int WEIGHT_DECIMAL_POINT = 3;

    public static final BigDecimal ONE_HUNDRED = new BigDecimal("100");

    /**
     * 金额相关计算方法：四舍五入 保留2位小数点
     */
    public static class Amount {

        public static BigDecimal add(BigDecimal num1, BigDecimal num2) {
            if (num1 == null && num2 == null) {
                return new BigDecimal(0);
            }

            if (num1 == null) {
                return setScale(BigDecimal.ZERO.add(num2), PRICE_DECIMAL_POINT);
            }

            if (num2 == null) {
                return setScale(num1.add(BigDecimal.ZERO), PRICE_DECIMAL_POINT);
            }
            return setScale(num1.add(num2), PRICE_DECIMAL_POINT);
        }

        public static BigDecimal add(BigDecimal... array) {
            BigDecimal res = new BigDecimal(0);
            for (BigDecimal item : array) {
                if (item == null) {
                    res = res.add(BigDecimal.ZERO);
                } else {
                    res = res.add(item);
                }
            }
            return setScale(res, PRICE_DECIMAL_POINT);
        }

        public static BigDecimal multiply(BigDecimal num1, BigDecimal num2) {
            return setScale(num1.multiply(num2), PRICE_DECIMAL_POINT);
        }

        public static BigDecimal subtract(BigDecimal num1, BigDecimal num2) {
            return setScale(num1.subtract(num2), PRICE_DECIMAL_POINT);
        }

        public static BigDecimal divide(BigDecimal num1, BigDecimal num2) {
            if(null == num1 || null == num2){
                return BigDecimal.ZERO;
            }
            if(num1.compareTo(BigDecimal.ZERO) == 0 || num2.compareTo(BigDecimal.ZERO) == 0){
                return BigDecimal.ZERO;
            }
            return setScale(num1.divide(num2, RoundingMode.HALF_UP), PRICE_DECIMAL_POINT);
        }
    }

    /**
     * 金额相关计算方法：四舍五入 保留2位小数点
     */
    public static class AmountSix {

        public static BigDecimal add(BigDecimal num1, BigDecimal num2) {
            return setScale(num1.add(num2), SIX_PRICE_DECIMAL_POINT);
        }

        public static BigDecimal multiply(BigDecimal num1, BigDecimal num2) {
            return setScale(num1.multiply(num2), SIX_PRICE_DECIMAL_POINT);
        }

        public static BigDecimal subtract(BigDecimal num1, BigDecimal num2) {
            return setScale(num1.subtract(num2), SIX_PRICE_DECIMAL_POINT);
        }

        public static BigDecimal divide(BigDecimal num1, BigDecimal num2) {
            return num1.divide(num2, PRICE_DECIMAL_POINT, RoundingMode.HALF_UP);
        }
    }

    /**
     * 重量相关计算方法：四舍五入 保留3位小数点
     */
    public static class Weight {

        public static BigDecimal add(BigDecimal num1, BigDecimal num2) {
            return setScale(num1.add(num2), WEIGHT_DECIMAL_POINT);
        }

        public static BigDecimal multiply(BigDecimal num1, BigDecimal num2) {
            return setScale(num1.multiply(num2), WEIGHT_DECIMAL_POINT);
        }

        public static BigDecimal subtract(BigDecimal num1, BigDecimal num2) {
            return setScale(num1.subtract(num2), WEIGHT_DECIMAL_POINT);
        }

        public static BigDecimal divide(BigDecimal num1, BigDecimal num2) {
            return num1.divide(num2, WEIGHT_DECIMAL_POINT, RoundingMode.HALF_UP);
        }
    }

    /**
     * BigDecimal 加法 num1 + num2
     * 未做非空校验
     *
     * @param num1
     * @param num2
     * @param point 请使用BigDecimalUtils.PRICE_DECIMAL_POINT | BigDecimalUtils.WEIGHT_DECIMAL_POINT
     * @return BigDecimal
     */
    public static BigDecimal add(BigDecimal num1, BigDecimal num2, int point) {
        return setScale(num1.add(num2), point);
    }

    /**
     * BigDecimal 乘法 num1 x num2
     * 未做非空校验
     *
     * @param num1
     * @param num2
     * @param point 请使用BigDecimalUtils.PRICE_DECIMAL_POINT | BigDecimalUtils.WEIGHT_DECIMAL_POINT
     * @return BigDecimal
     */
    public static BigDecimal multiply(BigDecimal num1, BigDecimal num2, int point) {
        return setScale(num1.multiply(num2), point);
    }

    /**
     * BigDecimal 减法 num1 - num2
     * 未做非空校验
     *
     * @param num1
     * @param num2
     * @param point 请使用BigDecimalUtils.PRICE_DECIMAL_POINT | BigDecimalUtils.WEIGHT_DECIMAL_POINT
     * @return BigDecimal
     */
    public static BigDecimal subtract(BigDecimal num1, BigDecimal num2, int point) {
        return setScale(num1.subtract(num2), point);
    }

    /**
     * BigDecimal 除法 num1/num2
     * 未做非空校验
     *
     * @param num1
     * @param num2
     * @param point 请使用BigDecimalUtils.PRICE_DECIMAL_POINT | BigDecimalUtils.WEIGHT_DECIMAL_POINT
     * @return BigDecimal
     */
    public static BigDecimal divide(BigDecimal num1, BigDecimal num2, int point) {
        return num1.divide(num2, point, RoundingMode.HALF_UP);
    }

    /**
     * 设置小数点类型为 四舍五入
     *
     * @param num
     * @param point
     * @return BigDecimal
     */
    public static BigDecimal setScale(BigDecimal num, int point) {
        return num.setScale(point, RoundingMode.HALF_UP);
    }

    /**
     * 比较 num1 是否大于 num2
     *
     * @param num1
     * @param num2
     * @return boolean
     */
    public static boolean isGreaterThan(BigDecimal num1, BigDecimal num2) {
        return num1.compareTo(num2) > 0;
    }

    /**
     * 比较 num1 是否大于等于 num2
     *
     * @param num1
     * @param num2
     * @return boolean
     */
    public static boolean isGreaterOrEqual(BigDecimal num1, BigDecimal num2) {
        return isGreaterThan(num1, num2) || equals(num1, num2);
    }

    /**
     * 比较 num1 是否小于 num2
     *
     * @param num1
     * @param num2
     * @return boolean
     */
    public static boolean isLessThan(BigDecimal num1, BigDecimal num2) {
        return num1.compareTo(num2) < 0;
    }

    /**
     * 比较 num1 是否小于等于 num2
     *
     * @param num1
     * @param num2
     * @return boolean
     */
    public static boolean isLessOrEqual(BigDecimal num1, BigDecimal num2) {
        return isLessThan(num1, num2) || equals(num1, num2);
    }

    /**
     * 比较 num1 是否等于 num2
     *
     * @param num1
     * @param num2
     * @return
     */
    public static boolean equals(BigDecimal num1, BigDecimal num2) {
        return num1.compareTo(num2) == 0;
    }

    /**
     * 计算 num1 / num2 的百分比
     *
     * @param num1
     * @param num2
     * @param point 保留几位小数
     * @return String
     */
    public static BigDecimal percent(Integer num1, Integer num2, int point) {
        if (num1 == null || num2 == null) {
            return BigDecimal.ZERO;
        }
        if (num2.equals(0)) {
            return BigDecimal.ZERO;
        }
        return percent(new BigDecimal(num1), new BigDecimal(num2), point);
    }

    /**
     * 计算 num1 / num2 的百分比
     *
     * @param num1
     * @param num2
     * @param point 保留几位小数
     * @return String
     */
    public static BigDecimal percent(BigDecimal num1, BigDecimal num2, int point) {
        if (num1 == null || num2 == null) {
            return BigDecimal.ZERO;
        }
        if (equals(BigDecimal.ZERO, num2)) {
            return BigDecimal.ZERO;
        }
        BigDecimal percent = num1.divide(num2, point + 2, RoundingMode.HALF_UP);
        return percent.multiply(ONE_HUNDRED).setScale(point);
    }

    /**
     * 比较 num1，num2 返回最大的值
     *
     * @param num1
     * @param num2
     * @return BigDecimal
     */
    public static BigDecimal max(BigDecimal num1, BigDecimal num2) {
        return num1.compareTo(num2) > 0 ? num1 : num2;
    }

    /**
     * 比较 num1，num2 返回最小的值
     *
     * @param num1
     * @param num2
     * @return BigDecimal
     */
    public static BigDecimal min(BigDecimal num1, BigDecimal num2) {
        return num1.compareTo(num2) < 0 ? num1 : num2;
    }

    /**
     * 百分比计算
     *
     * @param num1
     * @param num2
     * @return
     */
    public static BigDecimal dividePercent(BigDecimal num1, BigDecimal num2) {
        if (num1 == null || num2 == null) {
            return BigDecimal.ZERO;
        }
        if (num1.compareTo(BigDecimal.ZERO) == 0 || num2.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        if (num2.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        return SmartBigDecimalUtil.Amount.divide(num1.multiply(ONE_HUNDRED), num2);
    }

    public static void main(String[] args) {
        System.out.println(percent(new BigDecimal("3"), new BigDecimal("11"), 2));
        System.out.println(percent(new BigDecimal("8"), new BigDecimal("11"), 2));
    }
}

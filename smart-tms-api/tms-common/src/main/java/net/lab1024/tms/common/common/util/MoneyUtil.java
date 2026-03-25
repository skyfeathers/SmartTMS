package net.lab1024.tms.common.common.util;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class MoneyUtil {
    /**
     * 大写数字
     */
    public static final String[] NUMBERS = {"零", "壹", "贰", "叁", "肆", "伍",
            "陆", "柒", "捌", "玖"};

    /**
     * 整数单位
     */
    public static final String[] IUNIT = {"元", "拾", "佰", "仟", "万", "拾", "佰",
            "仟", "亿", "拾", "佰", "仟", "万", "拾", "佰", "仟"};

    /**
     * 小数单位
     */
    public static final String[] DUNIT = {"角", "分", "厘"};

    /**
     * 大写金额
     *
     * @param str
     * @return
     */
    public static String toChinese(String str) {
        str = str.replaceAll(",", "");// 去掉","

        String integerStr;// 整数部分数字

        String decimalStr;// 小数部分数字

        // 初始化：分离整数部分和小数部分
        if (str.indexOf(".") > 0) {
            integerStr = str.substring(0, str.indexOf("."));

            decimalStr = str.substring(str.indexOf(".") + 1);

        } else if (str.indexOf(".") == 0) {
            integerStr = "";
            decimalStr = str.substring(1);

        } else {
            integerStr = str;
            decimalStr = "";
        }

        // integerStr去掉首0，不必去掉decimalStr的尾0(超出部分舍去)
        if (!integerStr.equals("")) {
            integerStr = Long.toString(Long.parseLong(integerStr));

            if (integerStr.equals("0")) {
                integerStr = "";
            }
        }

        // overflow超出处理能力，直接返回
        if (integerStr.length() > IUNIT.length) {
            System.out.println(str + ":超出处理能力");
            return str;
        }

        int[] integers = toArray(integerStr);// 整数部分数字

        boolean isMust5 = isMust5(integerStr);// 设置万单位

        int[] decimals = toArray(decimalStr);// 小数部分数字

        String integerChinese =  getChineseInteger(integers, isMust5);

        String decimalChinese = getChineseDecimal(decimals);

        if("".equals(integerChinese) && "".equals(decimalChinese)){
            return NUMBERS[0]+IUNIT[0];
        }
        return integerChinese+decimalChinese;
    }

    /**
     * 整数部分和小数部分转换为数组，从高位至低位
     *
     * @param number
     * @return
     */
    private static int[] toArray(String number) {
        int[] array = new int[number.length()];

        for (int i = 0; i < number.length(); i++) {
            array[i] = Integer.parseInt(number.substring(i, i + 1));
        }
        return array;
    }

    /**
     * 中文整数金额
     *
     * @param integers
     * @param isMust5
     * @return
     */
    private static String getChineseInteger(int[] integers, boolean isMust5) {
        StringBuffer chineseInteger = new StringBuffer("");

        int length = integers.length;

        for (int i = 0; i < length; i++) {
            // 0出现在关键位置：1234(万)5678(亿)9012(万)3456(元)
            // 特殊情况：10(拾元、壹拾元、壹拾万元、拾万元)
            String key = "";

            if (integers[i] == 0) {
                if ((length - i) == 13)// 万(亿)(必填)
                {
                    key = IUNIT[4];
                } else if ((length - i) == 9)// 亿(必填)
                {
                    key = IUNIT[8];
                } else if ((length - i) == 5 && isMust5)// 万(不必填)
                {
                    key = IUNIT[4];
                } else if ((length - i) == 1)// 元(必填)
                {
                    key = IUNIT[0];
                }

                // 0遇非0时补零，不包含最后一位
                if ((length - i) > 1 && integers[i + 1] != 0) {
                    key += NUMBERS[0];
                }

            }
            chineseInteger.append(integers[i] == 0 ? key
                    : (NUMBERS[integers[i]] + IUNIT[length - i - 1]));
        }
        return chineseInteger.toString();
    }

    /**
     * 中文小数金额
     *
     * @param decimals
     * @return
     */
    private static String getChineseDecimal(int[] decimals) {
        StringBuffer chineseDecimal = new StringBuffer("");

        for (int i = 0; i < decimals.length; i++) {
            // 舍去3位小数之后的
            if (i == 3)
                break;

            chineseDecimal.append(decimals[i] == 0 ? ""
                    : (NUMBERS[decimals[i]] + DUNIT[i]));
        }
        return chineseDecimal.toString();
    }

    /**
     * 第五位数字是否加万
     *
     * @param integerStr
     * @return
     */
    private static boolean isMust5(String integerStr) {
        int length = integerStr.length();

        if (length > 4) {
            String subInteger = "";

            if (length > 8) {
                // 取得从低位数，第5到第8位的字串
                subInteger = integerStr.substring(length - 8, length - 4);
            } else {
                subInteger = integerStr.substring(0, length - 4);
            }
            return Integer.parseInt(subInteger) > 0;
        } else {
            return false;
        }
    }

    /**
     * 大小写金额比较 支持最小单位：厘
     *
     * @param amount        小写金额
     * @param chineseAmount 大写金额
     * @return
     */
    public static boolean isAmtEqChn(String amount, String chineseAmount) {

        if (StringUtils.isBlank(chineseAmount)) {
            return false;
        }

        // 格式化大写金额去整
        chineseAmount = chineseAmount.replace("整", "");
        chineseAmount = chineseAmount.replace("正", "");
        chineseAmount = chineseAmount.replace("人民币", "");

        chineseAmount = toSimplified(chineseAmount);

        String toChnAmt = toChinese(amount);

        if (chineseAmount.equals(toChnAmt)) {
            return true;
        }
        return false;
    }

    /**
     * 大写金额简体化
     *
     * @param chnAmt
     * @return
     */
    public static String toSimplified(String chnAmt) {

        if (StringUtils.isBlank(chnAmt)) {
            return null;
        }

        char[] amtArrChar = chnAmt.toCharArray();

        Map<String, String> mapping = getSimpToTradMapping();

        for (int i = 0; i < amtArrChar.length; i++) {
            if (mapping.containsKey(String.valueOf(amtArrChar[i]))) {
                amtArrChar[i] = mapping.get(String.valueOf(amtArrChar[i]))
                        .charAt(0);
            }
        }

        return String.valueOf(amtArrChar);
    }

    /**
     * 转换为以万元为单位,保留两位小数(四舍五入)
     * @param price 需要转换的数字
     * @return 万元为单位的结果
     */
    public static String convertTenThousand(BigDecimal price){
        // 转换为万元（除以10000）
        BigDecimal decimal = price.divide(new BigDecimal("10000"));
        // 保留两位小数
        DecimalFormat formater = new DecimalFormat("0.00");
        // 四舍五入
        formater.setRoundingMode(RoundingMode.HALF_UP);
        return formater.format(decimal);
    }

    /**
     * 繁体对应
     *
     * @return
     */
    private static Map<String, String> getSimpToTradMapping() {

        Map<String, String> mapping = new HashMap<String, String>();

        mapping.put("圆", "元");
        mapping.put("圓", "元");
        mapping.put("貳", "贰");
        mapping.put("陸", "陆");
        mapping.put("億", "亿");
        mapping.put("萬", "万");

        return mapping;
    }

    public static void main(String[] args) {
        BigDecimal bigDecimal = new BigDecimal("0");
        convertTenThousand(bigDecimal);

        String amount = "0000000.00";

        System.out.println("小写金额" + amount + "转换大写金额:" + toChinese(amount));

        String chnAmount = "玖萬玖仟玖佰玖拾玖圓玖角玖分";

        System.out.println(chnAmount + "-简体化:" + toSimplified(chnAmount));

        System.out.println("小写金额：" + amount + " 与 大写金额:" + toChinese(amount) + "比较结果--------" + isAmtEqChn(amount,  toChinese(amount)));
    }
}

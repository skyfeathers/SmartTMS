package net.lab1024.tms.common.common.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;

/**
 * java8 LocalDate 日期工具类
 *
 * @author listen
 * @date 2019/02/20 10:16
 */
public class SmartLocalDateUtil {

    /**
     * 日期格式 ： yyyy-MM-dd
     * 例：2019-10-15
     */
    public static final String YMD = "yyyy-MM-dd";

    public static final String SPECIAL_YMD = "yyyyMMdd";

    public static final String CHINESE_YMD = "yyyy年MM月dd日";

    /**
     * 日期格式：MM月dd日
     * 例：10月11日
     */
    public static final String SPECIAL_MD = "MM月dd日";

    /**
     * 日期格式 ： yyyy-MM-dd HH:mm:ss
     * 例：2019-10-15 10:15:00
     */
    public static final String YMD_HMS = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式 ： yyyy-MM-dd HH:mm
     * 例：2019-10-15 10:15
     */
    public static final String YMD_HM = "yyyy-MM-dd HH:mm";

    /**
     * 日期格式 ： yyyyMMddHHmmss
     * 例：20091225091010
     */
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    /**
     * 日期格式 ： yyyy-MM
     * 例：2019-10
     */
    public static final String YM = "yyyy-MM";

    /**
     * 时间格式 ： HH:mm:ss
     * 例：10:18:00
     */
    public static final String HHMMSS = "HH:mm:ss";

    /**
     * 时间格式 ： HH:mm
     * 例：10:18
     */
    public static final String HH_MM = "HH:mm";

    public static final DateTimeFormatter FORMATTER_YMD = DateTimeFormatter.ofPattern(YMD);

    public static final DateTimeFormatter FORMATTER_SPECIAL_YMD = DateTimeFormatter.ofPattern(SPECIAL_YMD);

    public static final DateTimeFormatter FORMATTER_CHINESE_YMD = DateTimeFormatter.ofPattern(CHINESE_YMD);

    public static final DateTimeFormatter FORMATTER_SPECIAL_MD = DateTimeFormatter.ofPattern(SPECIAL_MD);

    public static final DateTimeFormatter FORMATTER_YYYYMMDDHHMMSS = DateTimeFormatter.ofPattern(YYYYMMDDHHMMSS);

    public static final DateTimeFormatter FORMATTER_YMD_HM = DateTimeFormatter.ofPattern(YMD_HM);

    public static final DateTimeFormatter FORMATTER_HM = DateTimeFormatter.ofPattern(HH_MM);

    public static final DateTimeFormatter FORMATTER_YMD_HMS = DateTimeFormatter.ofPattern(YMD_HMS);

    public static final DateTimeFormatter FORMATTER_YM = DateTimeFormatter.ofPattern(YM);

    /**
     * 格式日期 yyyy-MM-dd
     * 返回String字符
     *
     * @return
     */
    public static String formatYMD(LocalDateTime localDateTime) {
        return localDateTime.format(FORMATTER_YMD);
    }

    /**
     * 获取时间戳
     *
     * @param time
     * @return
     */
    public static Long getTimestamp(LocalDateTime time) {
        return time.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
    }

    /**
     * 格式日期 yyyy-MM-dd HH:mm:ss
     * 返回String字符
     *
     * @return
     */
    public static String formatYMDHMS(LocalDateTime localDateTime) {
        return localDateTime.format(FORMATTER_YMD_HMS);
    }

    /**
     * 格式日期 yyyyMMddHHmmss
     * 返回String字符
     *
     * @return
     */
    public static String formatYYYYMMDDHHMMSS(LocalDateTime localDateTime) {
        return localDateTime.format(FORMATTER_YYYYMMDDHHMMSS);
    }

    public static String formatYMDHMS(LocalDate localDate) {
        return localDate.format(FORMATTER_YMD_HMS);
    }

    public static String formatYMD(LocalDate localDate) {
        return localDate.format(FORMATTER_YMD);
    }

    public static String formatSpecialMD(LocalDate localDate) {
        return localDate.format(FORMATTER_SPECIAL_MD);
    }

    /**
     * 格式日期 yyyy-MM-dd HH:mm
     * 返回String字符
     *
     * @return
     */
    public static String formatYMDHM(LocalDateTime localDateTime) {
        return localDateTime.format(FORMATTER_YMD_HM);
    }

    /**
     * 格式日期 HH:mm
     * 返回String字符
     *
     * @return
     */
    public static String formatHM(LocalDateTime localDateTime) {
        return localDateTime.format(FORMATTER_HM);
    }

    /**
     * 格式日期 yyyy-MM
     * 返回String字符 2019-10
     *
     * @return
     */
    public static String formatYM(LocalDateTime localDateTime) {
        return localDateTime.format(FORMATTER_YM);
    }

    public static String formatYM(LocalDate localDate) {
        return localDate.format(FORMATTER_YM);
    }

    /**
     * 格式日期 yyyy年MM月dd日
     * 返回String字符 2019年10月1日
     *
     * @return
     */
    public static String formatChineseYMD(LocalDateTime localDateTime) {
        return localDateTime.format(FORMATTER_CHINESE_YMD);
    }

    /**
     * 解析字符串为 年月日
     *
     * @param dateStr yyyy-MM-dd 例：2019-10-15
     * @return
     */
    public static LocalDate parseYMD(String dateStr) {
        return LocalDate.parse(dateStr, FORMATTER_YMD);
    }

    /**
     * 解析字符串为 年月日
     *
     * @param dateStr yyyyMMdd 例：20191015
     * @return
     */
    public static LocalDate parseSpecialYMD(String dateStr) {
        return LocalDate.parse(dateStr, FORMATTER_SPECIAL_YMD);
    }

    /**
     * 解析字符串为 年月日
     *
     * @param dateStr yyyy年MM月dd日 例：2019年10月15日
     * @return
     */
    public static LocalDate parseChineseYMD(String dateStr) {
        return LocalDate.parse(dateStr, FORMATTER_CHINESE_YMD);
    }


    public static YearMonth parseYearMonth(String yyyyMM) {
        return  YearMonth.parse(yyyyMM, FORMATTER_YM);
    }
    /**
     * 格式器 yyyy-MM-dd HH:mm:ss
     *
     * @param dateStr
     * @return
     */
    public static LocalDateTime parseYMDHMS(String dateStr) {
        return LocalDateTime.parse(dateStr, FORMATTER_YMD_HMS);
    }

    /**
     * 格式器 yyyy-MM-dd HH:mm
     *
     * @param dateStr
     * @return
     */
    public static LocalDateTime parseYMDHM(String dateStr) {
        return LocalDateTime.parse(dateStr, FORMATTER_YMD_HM);
    }

    /**
     * 解析  yyyyMMddHHmmss 格式的字符串 返回日期时间
     *
     * @param dateStr
     * @return
     */
    public static LocalDateTime parseYYYYMMDDHHMMSS(String dateStr) {
        return LocalDateTime.parse(dateStr, FORMATTER_YYYYMMDDHHMMSS);
    }

    /**
     * 获取当前时间戳(秒)
     *
     * @return
     */
    public static long nowSecond() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 返回格式化后的当期日期 例：2019-10-15
     *
     * @return
     */
    public static String nowDate() {
        return formatYMD(LocalDate.now());
    }

    /**
     * 返回格式化后的当期日期时间 例：2019-10-15 10:00:00
     *
     * @return
     */
    public static String nowDateTime() {
        return formatYMDHMS(LocalDateTime.now());
    }

    /**
     * 将时间格式化为 星期几，例：星期一 ... 星期日
     *
     * @param localDate
     * @return
     */
    public static String formatToChineseWeek(LocalDate localDate) {
        return localDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.CHINESE);
    }

    /**
     * 将时间格式化为 周几，例：周一 ... 周日
     *
     * @param localDate
     * @return
     */
    public static String formatToChineseWeekZhou(LocalDate localDate) {
        return formatToChineseWeek(localDate).replace("星期", "周");
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
    public static LocalDateTime toLocalDateTime(LocalDate localDate) {
        return LocalDateTime.of(localDate, LocalTime.of(0, 0));
    }
    /**
     * 获取当天剩余时间 单位
     *
     * @param unit 时间单位
     * @return
     */
    public static Long getDayBalanceTime(ChronoUnit unit) {
        LocalDateTime now = LocalDateTime.now();
        return Duration.between(now, now.plusDays(1L).with(LocalTime.MIN)).get(unit);
    }

    public static Integer getDiffMonth(LocalDate startTime,LocalDate endTime){
        int years = startTime.getYear() - endTime.getYear();
        int months = years * 12 + (startTime.getMonthValue() - endTime.getMonthValue());
        return months;
    }
}

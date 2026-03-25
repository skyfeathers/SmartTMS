package net.lab1024.tms.common.common.util;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;

import java.util.*;
import java.util.stream.Collectors;

public class SmartStringUtil extends StrUtil {
    public static Set<String> splitConvertToSet(String str, String split) {
        if (isEmpty(str)) {
            return new HashSet();
        } else {
            String[] splitArr = str.split(split);
            HashSet<String> set = new HashSet(splitArr.length);
            String[] var4 = splitArr;
            int var5 = splitArr.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                String string = var4[var6];
                set.add(string);
            }

            return set;
        }
    }

    public static List<String> splitConvertToList(String str, String split) {
        if (isEmpty(str)) {
            return new ArrayList();
        } else {
            String[] splitArr = str.split(split);
            ArrayList<String> list = new ArrayList(splitArr.length);
            String[] var4 = splitArr;
            int var5 = splitArr.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                String string = var4[var6];
                list.add(string);
            }

            return list;
        }
    }

    public static List<Integer> splitConverToIntList(String str, String split, int defaultVal) {
        if (isEmpty(str)) {
            return new ArrayList();
        } else {
            String[] strArr = str.split(split);
            List<Integer> list = new ArrayList(strArr.length);

            for(int i = 0; i < strArr.length; ++i) {
                try {
                    int parseInt = Integer.parseInt(strArr[i]);
                    list.add(parseInt);
                } catch (NumberFormatException var7) {
                    list.add(defaultVal);
                }
            }

            return list;
        }
    }

    public static Set<Integer> splitConverToIntSet(String str, String split, int defaultVal) {
        if (isEmpty(str)) {
            return new HashSet();
        } else {
            String[] strArr = str.split(split);
            HashSet<Integer> set = new HashSet(strArr.length);

            for(int i = 0; i < strArr.length; ++i) {
                try {
                    int parseInt = Integer.parseInt(strArr[i]);
                    set.add(parseInt);
                } catch (NumberFormatException var7) {
                    set.add(defaultVal);
                }
            }

            return set;
        }
    }

    public static Set<Integer> splitConverToIntSet(String str, String split) {
        return splitConverToIntSet(str, split, 0);
    }

    public static List<Integer> splitConverToIntList(String str, String split) {
        return splitConverToIntList(str, split, 0);
    }

    public static int[] splitConvertToIntArray(String str, String split, int defaultVal) {
        if (isEmpty(str)) {
            return new int[0];
        } else {
            String[] strArr = str.split(split);
            int[] result = new int[strArr.length];

            for(int i = 0; i < strArr.length; ++i) {
                try {
                    result[i] = Integer.parseInt(strArr[i]);
                } catch (NumberFormatException var7) {
                    result[i] = defaultVal;
                }
            }

            return result;
        }
    }

    public static int[] splitConvertToIntArray(String str, String split) {
        return splitConvertToIntArray(str, split, 0);
    }

    public static List<Long> splitConverToLongList(String str, String split, long defaultVal) {
        if (isEmpty(str)) {
            return new ArrayList();
        } else {
            String[] strArr = str.split(split);
            List<Long> list = new ArrayList(strArr.length);

            for(int i = 0; i < strArr.length; ++i) {
                try {
                    long parseLong = Long.parseLong(strArr[i]);
                    list.add(parseLong);
                } catch (NumberFormatException var9) {
                    list.add(defaultVal);
                }
            }

            return list;
        }
    }

    public static List<Long> splitConverToLongList(String str, String split) {
        return splitConverToLongList(str, split, 0L);
    }

    public static long[] splitConvertToLongArray(String str, String split, long defaultVal) {
        if (isEmpty(str)) {
            return new long[0];
        } else {
            String[] strArr = str.split(split);
            long[] result = new long[strArr.length];

            for(int i = 0; i < strArr.length; ++i) {
                try {
                    result[i] = Long.parseLong(strArr[i]);
                } catch (NumberFormatException var8) {
                    result[i] = defaultVal;
                }
            }

            return result;
        }
    }

    public static long[] splitConvertToLongArray(String str, String split) {
        return splitConvertToLongArray(str, split, 0L);
    }

    public static List<Byte> splitConverToByteList(String str, String split, byte defaultVal) {
        if (isEmpty(str)) {
            return new ArrayList();
        } else {
            String[] strArr = str.split(split);
            List<Byte> list = new ArrayList(strArr.length);

            for(int i = 0; i < strArr.length; ++i) {
                try {
                    byte parseByte = Byte.parseByte(strArr[i]);
                    list.add(parseByte);
                } catch (NumberFormatException var7) {
                    list.add(defaultVal);
                }
            }

            return list;
        }
    }

    public static List<Byte> splitConverToByteList(String str, String split) {
        return splitConverToByteList(str, split, (byte)0);
    }

    public static byte[] splitConvertToByteArray(String str, String split, byte defaultVal) {
        if (isEmpty(str)) {
            return new byte[0];
        } else {
            String[] strArr = str.split(split);
            byte[] result = new byte[strArr.length];

            for(int i = 0; i < strArr.length; ++i) {
                try {
                    result[i] = Byte.parseByte(strArr[i]);
                } catch (NumberFormatException var7) {
                    result[i] = defaultVal;
                }
            }

            return result;
        }
    }

    public static byte[] splitConvertToByteArray(String str, String split) {
        return splitConvertToByteArray(str, split, (byte)0);
    }

    public static List<Double> splitConvertToDoubleList(String str, String split, double defaultVal) {
        if (isEmpty(str)) {
            return new ArrayList();
        } else {
            String[] strArr = str.split(split);
            List<Double> list = new ArrayList(strArr.length);

            for(int i = 0; i < strArr.length; ++i) {
                try {
                    double parseByte = Double.parseDouble(strArr[i]);
                    list.add(parseByte);
                } catch (NumberFormatException var9) {
                    list.add(defaultVal);
                }
            }

            return list;
        }
    }

    public static List<Double> splitConvertToDoubleList(String str, String split) {
        return splitConvertToDoubleList(str, split, 0.0D);
    }

    public static double[] splitConvertToDoubleArray(String str, String split, double defaultVal) {
        if (isEmpty(str)) {
            return new double[0];
        } else {
            String[] strArr = str.split(split);
            double[] result = new double[strArr.length];

            for(int i = 0; i < strArr.length; ++i) {
                try {
                    result[i] = Double.parseDouble(strArr[i]);
                } catch (NumberFormatException var8) {
                    result[i] = defaultVal;
                }
            }

            return result;
        }
    }

    public static double[] splitConvertToDoubleArray(String str, String split) {
        return splitConvertToDoubleArray(str, split, 0.0D);
    }

    public static List<Float> splitConvertToFloatList(String str, String split, float defaultVal) {
        if (isEmpty(str)) {
            return new ArrayList();
        } else {
            String[] strArr = str.split(split);
            List<Float> list = new ArrayList(strArr.length);

            for(int i = 0; i < strArr.length; ++i) {
                try {
                    float parseByte = Float.parseFloat(strArr[i]);
                    list.add(parseByte);
                } catch (NumberFormatException var7) {
                    list.add(defaultVal);
                }
            }

            return list;
        }
    }

    public static List<Float> splitConvertToFloatList(String str, String split) {
        return splitConvertToFloatList(str, split, 0.0F);
    }

    public static float[] splitConvertToFloatArray(String str, String split, float defaultVal) {
        if (isEmpty(str)) {
            return new float[0];
        } else {
            String[] strArr = str.split(split);
            float[] result = new float[strArr.length];

            for(int i = 0; i < strArr.length; ++i) {
                try {
                    result[i] = Float.parseFloat(strArr[i]);
                } catch (NumberFormatException var7) {
                    result[i] = defaultVal;
                }
            }

            return result;
        }
    }

    public static float[] splitConvertToFloatArray(String str, String split) {
        return splitConvertToFloatArray(str, split, 0.0F);
    }

    public static String upperCaseFirstChar(String str) {
        if (str != null && !str.isEmpty()) {
            char firstChar = str.charAt(0);
            if (Character.isUpperCase(firstChar)) {
                return str;
            } else {
                char[] values = str.toCharArray();
                values[0] = Character.toUpperCase(firstChar);
                return new String(values);
            }
        } else {
            return str;
        }
    }

    public static String replace(String content, int begin, int end, String newStr) {
        if (begin < content.length() && begin >= 0) {
            if (end <= content.length() && end >= 0) {
                if (begin > end) {
                    return content;
                } else {
                    StringBuilder starStr = new StringBuilder();

                    for(int i = begin; i < end; ++i) {
                        starStr.append(newStr);
                    }

                    return content.substring(0, begin) + starStr + content.substring(end);
                }
            } else {
                return content;
            }
        } else {
            return content;
        }
    }

    public static List<String> joinIrregularString(String input) {
        if (input == null || input.isEmpty()) {
            return Lists.newArrayList();
        }

        // 使用正则表达式分割字符串：匹配任何空白字符、中英文逗号
        String[] parts = input.split("[\\s,，]+");

        // 过滤空字符串并用逗号连接
        return Arrays.stream(parts)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }
}
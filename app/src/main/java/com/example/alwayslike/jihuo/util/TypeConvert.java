package com.example.alwayslike.jihuo.util;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by alwayslike on 2017/5/16.
 */

public class TypeConvert {


    public static int bytes2int(byte[] bytes) {
        int num = bytes[3] & 0xFF;
        num |= ((bytes[2] << 8) & 0xFF00);
        num |= ((bytes[1] << 16) & 0xFF0000);
        num |= ((bytes[0] << 24) & 0xFF000000);
        return num;
    }

    // int转为byte数组
    public static byte[] intToByte(int number) {
        byte[] abyte = new byte[4];
        // "&" 与（AND），对两个整型操作数中对应位执行布尔代数，两个位都为1时输出1，否则0。
        abyte[3] = (byte) (0xff & number);
        // ">>"右移位，若为正数则高位补0，若为负数则高位补1
        abyte[2] = (byte) ((0xff00 & number) >> 8);
        abyte[1] = (byte) ((0xff0000 & number) >> 16);
        abyte[0] = (byte) ((0xff000000 & number) >> 24);
        return abyte;
    }

    public static float byte2float(byte[] b, int index) {
        int l;
        l = b[index + 0];
        l &= 0xff;
        l |= ((long) b[index + 1] << 8);
        l &= 0xffff;
        l |= ((long) b[index + 2] << 16);
        l &= 0xffffff;
        l |= ((long) b[index + 3] << 24);
        return Float.intBitsToFloat(l);
    }

    /**
     * 浮点转换为字节
     *
     * @param f
     * @return
     */
    public static byte[] float2byte(float f) {

        // 把float转换为byte[]
        int fbit = Float.floatToIntBits(f);

        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (fbit >> (24 - i * 8));
        }

        // 翻转数组
        int len = b.length;
        // 建立一个与源数组元素类型相同的数组
        byte[] dest = new byte[len];
        // 为了防止修改源数组，将源数组拷贝一份副本
        System.arraycopy(b, 0, dest, 0, len);
        byte temp;
        // 将顺位第i个与倒数第i个交换
        for (int i = 0; i < len / 2; ++i) {
            temp = dest[i];
            dest[i] = dest[len - i - 1];
            dest[len - i - 1] = temp;
        }

        return dest;

    }

    /**
     * long 8字节bytes[]转long
     *
     * @param bb
     * @return
     */
    public static long bytesToLong(byte[] bb) {
        return ((((long) bb[7] & 0xff) << 56) | (((long) bb[6] & 0xff) << 48)
                | (((long) bb[5] & 0xff) << 40) | (((long) bb[4] & 0xff) << 32)
                | (((long) bb[3] & 0xff) << 24) | (((long) bb[2] & 0xff) << 16)
                | (((long) bb[1] & 0xff) << 8) | (((long) bb[0] & 0xff) << 0));
    }

    /**
     * long 8字节 long转byte[]
     *
     * @param n
     * @return
     */
    public static byte[] longToBytes(long n) {
        byte[] b = new byte[8];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        b[2] = (byte) (n >> 16 & 0xff);
        b[3] = (byte) (n >> 24 & 0xff);
        b[4] = (byte) (n >> 32 & 0xff);
        b[5] = (byte) (n >> 40 & 0xff);
        b[6] = (byte) (n >> 48 & 0xff);
        b[7] = (byte) (n >> 56 & 0xff);
        return b;
    }

    public static byte[] doubleToByte(double n) {
        long v = Double.doubleToLongBits(n);
        byte[] b = new byte[8];
        b[0] = (byte) v;
        b[1] = (byte) (v >>> 8);
        b[2] = (byte) (v >>> 16);
        b[3] = (byte) (v >>> 24);
        b[4] = (byte) (v >>> 32);
        b[5] = (byte) (v >>> 40);
        b[6] = (byte) (v >>> 48);
        b[7] = (byte) (v >>> 56);
        return b;
    }

    public static double bytesToDouble(byte[] b) {
        return Double.longBitsToDouble(bytesToLong(b));

    }

    public static String transferLongToDate(Long millSec) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.mm.DD hh:mm:ss");
        Date date = new Date(millSec);
        return sdf.format(date);
    }

    public static byte[] strToHex(String value) {

        byte[] data = new byte[value.length() / 2];
        for (int i = 0; i < data.length; i++) {

            data[i] = (byte) hexStringToInt(value.substring(2 * i, 2 * i + 2));

        }

        return data;
    }

    private static int hexStringToInt(String value) {
        return Integer.valueOf(value, 16).intValue();
    }

    /**
     * @param content 需要进行转化的字符串
     * @param length  返回的字节数组长度
     * @return 返回的字节长度，如果长度不够，则在字符串之前加上空格“ ”
     */
    public static byte[] stringToBytes(String content, int length) {

        byte[] result = new byte[length];

        try {
            while ((result = content.getBytes("gbk")).length < length) {
                content = content + " ";
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return result;

    }

    /**
     * 把16进制字符串转换成字节数组
     *
     * @param hex
     * @return byte[]
     */
    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    /**
     * 字节数组转换为16进制字符串
     *
     * @param src
     * @return
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 字符转换为Byte
     *
     * @param c
     * @return
     */
    private static int toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

    /**
     * 格式化时间：yyyy-MM-dd HH:mm:ss
     *
     * @param current
     * @return
     */
    public static String formatTime(long current) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(current));

    }

    public static long strTime2Long(String time) {

        if (TextUtils.isEmpty(time))
            return 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;


    }
}
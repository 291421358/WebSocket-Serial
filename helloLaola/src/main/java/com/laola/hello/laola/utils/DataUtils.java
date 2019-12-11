package com.laola.hello.laola.utils;

public class DataUtils {
    public static String carryDigit(String strDig, Float Multiple) {
        if (null == strDig) {
            return "";
        }
        float v = Float.parseFloat(strDig);
        v = v / Multiple;

        return String.valueOf(v);
    }

    public static void main(String[] args) {
        String s = carryDigit("10", 100F);
        System.out.println(s);
    }

    /**
     * 将十六进制的字符串转换成字节数组	 *	 * @param hexString	 * @return
     */
    public static byte[] hexStrToBinaryStr(String hexString) {
        if (hexString.equals("")) {
            return null;
        }
        hexString = hexString.replaceAll(" ", "");
        int len = hexString.length();
        int index = 0;
        byte[] bytes = new byte[len / 2];
        while (index < len) {
            String sub = hexString.substring(index, index + 2);
            bytes[index / 2] = (byte) Integer.parseInt(sub, 16);
            index += 2;
        }
        return bytes;
    }


    private static final char[] HEX_CHARS = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
    /*     * byte[]数组转十六进制     */
    public static String bytes2hexStr(byte[] bytes) {
        int len = bytes.length;
        if (len == 0) {
            return null;
        }
        char[] cbuf = new char[len * 2];
        for (int i = 0; i < len; i++) {
            int x = i * 2;
            cbuf[x] = HEX_CHARS[(bytes[i] >>> 4) & 0xf];
            cbuf[x + 1] = HEX_CHARS[bytes[i] & 0xf];
        }
        return new String(cbuf);
    }
}

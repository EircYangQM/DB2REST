package com.db2rest.server.utils;

import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utilities {
  private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

  public static boolean isNullOrEmpty(String value) {
    return value == null || "".equals(value);
  }

  public static boolean equalsIgnoreCase(String value1, String value2) {
    if (value1 == null) {
      return value2 == null;
    } else {
      return toUpperCase(value1).equals(toUpperCase(value2));
    }
  }

  public static String toUpperCase(String value) {
    return value == null ? null : value.toUpperCase();
  }

  public static String toLowerCase(String value) {
    return value == null ? null : value.toLowerCase();
  }

  public static String fixedLengthString(String string, int length) {
    return fixedLengthString(string, length, false);
  }

  public static String fixedLengthString(String string, int length, boolean ltr) {
    if (ltr) {
      return String.format("%-"+length+ "s", string);
    } else {
      return String.format("%"+length+ "s", string);
    }
  }

  public static String getDateString(Date date, String format) {
    DateFormat dateFormat = new SimpleDateFormat(format);
    return dateFormat.format(date);
  }

  public static String formatInterval(long ms){
    long millis = ms % 1000;
    long x = ms / 1000;
    long seconds = x % 60;
    x /= 60;
    long minutes = x % 60;
    x /= 60;
    long hours = x % 24;
    return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, millis);
  }

  public static int getValueAsInt(String value, int defaultValue) {
    try {
      return Integer.parseInt(value);
    } catch (Exception ex) {
      return defaultValue;
    }
  }

  public static boolean getValueAsBoolean(String value, boolean defaultValue) {
    try {
      return Boolean.parseBoolean(value);
    } catch (Exception ex) {
      return defaultValue;
    }
  }

  public static String bytesToHex(byte[] bytes) {
    char[] hexChars = new char[bytes.length * 2];
    for (int j = 0; j < bytes.length; j++) {
      int v = bytes[j] & 0xFF;
      hexChars[j * 2] = HEX_ARRAY[v >>> 4];
      hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
    }
    return new String(hexChars);
  }

  public static byte[] hexToBytes(String s) {
    int len = s.length();
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
      data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
          + Character.digit(s.charAt(i+1), 16));
    }
    return data;
  }

  public static int bytesToInt(byte[] bytes) {
    byte[] intBytes = new byte[4];
    for (int i = 0; i < 4; i++) {
      if (bytes.length > i) {
        intBytes[3 - i] = bytes[i];
      }
    }
    ByteBuffer wrapped = ByteBuffer.wrap(intBytes);
    return wrapped.getInt();
  }
}


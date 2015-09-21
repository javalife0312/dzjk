package com.template.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class Type {

	/**
	 * 字符串
	 */
	public static final String STRING = "string";
	/**
	 * 整型
	 */
	public static final String INTEGER = "integer";
	/**
	 * 单精度浮点
	 */
	public static final String FLOAT = "float";
	/**
	 * 双精度浮点
	 */
	public static final String DOUBLE = "double";
	/**
	 * 日期型，不带时间
	 */
	public static final String DATE = "date";
	/**
	 * 日期时间型，带时间
	 */
	public static final String DATETIME = "datetime";

	public static final String BOOLEAN = "boolean";

	public static int getFinalMaxID(int id) {

		return id == 0 ? 1 : id;
	}

	public static int GetInt(Object obj) {
		if (obj == null || "".equals(obj))
			return 0;
		// return ((Integer) obj).intValue();
		return Integer.parseInt(String.valueOf(obj));
	}

	public static Integer getInteger(Object obj) {
		return (obj == null || "".equals(obj)) ? Integer.valueOf(0) : Integer
				.valueOf(String.valueOf(obj));
	}

	public static double GetDouble(Object obj) {
		// if (obj == null)
		// return 0.0;
		// return ((Double) obj).doubleValue();
		//

		return (obj == null || "".equals(obj)) ? Double.valueOf(0)
				.doubleValue() : Double.valueOf(String.valueOf(obj))
				.doubleValue();
	}

	public static float GetFloat(Object obj) {

		return (obj == null || "".equals(obj)) ? Float.valueOf(0).floatValue()
				: Float.valueOf(String.valueOf(obj)).floatValue();
	}

	public static Timestamp GetDate(Object obj) {
		System.out.print("123");
		if (obj == null)
			return Timestamp.valueOf("1770-1-1");
		else
			return (Timestamp) obj;

	}

	public static long getLong(Object obj) {

		return obj == null ? Long.parseLong("0") : Long.parseLong(String
				.valueOf(obj).trim());
	}

	public static String getString(Object obj) {
		if (obj == null) {
			return "";
		}
		String r = String.valueOf(obj).trim();
		return r;
		// if (r.indexOf('\"') == -1) {
		// return r;
		// }
		//
		//	return StringUtils.replace(r, "\"", "\\\"");
	}

	public static String getString(int a) {
		return String.valueOf(a);
	}


	public static int convertBinToInt(Object obj) {

		return obj == null ? 0
				: (Boolean.parseBoolean(String.valueOf(obj)) == true ? 1 : 0);
	}

	public static Timestamp getTimestamp(Object obj) {

		return obj == null ? null : (Timestamp) obj;

	}

	public static boolean getBoolean(Object obj) {

		try {
			return GetInt(obj) == 0 ? false : true;
		} catch (Exception e) {
			return Boolean.valueOf(String.valueOf(obj)).booleanValue();
		}

	}

	public static Object get(String type, String value) {
		if (STRING.equals(type)) {
			return value;
		} else if (INTEGER.equals(type)) {
			return Integer.valueOf(value);
		} else if (FLOAT.equals(type)) {
			return Float.valueOf(value);
		} else if (DOUBLE.equals(type)) {
			return Double.valueOf(value);
		} else if (DATETIME.equals(type)) {
			try {
				return DateUtil.parseDateTime(value);
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		} else {
			return value;
		}
	}


	public static boolean isType(String value, String type) {
		if (StringUtils.isEmpty(type) || StringUtils.isEmpty(value)) {
			return false;
		}
		if (STRING.equals(type)) {
			return true;
		} else if (INTEGER.equals(type)) {
			return Pattern.matches("^-?\\d+$", value);
		} else if (FLOAT.equals(type)) {
			return Pattern.matches("^-?\\d*\\.?\\d*$", value);
		} else if (DOUBLE.equals(type)) {
			return Pattern.matches("^-?\\d*\\.?\\d*$", value);
		} else if (DATE.equals(type)) {
			return Pattern.matches("^\\d{4}" + "-((0([1-9]{1}))|(1[1|2]))"
					+ "-(([0-2]([1-9]{1}))|(3[0|1]))$", value);
		} else if (DATETIME.equals(type)) {
			return Pattern.matches("^\\d{4}" + "-((0([1-9]{1}))|(1[1|2]))"
					+ "-(([0-2]([1-9]{1}))|(3[0|1]))\\s"
					+ "((0[0-9])|(1[0-9])|(2[0-3]))" + ":([0-5][0-9])"
					+ ":([0-5][0-9])$", value);
		}
		return false;
	}

	public static void main(String[] args) {
		System.out.println(isType("2010-03-31 00:18:00", DATETIME));
	}
}

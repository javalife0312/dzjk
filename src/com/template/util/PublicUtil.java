package com.template.util;

import java.lang.reflect.Field;
import java.util.Date;

import javassist.Modifier;
import net.sf.json.JSONArray;


public class PublicUtil {

	public static String getFieldJSON(Class clazz) {
		Field[] fields = clazz.getDeclaredFields();
		JSONArray array = new JSONArray();
		for (Field field : fields) {
			if (!Modifier.isTransient(field.getModifiers())
					&& !Modifier.isStatic(field.getModifiers())) {
				String fieldType = getFieldType(field.getType());
				DataField dataField = null;

				if (fieldType.equals("date")) {
					dataField = new DateField(field.getName(), field.getName(),
							fieldType);
					((DateField) dataField).setDateFormat("Y-m-d H:i:s");
					array.add(dataField);
				} else {
					dataField = new DataField(field.getName(), field.getName(),
							fieldType);
					array.add(dataField);
				}

			}
		}
		return array.toString();
	}

	public static String getFieldType(Class clazz) {
		if (clazz == String.class) {
			return "string";
		} else if (clazz == int.class || clazz == Integer.class
				|| clazz == Long.class) {
			return "int";
		} else if (clazz == float.class || clazz == Float.class
				|| clazz == Double.class) {
			return "float";
		} else if (clazz == Date.class || clazz == java.sql.Date.class) {
			return "date";
		} else if (clazz == boolean.class || clazz == Boolean.class) {
			return "boolean";
		} else {
			return "auto";
		}
	}

}

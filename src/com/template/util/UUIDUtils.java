package com.template.util;

import org.apache.commons.lang.StringUtils;

public class UUIDUtils {
	public static String nextCode() {
		return StringUtils.replace(java.util.UUID.randomUUID().toString()
				.toLowerCase(), "-", "");
	}

	public static void main(String[] args) {
		System.out.println(nextCode());
	}
}

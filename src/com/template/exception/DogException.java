package com.template.exception;

/**
 * 自定义异常
 */
public class DogException extends Exception{

	
	private static final long serialVersionUID = 1L;

	public DogException() {
		System.out.println("系统没有使用加密狗..................");
	}
	
}

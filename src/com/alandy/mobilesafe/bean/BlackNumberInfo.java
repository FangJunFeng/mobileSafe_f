package com.alandy.mobilesafe.bean;

/**
 * @author Fangjun
 * 黑名单实体类
 */
public class BlackNumberInfo {
	//黑名单电话号码
	private String number;
	/**
	 * 黑名单的拦截模式
	 * 1、全部拦截
	 * 2、电话拦截
	 * 3、短信拦截
	 * 
	 */
	private String mode;
	
	
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	
	
}

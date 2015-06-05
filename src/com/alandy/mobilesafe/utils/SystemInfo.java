package com.alandy.mobilesafe.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
/**
 * ============================================================
 *
 * 版 权 ： 小楫轻舟工作室 版权所有 (c) 2015
 *
 * 作 者 : 冯方俊
 *
 * 版 本 ： 1.0
 *
 * 创建日期 ： 2015年6月4日 下午8:29:41
 *
 * 描 述 ：
 *	判断当前的黑名单服务是否运行
 * 
 * 修订历史 ：
 *
 * ============================================================
 **/
public class SystemInfo {
	/**
	 * @param context 上下文
	 * @param name   当前服务的名字
	 * @return  如果已经开启返回true，否则false
	 */
	public static boolean isRunningService(Context context, String name){
		//获取到系统服务
		//进程管理器
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> runningServices = am.getRunningServices(100);
		for (RunningServiceInfo runningServiceInfo : runningServices) {
			//获取到服务的名字
			String className = runningServiceInfo.service.getClassName();
			//如果两个名字相等说明我们的服务已经开启
			if (name.equals(className)) {
				return true;
			}
		}
		return false;
	}
}

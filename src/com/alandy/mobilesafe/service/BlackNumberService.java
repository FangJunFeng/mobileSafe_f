package com.alandy.mobilesafe.service;

import com.alandy.mobilesafe.db.dao.BlackNumberDao;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.SmsMessage;

public class BlackNumberService extends Service {

	private BlackNumberDao dao;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		dao = new BlackNumberDao(this);
		// 短信的过滤器
		IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
		// 设置优先级
		filter.setPriority(Integer.MAX_VALUE);
		
		InnerSmsreceiver receiver = new InnerSmsreceiver();
		//注册一个短信的广播
		registerReceiver(receiver , filter);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	private class InnerSmsreceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			System.out.println("接收到短信");		
			Object[] obj = (Object[]) intent.getExtras().get("pdus");
			for (Object object : obj) {
				SmsMessage smsMessage = SmsMessage.createFromPdu((byte[])object);
				//获取到电话号码
				String number = smsMessage.getOriginatingAddress();
				// 根据电话号码查询拦截的模式
				String mode = dao.findBlackMode(number);
				
				/**
				 * 黑名单的拦截模式 1 全部拦截 2 电话拦截 3 短信拦截
				 */
				if (mode.equals("1") || mode.equals("3")) {
					System.out.println("被哥拦截了");
					abortBroadcast();
				}
			}
		}
		
	}
}

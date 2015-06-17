package com.alandy.mobilesafe.service;

import java.lang.reflect.Method;

import com.alandy.mobilesafe.db.dao.BlackNumberDao;
import com.android.internal.telephony.ITelephony;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

public class BlackNumberService extends Service {

	private BlackNumberDao dao;
	private TelephonyManager tm;
	private InnerSmsreceiver receiver;
	private MyPhoneStateListener listener;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		dao = new BlackNumberDao(this);

		// 获取到电话的管理者
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listener = new MyPhoneStateListener();
		// 第二个参数表示电话拨打的状态
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

		// 短信的过滤器
		IntentFilter filter = new IntentFilter(
				"android.provider.Telephony.SMS_RECEIVED");
		// 设置优先级
		filter.setPriority(Integer.MAX_VALUE);

		receiver = new InnerSmsreceiver();
		// 注册一个短信的广播
		registerReceiver(receiver, filter);
	}

	@Override
	public void onDestroy() {
		// 反注册掉广播
		unregisterReceiver(receiver);
		receiver = null;
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
		listener = null;
		super.onDestroy();
	}

	private class MyPhoneStateListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// 第一个参数表示电话状态
			// 第二个参数表示电话号码
			super.onCallStateChanged(state, incomingNumber);
			// 闲置状态
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:
				System.out.println("CALL_STATE_IDLE");
				break;
			// 电话接通状态
			case TelephonyManager.CALL_STATE_OFFHOOK:
				System.out.println("CALL_STATE_OFFHOOK");
				break;
			// 电话响铃状态
			case TelephonyManager.CALL_STATE_RINGING:
				System.out.println("CALL_STATE_RINGING");
				// 根据电话号码得到查询模式
				String mode = dao.findBlackMode(incomingNumber);
				/**
				 * 黑名单的拦截模式 1 全部拦截 2 电话拦截 3 短信拦截
				 */
				if (mode.equals("1") || mode.equals("2")) {
					System.out.println("被哥电话拦截了");
					// 所有的内容观察者开头都是content,call_log表示主机名
					Uri uri = Uri.parse("content://call_log/calls");
					// 注册一个内容观察者
					// 观察电话的数据库
					// 第一个参数：uri
					// 第二个参数：true表示前缀满足
					getContentResolver().registerContentObserver(uri, true,
							new CallLogObserver(new Handler(), incomingNumber));
					// 挂断电话
					endCall();
				}
				break;
			}
		}

	}

	private class CallLogObserver extends ContentObserver {
		// incomingNumber表示电话号码
		private String incomingNumber;

		public CallLogObserver(Handler handler, String incomingNumber2) {
			super(handler);
			this.incomingNumber = incomingNumber2;
		}

		/**
		 * 如果当前数据库发生改变的时候。调用当前的方法
		 */
		@Override
		public void onChange(boolean selfChange) {
			// TODO Auto-generated method stub
			super.onChange(selfChange);
			getContentResolver().unregisterContentObserver(this);
			deleteCallLog(incomingNumber);
		}

	}

	private class InnerSmsreceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			System.out.println("接收到短信");
			Object[] obj = (Object[]) intent.getExtras().get("pdus");
			for (Object object : obj) {
				SmsMessage smsMessage = SmsMessage
						.createFromPdu((byte[]) object);
				// 获取到电话号码
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

				// 智能拦截
				String body = smsMessage.getMessageBody();
				if (body.contains("mai fang zi")) {
					System.out.println("被哥智能拦截到了！");
					abortBroadcast();
				}
			}
		}

	}

	/**
	 * 挂断电话
	 */
	public void endCall() {
		try {
			// 应用反射，获取到一个类的加载器
			Class<?> clazz = getClassLoader().loadClass(
					"android.os.ServiceManager");
			// 调用ServiceManager里面的方法
			// 第一个参数表示方法名字
			// 第二个参数表示参数的类型
			Method method = clazz.getDeclaredMethod("getService", String.class);
			IBinder mIBinder = (IBinder) method.invoke(null, TELEPHONY_SERVICE);
			// 通过ibinder对象构建一个aidl
			ITelephony telephony = ITelephony.Stub.asInterface(mIBinder);
			// 挂断电话
			telephony.endCall();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 删除电话号码
	 * @param incomingNumber
	 */
	public void deleteCallLog(String incomingNumber) {
		ContentResolver contentResolver = getContentResolver();
		Uri uri = Uri.parse("content://call_log/calls");
		contentResolver.delete(uri, "number = ?", new String[]{incomingNumber});
	}
}

package com.alandy.mobilesafe;

import java.util.List;

import com.alandy.mobilesafe.adapter.BlackNumberAdapter;
import com.alandy.mobilesafe.bean.BlackNumberInfo;
import com.alandy.mobilesafe.db.dao.BlackNumberDao;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @author Fangjun
 *
 */
public class BlackNumberActivity extends Activity implements OnClickListener {
	private Button bt_add_black_number, bt_open_setting;
	private ListView lv_black_number;
	private BlackNumberDao dao;
	private List<BlackNumberInfo> blackNumberInfos;
	private AlertDialog dialog;
	private BlackNumberAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//设置当前界面没有标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_black_number);
		
		initView();
		initData();
	}
	
	private void initView() {
		//添加黑名单
		bt_add_black_number = (Button) this.findViewById(R.id.bt_add_black_number);
		bt_add_black_number.setOnClickListener(this);
		//设置按钮
		bt_open_setting = (Button) this.findViewById(R.id.bt_open_setting);
		bt_open_setting.setOnClickListener(this);
		//列表控件
		lv_black_number = (ListView) this.findViewById(R.id.lv_black_number);
		
	}
	
	private void initData() {
		dao = new BlackNumberDao(this);
		new Thread(){
			

			public void run(){
				blackNumberInfos = dao.findAll();
				//子线程不能刷新UI
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// 运行到主线程
						adapter = new BlackNumberAdapter(BlackNumberActivity.this, blackNumberInfos, dao);
						lv_black_number.setAdapter(adapter);
					}
				});
			}
		}.start();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//添加黑名单
		case R.id.bt_add_black_number:
			addBlackNumber();
			break;
		// 打开设置界面
		case R.id.bt_open_setting:
			openSetting();
			break;	

		default:
			break;
		}
	}

	/**
	 * 打开设置界面
	 */
	private void openSetting() {
		Intent intent = new Intent(this, BlackNumberSettingActivity.class);
		startActivity(intent);
	}

	/**
	 * 添加黑名单电话号码
	 */
	private void addBlackNumber() {
		AlertDialog.Builder builder = new Builder(this);
		View view = View.inflate(this, R.layout.dialog_add_black_number, null);
		Button bt_ok = (Button) view.findViewById(R.id.bt_ok);
		Button bt_cancle = (Button) view.findViewById(R.id.bt_cancle);
		final EditText et_phone = (EditText) view.findViewById(R.id.et_phone);
		final CheckBox cb_phone = (CheckBox) view.findViewById(R.id.cb_phone);
		final CheckBox cb_sms = (CheckBox) view.findViewById(R.id.cb_sms);
		
		bt_ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				BlackNumberInfo info = new BlackNumberInfo();
				String str_phone = et_phone.getText().toString().trim();
				if (TextUtils.isEmpty(str_phone)) {
					Toast.makeText(BlackNumberActivity.this, "请输入电话号码！", 0).show();
					return;
				}
				info.setNumber(str_phone);
				/**
				 * 黑名单的拦截模式 1 全部拦截 2 电话拦截 3 短信拦截
				 */
				String mode = "";
				if (cb_phone.isChecked() && cb_sms.isChecked()) {
					mode = "1";
				}else if(cb_phone.isChecked()){
					mode = "2";
				}else if (cb_sms.isChecked()) {
					mode = "3";
				} else {
					Toast.makeText(BlackNumberActivity.this, "必须选择一种拦截模式", 0).show();
					return;
				}
				
				info.setMode(mode);
				dao.add(str_phone, mode);
				//把黑名单添加到集合里面去
				blackNumberInfos.add(info);
				
				adapter.notifyDataSetChanged();
				dialog.dismiss();
			}
		});
		
		bt_cancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
		builder.setView(view);
		dialog = builder.show();
	}
}

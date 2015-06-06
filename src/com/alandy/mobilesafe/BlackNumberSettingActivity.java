package com.alandy.mobilesafe;

import com.alandy.mobilesafe.service.BlackNumberService;
import com.alandy.mobilesafe.utils.SystemInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * ============================================================
 *
 * 版 权 ： 小楫轻舟工作室 版权所有 (c) 2015
 *
 * 作 者 : 冯方俊
 *
 * 版 本 ： 1.0
 *
 * 创建日期 ： 2015年6月3日 下午4:11:50
 *
 * 描 述 ： 黑名单设置界面
 * 
 * 修订历史 ：
 *
 * ============================================================
 **/
public class BlackNumberSettingActivity extends Activity {
	private RelativeLayout rl_black_number;
	private TextView tv_desc;
	private CheckBox cb_state;
	private Intent black_intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_black_number_setting);
		initUI();
		initData();
	}

	private void initData() {
		black_intent = new Intent(this, BlackNumberService.class);
		rl_black_number.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (cb_state.isChecked()) {
					tv_desc.setText("已经关闭");
					cb_state.setChecked(false);
					stopService(black_intent);
				} else {
					cb_state.setChecked(true);
					tv_desc.setText("已经开启");
					startService(black_intent);
				}
			}
		});
	}

	private void initUI() {
		rl_black_number = (RelativeLayout) findViewById(R.id.rl_black_number);
		tv_desc = (TextView) findViewById(R.id.tv_desc);
		cb_state = (CheckBox) findViewById(R.id.cb_state);
	}

	@Override
	protected void onStart() {
		super.onStart();
		boolean result = SystemInfo.isRunningService(this,
				"com.alandy.mobilesafe.service.BlackNumberService");
		if (result) {
			cb_state.setChecked(true);
		}else {
			cb_state.setChecked(false);
		}
		if (cb_state.isChecked()) {
			tv_desc.setText("已经开启");
		}else {
			tv_desc.setText("已经关闭");
		}
	}
}

package com.alandy.mobilesafe;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
/**
 * ============================================================
 *
 * 版 权 ： 小楫轻舟工作室 版权所有 (c) 2015
 *
 * 作 者 : 冯方俊
 *
 * 版 本 ： 1.0
 *
 * 创建日期 ： 2015年6月6日 下午12:15:50
 *
 * 描 述 ：
 *	主界面
 * 
 * 修订历史 ：
 *
 * ============================================================
 **/
public class MainActivity extends ActionBarActivity implements OnClickListener {

	private Button bt_main_software_icon;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
	}

	private void initView() {
		//黑名单拦截
		Button main_sync_icon = (Button) findViewById(R.id.main_sync_icon);
		main_sync_icon.setOnClickListener(this);
		bt_main_software_icon = (Button) findViewById(R.id.bt_main_software_icon);
		bt_main_software_icon.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//黑名单拦截
		case R.id.main_sync_icon:
			intent = new Intent(this, BlackNumberActivity.class);
			startActivity(intent);
			break;
		//软件管理
		case R.id.bt_main_software_icon:
			intent = new Intent(this, SoftwareActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
}

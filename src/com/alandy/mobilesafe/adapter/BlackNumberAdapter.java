package com.alandy.mobilesafe.adapter;

import java.util.List;

import com.alandy.mobilesafe.BlackNumberActivity;
import com.alandy.mobilesafe.R;
import com.alandy.mobilesafe.bean.BlackNumberInfo;
import com.alandy.mobilesafe.db.dao.BlackNumberDao;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BlackNumberAdapter extends BaseAdapter {
	private List<BlackNumberInfo> blackNumberInfos;
	private Context context;
	private BlackNumberDao dao;
	public BlackNumberAdapter(Context context,
			List<BlackNumberInfo> blackNumberInfos, BlackNumberDao dao) {
		this.blackNumberInfos = blackNumberInfos;
		this.context = context;
		this.dao = dao;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return blackNumberInfos.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return blackNumberInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.item_black_number, null);
		}
		TextView tv_number = (TextView) convertView.findViewById(R.id.tv_number);
		TextView tv_mode = (TextView) convertView.findViewById(R.id.tv_mode);
		ImageView image_delete = (ImageView) convertView.findViewById(R.id.image_delete);
		tv_number.setText(blackNumberInfos.get(position).getNumber());
		String mode = blackNumberInfos.get(position).getMode();
		if (mode.equals("1")) {
			tv_mode.setText("全部拦截");
		}else if (mode.equals("2")) {
			tv_mode.setText("电话拦截");
		}else if (mode.equals("3")) {
			tv_mode.setText("短信拦截");
			
		}
		final BlackNumberInfo blackNumberInfo = blackNumberInfos.get(position);
		image_delete.setOnClickListener(new OnClickListener() {
			//根据电话号码进行删除
			@Override
			public void onClick(View v) {
				String number = blackNumberInfo.getNumber();
				boolean result = dao.delete(number);
				if (result) {
					Toast.makeText(context, "删除成功", 0).show();
					blackNumberInfos.remove(blackNumberInfo);
					//刷新界面
					notifyDataSetChanged();
				}else {
					Toast.makeText(context, "删除失败", 0).show();
				}
			}
		});
		return convertView;
	}

}

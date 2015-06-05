package com.alandy.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Fangjun 
 * 
 * 创建黑名单的数据库和黑名单的表
 */
public class BlackNumberOpenHelper extends SQLiteOpenHelper {

	public BlackNumberOpenHelper(Context context) {
		// 第一个参数表示上下文
		// 第二个参数表示数据库的名字
		// 第三个参数表示游标工厂
		// 第四个参数表示版本号必须大于0
		super(context, "callsafe.db", null, 1);
		// TODO Auto-generated constructor stub
	}

	/**
     * mode 表示拦截的模式
     * number 表示黑名单的电话号码
     */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table blacknumber (_id integer primary key autoincrement, number varchar(20), mode varchar(2))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}

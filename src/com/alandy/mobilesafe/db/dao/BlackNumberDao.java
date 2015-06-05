package com.alandy.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ListView;

import com.alandy.mobilesafe.bean.BlackNumberInfo;
import com.alandy.mobilesafe.db.BlackNumberOpenHelper;

public class BlackNumberDao {
	private BlackNumberOpenHelper helper;
	public BlackNumberDao(Context context) {
		helper = new BlackNumberOpenHelper(context);
	}
	
	/**
	 * 添加黑名单
	 * 
	 * @param number
	 *            电话号码
	 * @param mode
	 *            拦截模式
	 * @return
	 */
	public boolean add(String number, String mode) {
		SQLiteDatabase database = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("number", number);
		values.put("mode", mode);
		long rowID = database.insert("blacknumber", null, values);
		if (rowID == -1) {
			return false;			
		}else {
			return true;
		}

	}
	
	/**
	 * 根据电话号码查询拦截的模式
	 * @param number
	 * @return
	 */
	public String findBlackMode(String number){
		String mode = "0";
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query("blacknumber", new String[]{"mode"}, "number=?",
				new String[]{number}, null, null, null);
		if (cursor.moveToNext()) {
			mode = cursor.getString(0);
		}
		cursor.close();
		db.close();
		return mode;
	}
	/**
	 * 根据电话号码进行删除
	 * 
	 * @param number
	 *            电话号码
	 * @return
	 */
	public boolean delete(String number){
		SQLiteDatabase database = helper.getWritableDatabase();
		int rowNumber = database.delete("blacknumber", "number = ?", new String[]{number});
		// 如果当前的rownumber等于0表示一行都影响不了
		if (rowNumber == 0) {
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * 根据电话号码修改拦截模式
	 * 
	 * @param number
	 *            电话号码
	 * @param newMode
	 *            拦截模式
	 * @return
	 */
	public boolean changeNumberMode(String number, String newMode){
		SQLiteDatabase database = helper.getWritableDatabase();
		ContentValues values = new  ContentValues();
		values.put("mode", newMode);
		int rowNumber = database.update("blacknumber", values, "number = ?", new String[]{number});
		if (rowNumber == 0) {
			return false;
		}else {
			return true;
		}
	}
	
	/**
	 * 查询当前数据库里面的所有黑名单电话号码
	 * @return
	 */
	public List<BlackNumberInfo> findAll(){
		SQLiteDatabase database = helper.getReadableDatabase();
		List<BlackNumberInfo> lists = new ArrayList<BlackNumberInfo>();
		Cursor cursor = database.query("blacknumber", new String[]{"number", "mode"},
				null, null, null, null, null);
		while(cursor.moveToNext()){
			BlackNumberInfo info = new BlackNumberInfo();
			info.setNumber(cursor.getString(0));
			info.setMode(cursor.getString(1));
			lists.add(info);
		}
		cursor.close();
		database.close();
		return lists;
			
	}
}

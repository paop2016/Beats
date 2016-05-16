package wang.beats.db;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.widget.Toast;
import wang.beats.R;
import wang.beats.dao.Friend;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

public class MyDatabaseHelper extends SQLiteOpenHelper{
	
	private Context context;
	private static MyDatabaseHelper dbHelper;
	private ArrayList<String> positions;
	private HashMap<String, Integer> map;
	private String nowPeople="0";
	private int dataCount=0;
	private static String CREATE_All_DATA="create table AllData("
			+ "id integer primary key autoincrement,"
			+ "people varchar,"
			+ "time varchar,"
			+ "latitude varchar,"
			+ "longitude varchar,"
			+ "position varchar)";
	private static String CREATE_PEOPLE_COUNT_DATA="create table PeopleCountData("
			+ "id integer primary key autoincrement,"
			+ "people varchar,"
			+ "position varchar,"
			+ "count Integer)";
	private static String CREATE_PEOPLE_COUNT_OVER2_DATA="create table PeopleCountOver2Data("
			+ "id integer primary key autoincrement,"
			+ "people varchar,"
			+ "position varchar,"
			+ "count Integer)";
	private static String CREATE_POSITION_DATA="create table PositionData("
			+ "id integer primary key autoincrement,"
			+ "position varchar,"
			+ "latitude varchar,"
			+ "longitude varchar)";
	private static String CREATE_PEOPLE_Simple_DATA="create table PeopleSimpleData("
			+ "id integer primary key autoincrement,"
			+ "people varchar,"
			+ "position varchar)";
	private static String CREATE_PEOPLE_DATA="create table PeopleData("
			+ "id integer primary key autoincrement,"
			+ "people varchar,"
			+ "count varchar)";
	private static String CREATE_FRIEND_DATA="create table FriendData("
			+ "id integer primary key autoincrement,"
			+ "people varchar,"
			+ "friend varchar)";
	private static String CREATE_CONSEQUENCE_DATA="create table ConsequenceData("
			+ "id integer primary key autoincrement,"
			+ "people varchar,"
			+ "count Integer,"
			+ "j Integer,"
			+ "c Integer,"
			+ "m Integer,"
			+ "jave Integer,"
			+ "cave Integer,"
			+ "mave Integer,"
			+ "jcount Integer,"
			+ "ccount Integer,"
			+ "mcount Integer,"
			+ "consequence varchar)";
	private MyDatabaseHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
		this.context = context;
		positions=new ArrayList<String>();
		map = new HashMap<String, Integer>();
	}
	public static SQLiteDatabase getDatabase(Context context) {
		dbHelper = new MyDatabaseHelper(context, "BeatsData", null, 128);
		return dbHelper.getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_All_DATA);
		db.execSQL(CREATE_PEOPLE_DATA);
		db.execSQL(CREATE_POSITION_DATA);
		db.execSQL(CREATE_PEOPLE_COUNT_DATA);
		db.execSQL(CREATE_PEOPLE_Simple_DATA);
		db.execSQL(CREATE_PEOPLE_COUNT_OVER2_DATA);
		db.execSQL(CREATE_FRIEND_DATA);
		db.execSQL(CREATE_CONSEQUENCE_DATA);
		loadPeopleData(db);
		loadPeopleData1(db);
		final SQLiteDatabase db1 = db;
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				initDatabase(db1);
			}
		}).start();
	}

	private void loadPeopleData1(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		BufferedReader br = null;
		String str;
		File file = new File(Environment.getExternalStorageDirectory() + "/data/friendValidData.txt");
		FileReader fr;
		try {
			fr = new FileReader(file);
			if (fr != null) {
				br = new BufferedReader(fr);
				ContentValues cv_friend = new ContentValues();
				while ((str = br.readLine()) != null) {
					str.trim();
					String[] arr = str.split("\\s+");
					String people = arr[0];
					String friend = arr[1];
					cv_friend.put("people", people);
					cv_friend.put("friend", friend);
					db.insert("FriendData", null, cv_friend);
				}
			}
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void loadPeopleData(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		BufferedReader br = null;
		String str;
		File file = new File(Environment.getExternalStorageDirectory() + "/data/peopleValidData.txt");
		try {
			FileReader fr = new FileReader(file);
			if (fr != null) {
				br = new BufferedReader(fr);
				ContentValues cv_all = new ContentValues();
				ContentValues cv_count = new ContentValues();
				ContentValues cv_simple = new ContentValues();
				ContentValues cv_positon = new ContentValues();
				ContentValues cv_people = new ContentValues();
				while ((str = br.readLine()) != null) {
					str.trim();
					String[] arr = str.split("\\s+");
					String id = arr[0];
					String time = arr[1];
					String latitude = arr[2];
					String longitude = arr[3];
					String position = arr[4];
					if (!nowPeople.equals(arr[0])) {
						List<Entry<String, Integer>> entrys = new ArrayList<Entry<String, Integer>>(map.entrySet());
						Collections.sort(entrys, new Comparator<Entry<String, Integer>>() {
							@Override
							public int compare(Entry<String, Integer> en1, Entry<String, Integer> en2) {
								// TODO Auto-generated method stub
								return en2.getValue() - en1.getValue();
							}
						});
						Iterator<Entry<String, Integer>> it = entrys.iterator();
						while (it.hasNext()) {
							Entry<String, Integer> entry = it.next();
							cv_count.put("position", entry.getKey());
							cv_simple.put("position", entry.getKey());
							cv_count.put("count", entry.getValue());
							cv_count.put("people", nowPeople);
							cv_simple.put("people", nowPeople);
							db.insert("PeopleCountData", null, cv_count);
							db.insert("PeopleSimpleData", null, cv_simple);
							if (entry.getValue() > 2)
								db.insert("PeopleCountOver2Data", null, cv_count);
						}
						cv_people.put("count", dataCount);
						cv_people.put("people", nowPeople);
						db.insert("PeopleData", null, cv_people);
						nowPeople = id;
						map.clear();
						dataCount = 0;
					}
					
					cv_all.put("people", id);
					cv_all.put("time", time);
					cv_all.put("latitude", latitude);
					cv_all.put("longitude", longitude);
					dataCount++;
					int index;
					index = positions.indexOf(position);
					if (index >= 0) {
						cv_all.put("position", index);
						if (map.get(index + "") == null) {
							map.put(index + "", Integer.valueOf(1));
						} else
							map.put(index + "", Integer.valueOf((map.get(index + "").intValue() + 1)));
					} else {
						int size = positions.size();
						positions.add(position);
						cv_all.put("position", size);
						map.put(size + "", Integer.valueOf(1));
						cv_positon.put("position", size);
						cv_positon.put("latitude", latitude);
						cv_positon.put("longitude", longitude);
						db.insert("PositionData", null, cv_positon);
					}
					db.insert("AllData", null, cv_all);
				}
				cv_people.put("count", dataCount);
				cv_people.put("people", nowPeople);
				db.insert("PeopleData", null, cv_people);
				
				List<Entry<String, Integer>> entrys = new ArrayList<Entry<String, Integer>>(map.entrySet());
				Collections.sort(entrys, new Comparator<Entry<String, Integer>>() {
					@Override
					public int compare(Entry<String, Integer> en1, Entry<String, Integer> en2) {
						// TODO Auto-generated method stub
						return en2.getValue() - en1.getValue();
					}
				});
				Iterator<Entry<String, Integer>> it = entrys.iterator();
				while (it.hasNext()) {
					Entry<String, Integer> entry = it.next();
					cv_count.put("position", entry.getKey());
					cv_simple.put("position", entry.getKey());
					cv_count.put("count", entry.getValue());
					cv_count.put("people", nowPeople);
					cv_simple.put("people", nowPeople);
					db.insert("PeopleCountData", null, cv_count);
					db.insert("PeopleSimpleData", null, cv_simple);
					if (entry.getValue() > 2)
						db.insert("PeopleCountOver2Data", null, cv_count);
				}
			}
			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// db.setTransactionSuccessful();
			Toast.makeText(context, "错误", Toast.LENGTH_LONG).show();
		} finally {
			try {
				// db.endTransaction();
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("drop table if exists AllData");
		db.execSQL("drop table if exists PeopleCountData");
		db.execSQL("drop table if exists PeopleCountOver2Data");
		db.execSQL("drop table if exists PositionData");
		db.execSQL("drop table if exists PeopleSimpleData");
		db.execSQL("drop table if exists PeopleData");
		db.execSQL("drop table if exists FriendData");
		db.execSQL("drop table if exists ConsequenceData");
		onCreate(db);
		Toast.makeText(context, "version:" + newVersion, Toast.LENGTH_LONG).show();
	}
	private void initDatabase(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		int jCount2=0;
		int cCount2=0;
		int mCount2=0;
		int jCount1=0;
		int cCount1=0;
		int mCount1=0;
		for (int j = 0; j < 100; j++) {
			List<Friend> mJaccard = new ArrayList<Friend>();
			List<Friend> mCosine = new ArrayList<Friend>();
			List<Friend> mMix = new ArrayList<Friend>();
			HashMap<Integer, Integer> mCountMap = new HashMap<Integer, Integer>();
			// 地点集
			Map<Integer, int[]> positionMap = new HashMap<Integer, int[]>();
			// 用户平方和
			int userModulo = 0;
			// 好友平方和
			int friendModulo = 0;
			// 适量乘积 分子
			int numerator = 0;
			// 获得当前用户地点、签到数Map
			Cursor cursor = db.query("PeopleCountData", null, "people=?", new String[] { j + "" }, null, null, null);
			if (cursor.moveToFirst()) {
				do {
					int position = cursor.getInt(cursor.getColumnIndex("position"));
					int count = cursor.getInt(cursor.getColumnIndex("count"));
					mCountMap.put(position, count);
					userModulo += count * count;
				} while (cursor.moveToNext());
			}
			cursor.close();
			int sameNum = 0;
			int sumNum = 0;
			int sumInit = 0;
			// 获得当前用户签到总次数
			Cursor cursor2 = db.query("PeopleData", null, "people=?", new String[] { j + "" }, null, null, null);
			if (cursor2.moveToFirst()) {
				sumInit = cursor2.getInt(cursor2.getColumnIndex("count"));
			}
			cursor2.close();
			for (int i = 0; i < 100; i++) {
				if (i == j)
					continue;
				sumNum = sumInit;
				sameNum = 0;
				friendModulo = 0;
				numerator = 0;
				positionMap = new HashMap<Integer, int[]>();

				Iterator<Entry<Integer, Integer>> it = mCountMap.entrySet().iterator();
				while (it.hasNext()) {
					Entry<Integer, Integer> entry = it.next();
					int position = entry.getKey();
					int count = entry.getValue();
					positionMap.put(position, new int[] { count, 0 });
				}

				Cursor cursor1 = db.query("PeopleCountData", null, "people=?", new String[] { i + "" }, null, null,
						null);
				if (cursor1.moveToFirst()) {
					do {
						int position = cursor1.getInt(cursor1.getColumnIndex("position"));
						int count = cursor1.getInt(cursor1.getColumnIndex("count"));
						sumNum += count;
						friendModulo += count * count;
						if (mCountMap.containsKey(position)) {
							sameNum += Math.min(count, mCountMap.get(position).intValue());
						}

						if (positionMap.containsKey(position)) {
							positionMap.get(position)[1] = count;
						} else {
							positionMap.put(position, new int[] { 0, count });
						}
					} while (cursor1.moveToNext());
				}
				Iterator<Entry<Integer, int[]>> it1 = positionMap.entrySet().iterator();
				while (it1.hasNext()) {
					Entry<Integer, int[]> entry = it1.next();
					int count0 = entry.getValue()[0];
					int count1 = entry.getValue()[1];
					numerator += count0 * count1;
				}
				DecimalFormat df = new DecimalFormat("#0.0000000");
				Float jaccard = ((float) sameNum) / (sumNum - sameNum);
				Float cosine = (float) ((numerator) / (Math.sqrt(userModulo) * Math.sqrt(friendModulo)));
				// 算法部分 
				Float mix;
//				if(sumInit<=850){
//					mix=cosine;
//				}else{
//					mix=jaccard;
//				}
				mix =(float) (jaccard*sumInit*sumInit*sumInit*0.000001*0.0016283*3+cosine);
//				Float mix = jaccard * 8 + cosine;
				Friend friend = new Friend(R.drawable.j, i, df.format(jaccard));
				mJaccard.add(friend);
				Friend friend1 = new Friend(R.drawable.c, i, df.format(cosine));
				mCosine.add(friend1);
				Friend friend3 = new Friend(R.drawable.c, i, df.format(mix));
				mMix.add(friend3);
			}
			Collections.sort(mJaccard,new Comparator<Friend>(){
				
				@Override
				public int compare(Friend lhs, Friend rhs) {
					// TODO Auto-generated method stub
					return rhs.getSimilar().compareTo(lhs.getSimilar());
				}
			});
			Collections.sort(mCosine,new Comparator<Friend>(){
				
				@Override
				public int compare(Friend lhs, Friend rhs) {
					// TODO Auto-generated method stub
					return rhs.getSimilar().compareTo(lhs.getSimilar());
				}
			});
			Collections.sort(mMix,new Comparator<Friend>(){
				
				@Override
				public int compare(Friend lhs, Friend rhs) {
					// TODO Auto-generated method stub
					return rhs.getSimilar().compareTo(lhs.getSimilar());
				}
			});
			int jCount=0;
			int cCount=0;
			int mCount=0;
			Cursor cursor4 = db.query("FriendData", null, "people=?", new String[] { j + "" }, null, null,
					null);
			if (cursor4.moveToFirst()) {
				do {
					int friendName = cursor4.getInt(cursor4.getColumnIndex("friend"));
					for (int i = 0; i < mJaccard.size(); i++) {
						Friend friend = mJaccard.get(i);
						Friend friend1 = mCosine.get(i);
						Friend friend2 = mMix.get(i);
						if (friend.getName() == friendName) {
							// 位置
							jCount+=i+1;
						}
						if (friend1.getName() == friendName) {
							// 位置
							cCount+=i+1;
						}
						if (friend2.getName() == friendName) {
							// 位置
							mCount+=i+1;
						}
					}
				} while (cursor4.moveToNext());
			}
			cursor4.close();
			Cursor cursor5 = db.query("PeopleData", null, "people=?", new String[] { j + "" }, null, null, null);
			cursor5.moveToFirst();
			int userCount_1 = cursor5.getInt(cursor5.getColumnIndex("count"));
			cursor5.close();
			ContentValues cv = new ContentValues();
			cv.put("people", j);
			cv.put("count", userCount_1);
			cv.put("j", jCount);
			cv.put("c", cCount);
			cv.put("m", mCount);
			int min=getMin(jCount, cCount, mCount);
			if(jCount==min){
				jCount1++;
				if(jCount==cCount){
					if(jCount==mCount){
						cv.put("consequence", "jcm");
						mCount1++;
						cv.put("mcount", mCount1);
					}else{
						cv.put("consequence", "jc");
					}
					cCount1++;
					cv.put("ccount", cCount1);
				}
				else if(jCount==mCount){
					cv.put("consequence", "jm");
					mCount1++;
					cv.put("mcount", mCount1);
				}else{
//					jCount1++;
					jCount2+=userCount_1;
					cv.put("consequence", "j");
				}
				cv.put("jcount", jCount1);
			}else if(cCount==min){
				cCount1++;
				if(cCount==mCount){
					mCount1++;
					cv.put("consequence", "cm");
					cv.put("mcount", mCount1);
				}else{
//					cCount1++;
					cCount2+=userCount_1;
					cv.put("consequence", "c");
				}
				cv.put("ccount", cCount1);
			}else if(mCount==min){
				mCount1++;
				mCount2+=userCount_1;
				cv.put("consequence", "m");
				cv.put("mcount", mCount1);
			}
			if(jCount1!=0)
				cv.put("jave", jCount2/jCount1);
			if(cCount1!=0)
				cv.put("cave", cCount2/cCount1);
			if(mCount1!=0)
				cv.put("mave", mCount2/mCount1);
			db.insert("ConsequenceData", null, cv);
		}
	}
	private int getMin(int a, int b, int c){
		int min=a;
		if(b<min){
			min=b;
		}if(c<min){
			min=c;
		}
		return min;
	}

}

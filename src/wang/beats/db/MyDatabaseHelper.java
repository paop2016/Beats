package wang.beats.db;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.widget.Toast;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

public class MyDatabaseHelper extends SQLiteOpenHelper{
	
	private Context context;
	private static int mVersion;
	private static String mName="";
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
			+ "count int)";
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
	private MyDatabaseHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
		this.context = context;
		positions=new ArrayList<String>();
		map = new HashMap<String, Integer>();
	}
	public static SQLiteDatabase getDatabase(Context context, String name, int version) {
		if (dbHelper == null || mVersion != version || !mName.equals(name)) {
			dbHelper = new MyDatabaseHelper(context, name, null, version);
			mName = name;
			mVersion = version;
		}
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
		loadPeopleData(db);
		// Toast.makeText(context, "创建表PeopleData", Toast.LENGTH_LONG).show();
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
		onCreate(db);
		Toast.makeText(context, "version:" + newVersion, Toast.LENGTH_LONG).show();
	}

}

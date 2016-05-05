package wang.beats.activity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Text;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import charming.adapter.CommenViewHolder;
import wang.beats.R;
import wang.beats.adapter.ListAdapter;
import wang.beats.adapter.LoginAdapter;
import wang.beats.dao.User;
import wang.beats.db.MyDatabaseHelper;

public class LoginActivity extends Activity{
	private Spinner mSpinner;
	private ArrayAdapter mTitleAdpater;
	private LoginAdapter mSpinnerAdapter;
	private List<User> mUsers;
	private ImageView mGo;
	private User mSelectUser;
	SQLiteDatabase db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initView();
		initData();
		initFile2();
	}


	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void initData() {
		// TODO Auto-generated method stub
		db = MyDatabaseHelper.getDatabase(this);
		mUsers = new ArrayList<User>();
		ArrayList<Integer> arr = new ArrayList<Integer>();
		arr.add(R.drawable.pic5);
		arr.add(R.drawable.pic1);
		arr.add(R.drawable.pic2);
		arr.add(R.drawable.pic3);
		arr.add(R.drawable.pic4);
		arr.add(R.drawable.pic6);
		arr.add(R.drawable.pic7);
		arr.add(R.drawable.pic8);
		arr.add(R.drawable.pic9);
		arr.add(R.drawable.pic10);
		arr.add(R.drawable.pic1);
		arr.add(R.drawable.pic2);
		arr.add(R.drawable.pic3);
		arr.add(R.drawable.pic4);
		arr.add(R.drawable.pic6);
		arr.add(R.drawable.pic7);
		arr.add(R.drawable.pic8);
		arr.add(R.drawable.pic9);
		arr.add(R.drawable.pic10);
		for (int i = 6; i < 15; i++) {
			Cursor cursor = db.query("PeopleData", null, "people=?", new String[] { i + "" }, null, null, null);
			cursor.moveToFirst();
			int userCount_1 = cursor.getInt(cursor.getColumnIndex("count"));
			User user = new User(arr.get(i), i, userCount_1);
			mUsers.add(user);
		}
		mSpinnerAdapter = new LoginAdapter(this, mUsers, R.layout.item_user_login);
		mSpinner.setAdapter(mSpinnerAdapter);
		mSpinner.setPopupBackgroundResource(R.color.bg_transparent);
		mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				mSelectUser=mUsers.get(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	@TargetApi(Build.VERSION_CODES.KITKAT)
	private void initView() {
		// TODO Auto-generated method stub
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			// 透明状态栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// 透明通知栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
		mSpinner=(Spinner) findViewById(R.id.spinnerId);
		mGo=(ImageView) findViewById(R.id.iv_go);
		mGo.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction()==MotionEvent.ACTION_DOWN){
					mGo.setPadding(mGo.getPaddingLeft()+10, mGo.getPaddingTop()+10, mGo.getPaddingRight()+10, mGo.getPaddingBottom()+10);
				}else if(event.getAction()==MotionEvent.ACTION_UP){
					mGo.setPadding(mGo.getPaddingLeft()-10, mGo.getPaddingTop()-10, mGo.getPaddingRight()-10, mGo.getPaddingBottom()-10);
				}
				return mGo.onTouchEvent(event);
			}
		});
		mGo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("user", mSelectUser);
				intent.setClass(LoginActivity.this, RecActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
	public int getStatusBarHeight() {
		int result = 0;
		int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}
	private void initFile1() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		File file1 = new File(Environment.getExternalStorageDirectory() + "/data/peopleData.txt");
		File file2 = new File(Environment.getExternalStorageDirectory() + "/data/peopleValidData.txt");
		String nowPeople = "0";
		int num = 0;
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			file2.createNewFile();
			FileReader fr = new FileReader(file1);
			FileWriter fw = new FileWriter(file2);
			BufferedWriter bw = new BufferedWriter(fw);
			BufferedReader br = new BufferedReader(fr);
			String string;
			while ((string = br.readLine()) != null) {
				// 去0 去粘连
				String[] arr = string.split("\\s+");
				if (arr.length == 5) {
					if (!arr[2].matches("0.0") && !arr[4].matches("['0']+")) {

						StringBuilder sb = new StringBuilder();
						sb.append(arr[2]);
						sb.append(arr[3]);
						// 去除经纬度相同地点重复
						if (map.containsKey(sb.toString())) {
							string = string.replaceAll(arr[4], map.get(sb.toString()));
						} else {
							map.put(sb.toString(), arr[4]);
						}
						// 去除用户无签到地点
						if ((Integer.valueOf(arr[0]) - Integer.valueOf(nowPeople)) == 1) {
							nowPeople = arr[0];
						} else if ((Integer.valueOf(arr[0]) - Integer.valueOf(nowPeople)) == 2) {
							num++;
							nowPeople = arr[0];
						}
						string = string.replaceFirst(arr[0], Integer.valueOf(arr[0]) - num + "");
						// 前100用户
						if (Integer.valueOf(arr[0]).intValue() > 99 + num) {
							break;
						}
						bw.write(string);
						bw.newLine();

					}
				}
			}
			br.close();
			bw.flush();
			bw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	private void initFile2() {
		// TODO Auto-generated method stub
		File file1 = new File(Environment.getExternalStorageDirectory() + "/data/friendData.txt");
		File file2 = new File(Environment.getExternalStorageDirectory() + "/data/friendValidData.txt");
		int num=0;
		try {
			file2.createNewFile();
			FileReader fr = new FileReader(file1);
			FileWriter fw = new FileWriter(file2);
			BufferedWriter bw = new BufferedWriter(fw);
			BufferedReader br = new BufferedReader(fr);
			String string;
			while ((string = br.readLine()) != null) {
				String[] arr = string.split("\\s+");
				if (arr.length == 2) {
					if (Integer.valueOf(arr[0]) > 99 + num) {
						break;
					}
					if (Integer.valueOf(arr[0])<=99+num && Integer.valueOf(arr[1])<=99+1 && !arr[1].matches("73")) {
						if(arr[0].equals("73")){
							num++;
							continue;
						}
						string = string.replaceFirst(arr[0], Integer.valueOf(arr[0]) - num + "");
						if(Integer.valueOf(arr[1])>73){
							string = string.replaceFirst(arr[1], Integer.valueOf(arr[1]) - 1 + "");
						}
						bw.write(string);
						bw.newLine();
					}
				}
			}
			br.close();
			bw.flush();
			bw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

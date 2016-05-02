package wang.beats.activity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.pdf.PdfDocument.Page;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Environment;
import android.text.BoringLayout.Metrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import wang.beats.R;
import wang.beats.adapter.LoginAdapter;
import wang.beats.dao.User;
import wang.beats.db.MyDatabaseHelper;
import wang.beats.views.TitleBuilder;

public class LoginActivity extends Activity {
	TextView tv;
	ListView lv;
	SQLiteDatabase db;
	LoginAdapter adapter;
	ArrayList<User> users;
	User mSelectUser;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userlogin);
		// initFile();
		initView();
		initData();
	}
	private void initFile() {
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

	private void initView() {
		long oldTime = System.currentTimeMillis();
		db = MyDatabaseHelper.getDatabase(this, "BeatsData", 90);
		long newTime = System.currentTimeMillis();
		new TitleBuilder(this).setTextLeft(newTime - oldTime + "").setTextCenter("请选择账号").setTextRight("登入")
				.setRightListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (mSelectUser !=null) {
							Intent intent = new Intent();
							intent.putExtra("user", mSelectUser);
							intent.setClass(LoginActivity.this, RecActivity.class);
							startActivity(intent);
							finish();
						} else {
							Toast.makeText(LoginActivity.this, "请选择账号", Toast.LENGTH_SHORT).show();
						}
					}
				});
		lv = (ListView) findViewById(R.id.lv_login);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				mSelectUser=((LoginAdapter)parent.getAdapter()).changeSelect(position);
			}
		});
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			View mTopView = findViewById(R.id.viewId);
			// 透明状态栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// 透明通知栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			RelativeLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			params.height = getStatusBarHeight();
			mTopView.setLayoutParams(params);
		}
	}

	@SuppressWarnings("deprecation")
	private void initData() {
		// TODO Auto-generated method stub
		// long oldTime=System.currentTimeMillis();
		users = new ArrayList<User>();
		ArrayList<Integer> arr = new ArrayList<Integer>();
		arr.add(R.drawable.pic1);
		arr.add(R.drawable.pic2);
		arr.add(R.drawable.pic3);
		arr.add(R.drawable.pic4);
		arr.add(R.drawable.pic5);
		arr.add(R.drawable.pic6);
		arr.add(R.drawable.pic7);
		arr.add(R.drawable.pic8);
		arr.add(R.drawable.pic9);
		arr.add(R.drawable.pic10);
		arr.add(R.drawable.pic10);
		arr.add(R.drawable.pic10);
		arr.add(R.drawable.pic10);
		arr.add(R.drawable.pic10);
		arr.add(R.drawable.pic10);
		arr.add(R.drawable.pic10);
		arr.add(R.drawable.pic10);
		arr.add(R.drawable.pic10);
		for (int i = 0; i < 18; i++) {
			Cursor cursor = db.query("PeopleData", null, "people=?", new String[] { i + "" }, null, null, null);
			cursor.moveToFirst();
			int userCount_1 = cursor.getInt(cursor.getColumnIndex("count"));
			User user = new User(arr.get(i), "用户" + i, userCount_1);
			users.add(user);
		}
		adapter = new LoginAdapter(this, users, R.layout.item_user_login);
		lv.setAdapter(adapter);
		// long newTime=System.currentTimeMillis();
		// Toast.makeText(this, newTime-oldTime+"", Toast.LENGTH_SHORT).show();
	}

	long oldTime;

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		long currentTime = System.currentTimeMillis();
		if (currentTime - oldTime > 2000) {
			oldTime = System.currentTimeMillis();
			Toast.makeText(this, "再按一次退出地理好友推荐系统", Toast.LENGTH_SHORT).show();
			return;
		}
		super.onBackPressed();
	}

	public int getStatusBarHeight() {
		int result = 0;
		int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

}

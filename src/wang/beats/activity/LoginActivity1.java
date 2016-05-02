package wang.beats.activity;

import java.util.ArrayList;
import java.util.Arrays;
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
import wang.beats.adapter.LoginAdapter;
import wang.beats.adapter.LoginAdapter1;
import wang.beats.dao.User;
import wang.beats.db.MyDatabaseHelper;

public class LoginActivity1 extends Activity{
	private Spinner mSpinner;
	private ArrayAdapter mTitleAdpater;
	private LoginAdapter1 mSpinnerAdapter;
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
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void initData() {
		// TODO Auto-generated method stub
		db = MyDatabaseHelper.getDatabase(this, "BeatsData", 90);
		mUsers = new ArrayList<User>();
		ArrayList<Integer> arr = new ArrayList<Integer>();
		arr.add(R.drawable.pic5);
		arr.add(R.drawable.pic1);
		arr.add(R.drawable.pic2);
		arr.add(R.drawable.pic3);
		arr.add(R.drawable.pic4);
//		arr.add(R.drawable.pic6);
//		arr.add(R.drawable.pic7);
//		arr.add(R.drawable.pic8);
//		arr.add(R.drawable.pic9);
//		arr.add(R.drawable.pic10);
		for (int i = 0; i < 5; i++) {
			Cursor cursor = db.query("PeopleData", null, "people=?", new String[] { i + "" }, null, null, null);
			cursor.moveToFirst();
			int userCount_1 = cursor.getInt(cursor.getColumnIndex("count"));
			User user = new User(arr.get(i), "用户" + i, userCount_1);
			mUsers.add(user);
		}
		mSpinnerAdapter = new LoginAdapter1(this, mUsers, R.layout.item_user_login);
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
				intent.setClass(LoginActivity1.this, RecActivity.class);
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
}

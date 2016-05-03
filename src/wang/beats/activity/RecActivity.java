package wang.beats.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;
import charming.views.VPIndicator;
import wang.beats.R;
import wang.beats.dao.Friend;
import wang.beats.dao.User;
import wang.beats.db.MyDatabaseHelper;
import wang.beats.fragment.SortFragment;
import wang.beats.fragment.SortFragment1;
import wang.beats.views.TitleBuilder;

public class RecActivity extends FragmentActivity {
	private VPIndicator vi;
	private ViewPager vp;
	private User mUser;
	private long oldTime;
	private List<String> titles = Arrays.asList("Jaccard相关系数", "Cosine相关系数");
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	private FragmentPagerAdapter adapter;
	private ArrayList<Friend> mJaccardList;
	private ArrayList<Friend> mCosineList;
	private SQLiteDatabase db;
	private HashMap<Integer,Integer> mCountMap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recactivity);
		initView();
		initData();
		initList();
	}

	private void initList() {
		// TODO Auto-generated method stub
		db=MyDatabaseHelper.getDatabase(this, "BeatsData", 90);
		mJaccardList=new ArrayList<Friend>();
		mCosineList=new ArrayList<Friend>();
		mCountMap=new HashMap<Integer, Integer>();
		mUser = (User) getIntent().getSerializableExtra("user");
		int peopleName=mUser.getName();
//		地点集
		Map<Integer, int[]> positionMap=new HashMap<Integer, int[]>();
//		用户平方和
		int userModulo=0;
//		好友平方和
		int friendModulo=0;
		//适量乘积 分子
		int numerator=0;
//		获得当前用户地点、签到数Map
		Cursor cursor=db.query("PeopleCountData", null, "people=?", new String[]{peopleName+""}, null, null, null);
		if(cursor.moveToFirst()){
			do{
				int position=cursor.getInt(cursor.getColumnIndex("position"));
				int count=cursor.getInt(cursor.getColumnIndex("count"));
				mCountMap.put(position, count);
				userModulo+=count*count;
			}while(cursor.moveToNext());
		}
		cursor.close();
		int sameNum=0;
		int sumNum=0;
		int sumInit=0;
//		获得当前用户签到总次数
		Cursor cursor2=db.query("PeopleData", null, "people=?", new String[]{peopleName+""}, null, null, null);
		if(cursor2.moveToFirst()){
			sumInit=cursor2.getInt(cursor2.getColumnIndex("count"));
		}
		cursor2.close();
		for (int i = 0; i < 100; i++) {
			if(i==peopleName)
				continue;
			sumNum=sumInit;
			sameNum=0;
			friendModulo=0;
			numerator=0;
			positionMap=new HashMap<Integer, int[]>();
			
			Iterator<Entry<Integer, Integer>> it=mCountMap.entrySet().iterator();
			while(it.hasNext()){
				Entry<Integer, Integer> entry=it.next();
				int position=entry.getKey();
				int count=entry.getValue();
				positionMap.put(position, new int[]{count,0});
			}
			
			Cursor cursor1=db.query("PeopleCountData", null, "people=?", new String[]{i+""}, null, null, null);
			if(cursor1.moveToFirst()){
				do{
					int position=cursor1.getInt(cursor1.getColumnIndex("position"));
					int count=cursor1.getInt(cursor1.getColumnIndex("count"));
					sumNum+=count;
					friendModulo+=count*count;
					if(mCountMap.containsKey(position)){
						sameNum+=Math.min(count, mCountMap.get(position).intValue());
					}
					
					if(positionMap.containsKey(position)){
						positionMap.get(position)[1]=count;
					}else{
						positionMap.put(position, new int[]{0,count});
					}
				}while(cursor1.moveToNext());
			}
			Iterator<Entry<Integer, int[]>> it1=positionMap.entrySet().iterator();
			while(it1.hasNext()){
				Entry<Integer, int[]> entry=it1.next();
				int position=entry.getKey();
				int count0=entry.getValue()[0];
				int count1=entry.getValue()[1];
				numerator+=count0*count1;
			}
			DecimalFormat df=new DecimalFormat("#0.0000000");
			Float jaccard=((float)sameNum)/(sumNum-sameNum);
			Float cosine=(float) ((numerator)/(Math.sqrt(userModulo)*Math.sqrt(friendModulo)));
			Friend friend=new Friend(R.drawable.j, i, df.format(jaccard));
			mJaccardList.add(friend);
			Friend friend1=new Friend(R.drawable.c, i, df.format(cosine));
			mCosineList.add(friend1);
		}
		Collections.sort(mJaccardList,new Comparator<Friend>(){
			
			@Override
			public int compare(Friend lhs, Friend rhs) {
				// TODO Auto-generated method stub
				return rhs.getSimilar().compareTo(lhs.getSimilar());
			}
		});
		Collections.sort(mCosineList,new Comparator<Friend>(){
			
			@Override
			public int compare(Friend lhs, Friend rhs) {
				// TODO Auto-generated method stub
				return rhs.getSimilar().compareTo(lhs.getSimilar());
			}
		});
	}

	private void initView() {
		// TODO Auto-generated method stub
		vi = (VPIndicator) findViewById(R.id.vp_indicator);
		vp = (ViewPager) findViewById(R.id.vp_id);

		mUser = (User) getIntent().getSerializableExtra("user");
		if (mUser != null) {
			new TitleBuilder(this).setTextCenter("用户"+mUser.getName()).setTextRight("切换账号").setImgLeft(mUser.getImg())
			.setRightListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.setClass(RecActivity.this, LoginActivity.class);
					startActivity(intent);
					finish();
				}
			});
		}
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			View mTopView = findViewById(R.id.viewId);
			// 透明状态栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// 透明通知栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			params.height = getStatusBarHeight();
			mTopView.setLayoutParams(params);
		}
	}

	private void initData() {
		fragments.add(new SortFragment());
		fragments.add(new SortFragment1());
		adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return fragments.size();
			}

			@Override
			public Fragment getItem(int arg0) {
				// TODO Auto-generated method stub
				return fragments.get(arg0);
			}
		};
		vp.setAdapter(adapter);
		vi.setText(titles).setVisible_item(2).setViewPager(vp, 0).setTextSize(16).setTextLightColor(0xffD64541)
				.setIndicatorColor(0XFFE74C3C).setIndicatorHeight(2).setMovePattern(VPIndicator.MOVE_SMOOTH)
				.setMoveDuration(300);
	}

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
	public User getUser(){
		return mUser;
	}
	public ArrayList<Friend> getJaccardList(){
		return mJaccardList;
	}
	public ArrayList<Friend> getCosineList(){
		return mCosineList;
	}
}

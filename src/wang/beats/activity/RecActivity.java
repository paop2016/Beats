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
import java.util.Set;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;
import charming.utils.ConfigUtils;
import charming.utils.DensityUtils;
import charming.utils.ScreenUtils;
import charming.views.VPIndicator;
import charming.views.VPIndicator.VPListener;
import wang.beats.R;
import wang.beats.adapter.ListDrawerAdapter;
import wang.beats.dao.Friend;
import wang.beats.dao.Position;
import wang.beats.dao.User;
import wang.beats.db.MyDatabaseHelper;
import wang.beats.fragment.LineFragment;
import wang.beats.fragment.MixFragment;
import wang.beats.utils.ImageUtils;
import wang.beats.fragment.JaccardFragment;
import wang.beats.fragment.CosineFragment;
import wang.beats.views.TitleBuilder;

public class RecActivity extends FragmentActivity {
	private VPIndicator vi;
	private ViewPager vp;
	private User mUser;
	private long oldTime;
	private TextView tv_name;
	private TextView tv_count;
	private ImageView iv_icon;
	private ImageView iv_delate;
	private ImageView iv_submit;
	private TextView tv_item_position;
	private TextView tv_item_conut;
	private ListView lv_drawerlayout;
	private RelativeLayout ll_container;
	ProgressDialog progressDialog;
	private List<String> titles = Arrays.asList("Jaccard", "Cosine","Mix","评价");
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	private FragmentPagerAdapter adapter;
	private ArrayList<Friend> mJaccardList;
	private ArrayList<Friend> mCosineList;
	private ArrayList<Friend> mMixList;
	private SQLiteDatabase db;
	private HashMap<Integer,Integer> mCountMap;
	private DrawerLayout drawer;
	private boolean flag=false;
	private HashMap<Integer,Integer> newCountMap;
	private boolean needRefrash = false;
	private List<Position> positions;
	private ListDrawerAdapter drawerAdpter;
	private int currentItem=0;
	ProgressBar pg_drawerLayout;
	private Handler handler = new Handler(Looper.getMainLooper());
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recactivity);
		initView();
		initList();
		initData();
	}

	private void initList() {
		// TODO Auto-generated method stub
		db=MyDatabaseHelper.getDatabase(this);
		mJaccardList=new ArrayList<Friend>();
		mCosineList=new ArrayList<Friend>();
		mMixList=new ArrayList<Friend>();
		if(needRefrash){
			mCountMap=conbineMap(mCountMap, newCountMap);
			newCountMap.clear();
		}else{
			mCountMap=new HashMap<Integer, Integer>();
			newCountMap=new HashMap<Integer, Integer>();
		}
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
		if(!needRefrash){
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
		}else{
			userModulo=getModulo(mCountMap);
		}
		int sameNum=0;
		int sumNum=0;
		int sumInit=0;
//		获得当前用户签到总次数
//		Cursor cursor2=db.query("PeopleData", null, "people=?", new String[]{peopleName+""}, null, null, null);
//		if(cursor2.moveToFirst()){
//			sumInit=cursor2.getInt(cursor2.getColumnIndex("count"));
//		}
//		cursor2.close();
		sumInit=getSum(mCountMap);
		
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
				int count0=entry.getValue()[0];
				int count1=entry.getValue()[1];
				numerator+=count0*count1;
			}
			DecimalFormat df=new DecimalFormat("#0.0000000");
			Float jaccard=((float)sameNum)/(sumNum-sameNum);
			Float cosine=(float) ((numerator)/(Math.sqrt(userModulo)*Math.sqrt(friendModulo)));
//			算法部分
//			Mix相关系数计算
			Float mix =(float) (jaccard*sumInit*sumInit*sumInit*0.000001*0.0015625*3+cosine);
			Friend friend=new Friend(ImageUtils.getScaledImage(i), i, df.format(jaccard));
			mJaccardList.add(friend);
			Friend friend1=new Friend(ImageUtils.getScaledImage(i), i, df.format(cosine));
			mCosineList.add(friend1);
			Friend friend2=new Friend(ImageUtils.getScaledImage(i), i, df.format(mix));
			mMixList.add(friend2);
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
		Collections.sort(mMixList,new Comparator<Friend>(){
			
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
		drawer=(DrawerLayout) findViewById(R.id.drawerId);
		iv_icon = (ImageView) findViewById(R.id.iv_iconId);
		iv_delate = (ImageView) findViewById(R.id.iv_delateId);
		iv_submit = (ImageView) findViewById(R.id.iv_submitId);
		tv_name=(TextView) findViewById(R.id.tv_iconId);
		tv_count=(TextView) findViewById(R.id.tv_count);
		tv_item_conut=(TextView) findViewById(R.id.tv_item_count_drawerLayout);
		tv_item_position=(TextView) findViewById(R.id.tv_item_position_drawerLayout);
		lv_drawerlayout=(ListView) findViewById(R.id.lv_drawerId);
		ll_container=(RelativeLayout) findViewById(R.id.ll_containerId);
		pg_drawerLayout=(ProgressBar) findViewById(R.id.pg_drawerlayout);
		
		ConfigUtils.setDrawerLayoutSize(drawer, 100);
		LayoutParams params=(LayoutParams) iv_icon.getLayoutParams();
		params.topMargin=ScreenUtils.getStatusBarHeight(this)+DensityUtils.dp2px(this, 8);
		iv_icon.setLayoutParams(params);
		drawer.setScrimColor(0x15000000);
		
		mUser = (User) getIntent().getSerializableExtra("user");
		tv_name.setText("用户"+mUser.getName());
		tv_count.setText("签到数："+mUser.getCount());
		iv_icon.setImageResource(ImageUtils.getImage(mUser.getName()));
		
		iv_delate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				emptyNewCount();

			}
		});
		iv_submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				refresh();
			}
		});
		
		if (mUser != null) {
			new TitleBuilder(this).setTextCenter("好友推荐").setTextRight("切换账号").setImgLeft(R.drawable.menu).setLeftListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					drawer.openDrawer(Gravity.LEFT);
				}
			})
			.setRightListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					new AlertDialog.Builder(RecActivity.this).setMessage("确定要切换账户吗").setPositiveButton("确定", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							ProgressDialog pd=ProgressDialog.show(RecActivity.this, "", "正在切换...", false, false);
							Intent intent = new Intent();
							intent.setClass(RecActivity.this, LoginActivity.class);
							startActivity(intent);
							finish();
						}
					}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					}).setCancelable(true).create().show();
				}
			});
		}
		ScreenUtils.tranStutasBar(this, R.id.viewId);
		drawer.setDrawerListener(new DrawerListener() {
			
			@Override
			public void onDrawerStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onDrawerSlide(View arg0, float arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onDrawerOpened(View arg0) {
				// TODO Auto-generated method stub
				flag=true;
			}
			
			@Override
			public void onDrawerClosed(View arg0) {
				// TODO Auto-generated method stub
				flag=false;
			}
		});
	}

	private void initData() {
		fragments.add(new JaccardFragment());
		fragments.add(new CosineFragment());
		fragments.add(new MixFragment());
		fragments.add(new LineFragment());
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
		vi.setText(titles).setVisible_item(4).setViewPager(vp, 0).setTextSize(16).setTextLightColor(0xffD64541)
				.setIndicatorColor(0XFFE74C3C).setIndicatorHeight(2).setMovePattern(VPIndicator.MOVE_SMOOTH)
				.setMoveDuration(300);
		vi.setVPListener(new VPListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				currentItem=arg0;
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		positions = new ArrayList<Position>();
		positions.addAll(getPositionList(mCountMap));
		drawerAdpter = new ListDrawerAdapter(this, positions, R.layout.item_drawerlayout,ll_container);
		lv_drawerlayout.setAdapter(drawerAdpter);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(flag){
			drawer.closeDrawer(Gravity.LEFT);
		}else {
			long currentTime = System.currentTimeMillis();
			if (currentTime - oldTime > 2000) {
				oldTime = System.currentTimeMillis();
				Toast.makeText(this, "再按一次退出地理好友推荐系统", Toast.LENGTH_SHORT).show();
				return;
			}
			super.onBackPressed();
		}
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
	public ArrayList<Friend> getMisList(){
		return mMixList;
	}
	public HashMap<Integer, Integer> conbineMap(HashMap<Integer, Integer> map1, HashMap<Integer, Integer> map2){
		HashMap<Integer, Integer> map3= new HashMap<Integer, Integer>();
		map3.putAll(map1);
		Set<Entry<Integer, Integer>> entrySet = map2.entrySet();
		Iterator<Entry<Integer, Integer>> it= entrySet.iterator();
		while(it.hasNext()){
			Entry<Integer, Integer> entry= it.next();
			int key=entry.getKey();
			int value = entry.getValue();
			if(map3.containsKey(key)){
				map3.put(key, value+map3.get(key));
			}else{
				map3.put(key, value);
			}
		}
		return map3;
	}
	public List<Position> getPositionList(HashMap<Integer, Integer> map){
		List<Position> list=new ArrayList<Position>();
		Set<Entry<Integer, Integer>> entrySet = map.entrySet();
		Iterator<Entry<Integer, Integer>> it= entrySet.iterator();
		while(it.hasNext()){
			Entry<Integer, Integer> entry= it.next();
			int key=entry.getKey();
			int value = entry.getValue();
			Position position = new Position(key, value);
			list.add(position);
		}
		Collections.sort(list,new Comparator<Position>(){
			
			@Override
			public int compare(Position lhs, Position rhs) {
				// TODO Auto-generated method stub
				return rhs.getCount()-lhs.getCount();
			}
		});
		return list;
	}
	public int  getModulo(HashMap<Integer, Integer> map){
		int modulo=0;
		List<Position> list=new ArrayList<Position>();
		Set<Entry<Integer, Integer>> entrySet = map.entrySet();
		Iterator<Entry<Integer, Integer>> it= entrySet.iterator();
		while(it.hasNext()){
			Entry<Integer, Integer> entry= it.next();
			int value = entry.getValue();
			modulo+=value*value;
		}
		return modulo;
	}
	public int getSum(HashMap<Integer, Integer> map){
		int sum=0;
		Set<Entry<Integer, Integer>> entrySet = map.entrySet();
		Iterator<Entry<Integer, Integer>> it= entrySet.iterator();
		while(it.hasNext()){
			Entry<Integer, Integer> entry= it.next();
			int value = entry.getValue();
			sum+=value;
		}
		return sum;
	}
	public void emptyNewCount(){
		positions.clear();
		positions.addAll(getPositionList(mCountMap));
		drawerAdpter.notifyDataSetChanged();
	}
	public void refresh(){
		
		Iterator<Position> it = positions.iterator();
		while(it.hasNext()){
			Position position= it.next();
			newCountMap.put(position.getName(), position.getNewCount());
		}
		needRefrash=true;
		initList();
		emptyNewCount();
		tv_count.setText("签到数："+getSum(mCountMap));
		fragments.clear();
		fragments.add(new JaccardFragment());
		fragments.add(new CosineFragment());
		fragments.add(new MixFragment());
		fragments.add(new LineFragment());
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
		vp.setCurrentItem(currentItem);
	} 

}

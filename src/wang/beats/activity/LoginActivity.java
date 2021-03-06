package wang.beats.activity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import charming.adapter.CommenViewHolder;
import charming.utils.DensityUtils;
import charming.utils.ScreenUtils;
import wang.beats.R;
import wang.beats.adapter.ListAdapter;
import wang.beats.adapter.LoginAdapter;
import wang.beats.dao.Friend;
import wang.beats.dao.User;
import wang.beats.db.MyDatabaseHelper;
import wang.beats.utils.ImageUtils;

public class LoginActivity extends Activity{
	private Spinner mSpinner;
	private LoginAdapter mSpinnerAdapter;
	private List<User> mUsers;
	private ImageView mGo;
	private ImageView mEvaluation;
	private ImageView mLogoBody;
	private ImageView mLogoBase;
	private ImageView mShake;
	private User mSelectUser;
	SQLiteDatabase db;
	AnimationSet bodyDisappearSet;
	AnimationSet baseDisappearSet;
	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what==1){
				mLogoBody.startAnimation(bodyDisappearSet);
				mLogoBase.startAnimation(baseDisappearSet);
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initView();
		initData();
//		initFile2();
//		new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				initDatabase(db);
//			}
//		}).start();
	}


	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void initData() {
		// TODO Auto-generated method stub
		db = MyDatabaseHelper.getDatabase(this);
		mUsers = new ArrayList<User>();
		for (int i = 0; i < 100; i++) {
			Cursor cursor = db.query("PeopleData", null, "people=?", new String[] { i + "" }, null, null, null);
			cursor.moveToFirst();
			int userCount = cursor.getInt(cursor.getColumnIndex("count"));
			User user = new User(ImageUtils.getScaledImage(i), i, userCount);
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
		ScreenUtils.ignoreStutasBar(this);
		mSpinner=(Spinner) findViewById(R.id.spinnerId);
		mGo=(ImageView) findViewById(R.id.iv_go);
		mEvaluation=(ImageView) findViewById(R.id.iv_line);
		mLogoBody=(ImageView) findViewById(R.id.iv_logoBody);
		mLogoBase=(ImageView) findViewById(R.id.iv_logoBase);
		mShake=(ImageView) findViewById(R.id.iv_shake);
		
//		RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams((int)(36*scale), (int)(36*scale));
		RelativeLayout.LayoutParams params=(LayoutParams) mEvaluation.getLayoutParams();
		params.height=DensityUtils.dp2px(this, 36);
		params.width=DensityUtils.dp2px(this, 36);
		params.topMargin=ScreenUtils.getStatusBarHeight(this)+DensityUtils.dp2px(this, 16);;
		params.rightMargin=DensityUtils.dp2px(this, 16);
		mEvaluation.setLayoutParams(params);
		
		final TranslateAnimation jumpAnimation = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, 0f,
				TranslateAnimation.RELATIVE_TO_SELF, 0f, TranslateAnimation.RELATIVE_TO_SELF, 0f,
				TranslateAnimation.RELATIVE_TO_SELF, -0.3f);
		jumpAnimation.setDuration(600);
		jumpAnimation.setRepeatMode(Animation.REVERSE);
		jumpAnimation.setRepeatCount(5);
		jumpAnimation.setFillAfter(true);
		//Logo本体
		final TranslateAnimation bodyMoveOutAnimation = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, 0f,
				TranslateAnimation.RELATIVE_TO_SELF, -5f, TranslateAnimation.RELATIVE_TO_SELF, 0f, TranslateAnimation.RELATIVE_TO_SELF, 2f);
		final ScaleAnimation bodyScaleAnimation = new ScaleAnimation(1, 0.6f, 1, 0.6f);
		//Logo底座
		final TranslateAnimation baseMoveOutAnimation = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, 0f,
				TranslateAnimation.RELATIVE_TO_SELF, 0f, TranslateAnimation.RELATIVE_TO_SELF, 0f, TranslateAnimation.RELATIVE_TO_SELF, 5f);
		AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0.5f);
		//Logo手
		final TranslateAnimation shakeMoveInAnimation = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, 1.8f,
				TranslateAnimation.RELATIVE_TO_SELF, 0f, TranslateAnimation.RELATIVE_TO_SELF, -1.2f, TranslateAnimation.RELATIVE_TO_SELF, 0f);
//		final TranslateAnimation shakeMoveInAnimation = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, 2.4f,
//				TranslateAnimation.RELATIVE_TO_SELF, 0f, TranslateAnimation.RELATIVE_TO_SELF, -1.6f, TranslateAnimation.RELATIVE_TO_SELF, 0f);
		AlphaAnimation shakeAlphaAnimation = new AlphaAnimation(0.5f, 1f);
		final AnimationSet shakeAppearSet = new AnimationSet(true);
		shakeAppearSet.setFillAfter(true);
		shakeAppearSet.addAnimation(shakeMoveInAnimation);
		shakeAppearSet.addAnimation(shakeAlphaAnimation);
		shakeAppearSet.setDuration(1200);
		
		
		bodyDisappearSet = new AnimationSet(true);
		bodyDisappearSet.setFillAfter(true);
		bodyDisappearSet.addAnimation(bodyMoveOutAnimation);
		bodyDisappearSet.addAnimation(alphaAnimation);
		bodyDisappearSet.addAnimation(bodyScaleAnimation);
		bodyDisappearSet.setDuration(1800);
		
		baseDisappearSet = new AnimationSet(true);
		baseDisappearSet.setFillAfter(true);
		baseDisappearSet.addAnimation(baseMoveOutAnimation);
		baseDisappearSet.addAnimation(alphaAnimation);
		baseDisappearSet.setDuration(1800);
		
		
		mLogoBody.startAnimation(jumpAnimation);
		jumpAnimation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				mShake.setVisibility(View.VISIBLE);
				mShake.startAnimation(shakeAppearSet);
				handler.sendEmptyMessageDelayed(1, 600);
			}
		});
		mLogoBody.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mLogoBody.startAnimation(jumpAnimation);
				mLogoBase.clearAnimation();
				mShake.clearAnimation();
				mShake.setVisibility(View.GONE);
			}
		});
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
				ProgressDialog pd=ProgressDialog.show(LoginActivity.this, "", "正在登陆中", false, false);
				Intent intent = new Intent();
				intent.putExtra("user", mSelectUser);
				intent.setClass(LoginActivity.this, RecActivity.class);
				startActivity(intent);
				finish();
			}
		});
		mEvaluation.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction()==MotionEvent.ACTION_DOWN){
					mEvaluation.setPadding(mEvaluation.getPaddingLeft()+8, mEvaluation.getPaddingTop()+8, mEvaluation.getPaddingRight()+8, mEvaluation.getPaddingBottom()+8);
				}else if(event.getAction()==MotionEvent.ACTION_UP){
					mEvaluation.setPadding(mEvaluation.getPaddingLeft()-8, mEvaluation.getPaddingTop()-8, mEvaluation.getPaddingRight()-8, mEvaluation.getPaddingBottom()-8);
				}
				return mEvaluation.onTouchEvent(event);
			}
		});
		mEvaluation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, EvaluationActivity.class);
				startActivity(intent);
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
				String[] arr = string.split("\\s+");
				if (arr.length == 5) {
					// 去除无效地点
					if (!arr[2].matches("0.0") && !arr[4].matches("['0']+")) {

						StringBuilder sb = new StringBuilder();
						sb.append(arr[2]);
						sb.append(arr[3]);
						// 将地点代码编号
						if (map.containsKey(sb.toString())) {
							string = string.replaceAll(arr[4], map.get(sb.toString()));
						} else {
							map.put(sb.toString(), arr[4]);
						}
						// 去除无数据用户
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

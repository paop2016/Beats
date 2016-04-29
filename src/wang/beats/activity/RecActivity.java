package wang.beats.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import charming.views.VPIndicator;
import wang.beats.R;
import wang.beats.dao.User;
import wang.beats.fragment.MyFragment;
import wang.beats.views.TitleBuilder;

public class RecActivity extends FragmentActivity {
	VPIndicator vi;
	ViewPager vp;
	User user;
	List<String> titles = Arrays.asList("好友推荐", "添加新地点");
	ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	FragmentPagerAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		setContentView(R.layout.activity_recactivity);
		initView();
		initData();
	}

	private void initView() {
		// TODO Auto-generated method stub
		vi = (VPIndicator) findViewById(R.id.vp_indicator);
		vp = (ViewPager) findViewById(R.id.vp_id);

		user = (User) getIntent().getSerializableExtra("user");
		if (user != null) {
			new TitleBuilder(this).setTextCenter(user.getName()).setTextRight("切换账号").setImgLeft(user.getImg())
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
	}

	private void initData() {
		for (int i = 0; i < titles.size(); i++) {
			MyFragment fragment = MyFragment.getInstance(titles.get(i));
			fragments.add(fragment);
		}
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
		vi.setText(titles).setVisible_item(2).setViewPager(vp, 0).setTextSize(16).setTextLightColor(0xffD64541).setIndicatorColor(0XFFE74C3C)
		.setIndicatorHeight(2).setMovePattern(VPIndicator.MOVE_DELAY).setMoveDuration(1000);
		Log.v("jay", "Activity");
	}
	long oldTime;
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		long currentTime=System.currentTimeMillis();
		if(currentTime-oldTime>2000){
			oldTime=System.currentTimeMillis();
			Toast.makeText(this, "再按一次退出地理好友推荐系统", Toast.LENGTH_SHORT).show();
			return;
		}
		super.onBackPressed();
	}
	
}

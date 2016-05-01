package wang.beats.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;
import charming.views.VPIndicator;
import wang.beats.R;
import wang.beats.dao.User;
import wang.beats.fragment.SortFragment;
import wang.beats.views.TitleBuilder;

public class RecActivity extends FragmentActivity {
	private VPIndicator vi;
	private ViewPager vp;
	private User user;
	private long oldTime;
	private List<String> titles = Arrays.asList("好友推荐", "添加新地点");
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	private FragmentPagerAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
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
		fragments.add(new SortFragment());
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

}

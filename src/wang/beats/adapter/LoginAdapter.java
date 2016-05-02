package wang.beats.adapter;

import java.util.List;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import charming.adapter.CommenAdapter;
import charming.adapter.CommenViewHolder;
import wang.beats.R;
import wang.beats.dao.User;

public class LoginAdapter extends CommenAdapter<User> implements SpinnerAdapter{

	public LoginAdapter(Context context, List<User> datas, int layoutId) {
		super(context, datas, layoutId);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void convert(CommenViewHolder holder, User t) {
		// TODO Auto-generated method stub
		holder.setImageResource(R.id.iv_item_userimg_login, t.getImg())
		.setText(R.id.tv_item_username_login, "用户"+t.getName())
		.setText(R.id.tv_item_usercount_login, t.getCount() + "");
	}
}

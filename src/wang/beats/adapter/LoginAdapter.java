package wang.beats.adapter;

import java.util.List;

import android.content.Context;
import charming.adapter.CommenAdapter;
import charming.adapter.CommenViewHolder;
import wang.beats.R;
import wang.beats.dao.User;

public class LoginAdapter extends CommenAdapter<User>{
	
	int selectPosition=-1;
	public LoginAdapter(Context context, List<User> datas, int layoutId) {
		super(context, datas, layoutId);
		// TODO Auto-generated constructor stub
	}
	public User changeSelect(int position){
		User user=null;
		if(position!=selectPosition){
			user=getItem(position);
			user.setIsChecked(true);
			if(selectPosition>=0){
				getItem(selectPosition).setIsChecked(false);
			}
			selectPosition=position;
		}
		else {
			getItem(position).setIsChecked(false);
			selectPosition=-1;
		}
		notifyDataSetChanged();
		return user;
	}
	@Override
	protected void convert(CommenViewHolder holder, User t) {
		// TODO Auto-generated method stub
		holder.setImageResource(R.id.iv_item_userimg_login, t.getImg());
		holder.setText(R.id.tv_item_username_login, t.getName());
		holder.setText(R.id.tv_item_usercount_login, t.getCount()+"");
		if(t.getIsChecked()){
			holder.getConvertView().setBackgroundResource(R.color.bg_red_checked);
		}else{
			holder.getConvertView().setBackgroundResource(R.drawable.sel_lv_login);
		}
	}

}

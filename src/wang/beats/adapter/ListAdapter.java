package wang.beats.adapter;

import java.util.List;

import android.content.Context;
import charming.adapter.CommenAdapter;
import charming.adapter.CommenViewHolder;
import wang.beats.R;
import wang.beats.dao.Friend;
import wang.beats.dao.User;

public class ListAdapter extends CommenAdapter<Friend>{
	
	int selectPosition=-1;
	public ListAdapter(Context context, List<Friend> datas, int layoutId) {
		super(context, datas, layoutId);
		// TODO Auto-generated constructor stub
	}
	public Friend changeSelect(int position){
		Friend friend=null;
		if(position!=selectPosition){
			friend=getItem(position);
			friend.setChecked(true);
			if(selectPosition>=0){
				getItem(selectPosition).setChecked(false);
			}
			selectPosition=position;
		}
		else {
			getItem(position).setChecked(false);
			selectPosition=-1;
		}
		notifyDataSetChanged();
		return friend;
	}
	public int getSelectPosition() {
		return selectPosition;
	}
	public void setSelectPosition(int position){
		selectPosition=position;
	}
	@Override
	protected void convert(CommenViewHolder holder, Friend t) {
		// TODO Auto-generated method stub
		holder.setImageResource(R.id.iv_item_userimg_login, t.getImgId());
		holder.setText(R.id.tv_item_username_login, "用户"+t.getName());
		holder.setText(R.id.tv_item_usercount_login, t.getSimilar()+"");
		if(t.isChecked()){
			holder.getConvertView().setBackgroundResource(R.color.bg_red_checked);
		}else{
			holder.getConvertView().setBackgroundResource(R.drawable.sel_lv_login);
		}
	}

}

package wang.beats.adapter;

import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import charming.adapter.CommenAdapter;
import charming.adapter.CommenViewHolder;
import wang.beats.R;
import wang.beats.dao.Position;

public class ListDrawerAdapter extends CommenAdapter<Position> {

	final View visView;
	public ListDrawerAdapter(Context context, List<Position> datas, int layoutId, View view) {
		super(context, datas, layoutId);
		// TODO Auto-generated constructor stub
		visView=view;
	}
	
	@Override
	protected void convert(CommenViewHolder holder, final Position t) {
		// TODO Auto-generated method stub
		holder.setText(R.id.tv_item_position_drawerLayout, t.getName() + "")
				.setText(R.id.tv_item_count_drawerLayout, t.getCount() + "")
				.setText(R.id.tv_item_number_drawerLayout, t.getNewCount() + "");
		final TextView tv_number = (TextView) holder.getView(R.id.tv_item_number_drawerLayout);
		ImageView iv_add = (ImageView) holder.getView(R.id.iv_item_add_drawerLayout);
		ImageView iv_unadd = (ImageView) holder.getView(R.id.iv_item_unadd_drawerLayout);
		final View view =holder.getConvertView();
		
		if(t.getNewCount()!=0){
			view.setBackgroundColor(0xffFDE3A7);
		}else {
			view.setBackgroundResource(R.color.bg_white);
		}
		if(newConutExit(mDatas)){
			visView.setVisibility(View.VISIBLE);
		}else{
			visView.setVisibility(View.GONE);
		}
		iv_add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				t.setNewCount(t.getNewCount()+1);
				tv_number.setText(t.getNewCount()+"");
				notifyDataSetChanged();
			}
		});
		iv_unadd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(t.getNewCount()+t.getCount()!=0){
					t.setNewCount(t.getNewCount()-1);
					tv_number.setText(t.getNewCount()+"");
					notifyDataSetChanged();
				}
			}
		});
	}
	private boolean newConutExit(List<Position> datas){
		Iterator<Position> it=datas.iterator();
		while (it.hasNext()) {
			int newCount = it.next().getNewCount();
			if(newCount!=0){
				return true;
			}
		}
		return false;
	}
}

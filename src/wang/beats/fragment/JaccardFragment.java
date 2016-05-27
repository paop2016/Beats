package wang.beats.fragment;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnGenericMotionListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import wang.beats.R;
import wang.beats.activity.LoginActivity;
import wang.beats.activity.RecActivity;
import wang.beats.adapter.ListAdapter;
import wang.beats.dao.Friend;

public class JaccardFragment extends Fragment{
	private ArrayList<Friend> mFriends;
	private ListAdapter adapter;
	private int selectedItem=-1;
	private ImageView iv;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.v("jay", "JaccardonCreateView");
		View view=inflater.inflate(R.layout.fragment_sort, container, false);
		mFriends=((RecActivity)getActivity()).getJaccardList();
		ListView listView=(ListView) view.findViewById(R.id.lv_jaccard);
		iv=(ImageView) view.findViewById(R.id.iv_plus);
		listView.setSelector(getResources().getDrawable(R.color.bg_transparent));
		listView.setBackgroundColor(getResources().getColor(R.color.bg_white));
		listView.setDividerHeight((int) (getResources().getDisplayMetrics().density*1+0.5f));
		adapter=new ListAdapter(getContext(), mFriends, R.layout.item_user_login);
		adapter.setSelectPosition(selectedItem);
		if(selectedItem>=0){
			iv.setVisibility(View.VISIBLE);
		}else {
			iv.setVisibility(View.GONE);
		}
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				adapter.changeSelect(position,iv);
				selectedItem=adapter.getSelectPosition();
			}
			
		});
		iv.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					iv.setPadding(iv.getPaddingLeft() + 20, iv.getPaddingTop() + 20, iv.getPaddingRight() + 20,
							iv.getPaddingBottom() + 20);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					iv.setPadding(iv.getPaddingLeft() - 20, iv.getPaddingTop() - 20, iv.getPaddingRight() - 20,
							iv.getPaddingBottom() - 20);
				}
				return iv.onTouchEvent(event);
//				return true;
			}
		});
		iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(getActivity())
						.setMessage("确定要将用户" + mFriends.get(selectedItem).getName() + "加为好友吗")
						.setPositiveButton("确定", null).setNegativeButton("取消", null).setCancelable(true).create()
						.show();
			}
		});
		return view;
	}
}

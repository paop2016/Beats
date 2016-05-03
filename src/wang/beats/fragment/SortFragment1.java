package wang.beats.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import wang.beats.R;
import wang.beats.activity.RecActivity;
import wang.beats.adapter.ListAdapter;
import wang.beats.dao.Friend;

public class SortFragment1 extends Fragment{
	private ArrayList<Friend> mFriends;
	private ListAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mFriends=((RecActivity)getActivity()).getCosineList();
		ListView listView=new ListView(getContext());
		listView.setSelector(getResources().getDrawable(R.color.bg_transparent));
		listView.setBackgroundColor(getResources().getColor(R.color.bg_white));
		listView.setDividerHeight((int) (getResources().getDisplayMetrics().density*1+0.5f));
		adapter=new ListAdapter(getContext(), mFriends, R.layout.item_user_login);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				adapter.changeSelect(position);
			}
			
		});
		return listView;
	}
}

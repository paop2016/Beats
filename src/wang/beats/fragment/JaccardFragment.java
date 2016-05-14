package wang.beats.fragment;

import java.util.ArrayList;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnGenericMotionListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import wang.beats.R;
import wang.beats.activity.RecActivity;
import wang.beats.adapter.ListAdapter;
import wang.beats.dao.Friend;

public class JaccardFragment extends Fragment{
	private ArrayList<Friend> mFriends;
	private ListAdapter adapter;
	private int selectedItem=-1;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mFriends=((RecActivity)getActivity()).getJaccardList();
		ListView listView=new ListView(getContext());
		listView.setSelector(getResources().getDrawable(R.color.bg_transparent));
		listView.setBackgroundColor(getResources().getColor(R.color.bg_white));
		listView.setDividerHeight((int) (getResources().getDisplayMetrics().density*1+0.5f));
		adapter=new ListAdapter(getContext(), mFriends, R.layout.item_user_login);
		adapter.setSelectPosition(selectedItem);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				adapter.changeSelect(position);
				selectedItem=adapter.getSelectPosition();
			}
			
		});
		return listView;
	}
}

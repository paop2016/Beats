package wang.beats.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.res.Resources.Theme;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import wang.beats.R;
import wang.beats.activity.RecActivity;
import wang.beats.adapter.ListAdapter;
import wang.beats.dao.User;
import wang.beats.db.MyDatabaseHelper;

public class SortFragment extends Fragment{
	private ArrayList<User> mFriends;
	private ListAdapter adapter;
	private SQLiteDatabase db;
	private User mUser;
	private HashMap<Integer,Integer> mMap;
	
	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		db=MyDatabaseHelper.getDatabase(getContext(), "BeatsData", 90);
		mUser=((RecActivity)getActivity()).getUser();
		mMap=new HashMap<Integer, Integer>();
		ListView listView=new ListView(getContext());
		listView.setSelector(getResources().getDrawable(R.color.bg_transparent));
		listView.setBackgroundColor(getResources().getColor(R.color.bg_white));
		listView.setDividerHeight((int) (getResources().getDisplayMetrics().density*1+0.5f));
		mFriends=new ArrayList<User>();
		ArrayList<Integer> arr=new ArrayList<Integer>();
//		arr.add(R.drawable.pic1);
//		arr.add(R.drawable.pic2);
//		arr.add(R.drawable.pic3);
//		arr.add(R.drawable.pic4);
//		arr.add(R.drawable.pic5);
//		arr.add(R.drawable.pic6);
//		arr.add(R.drawable.pic7);
//		arr.add(R.drawable.pic8);
//		arr.add(R.drawable.pic9);
//		arr.add(R.drawable.pic10);
		initUsers(arr);
//		for (int i = 0; i < 18; i++) {
//			Cursor cursor=db.query("PeopleData", null, "people=?", new String[]{i+""}, null, null, null);
//			cursor.moveToFirst();
//			int userCount_1=cursor.getInt(cursor.getColumnIndex("count"));
//			User user=new User(arr.get(i), "用户"+i, userCount_1);
//			users.add(user);
//		}
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
	private void initUsers(ArrayList<Integer> arr) {
		// TODO Auto-generated method stub
		int peopleName=mUser.getName();
		Cursor cursor=db.query("PeopleCountData", null, "people=?", new String[]{peopleName+""}, null, null, null);
		if(cursor.moveToFirst()){
			do{
				int position=cursor.getInt(cursor.getColumnIndex("position"));
				int count=cursor.getInt(cursor.getColumnIndex("count"));
				User user=new User(R.drawable.pic3, position, count);
				mFriends.add(user);
				mMap.put(position, count);
			}while(cursor.moveToNext());
		}
//		List<Entry<Integer, Integer>> list=new ArrayList<Map.Entry<Integer,Integer>>(mMap.entrySet());
//		Iterator<Entry<Integer, Integer>> it=list.iterator();
//		while(it.hasNext()){
//			arr.add(R.drawable.pic1);
//			
//		}
	}
}

package wang.beats.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.Inflater;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import charming.views.Histogram;
import wang.beats.R;
import wang.beats.activity.RecActivity;
import wang.beats.dao.Friend;
import wang.beats.dao.User;
import wang.beats.db.MyDatabaseHelper;

public class CountFragment extends Fragment{
	private ArrayList<Friend> mJaccardList;
	private ArrayList<Friend> mCosineList;
	ArrayList<Integer> int1;
	ArrayList<Integer> int2;
	private User mUser;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Histogram histogram = new Histogram(getActivity());
		SQLiteDatabase db=MyDatabaseHelper.getDatabase(getActivity());
		mJaccardList=((RecActivity)getActivity()).getJaccardList();
		mCosineList=((RecActivity)getActivity()).getCosineList();
		mUser=((RecActivity)getActivity()).getUser();
		Cursor cursor=db.query("FriendData", null, "people=?", new String[]{mUser.getName()+""}, null, null, null);
		int mJaccardCount=0;
		int mCosineCount=0;
		if(cursor.moveToFirst()){
			do{
				int friendName=cursor.getInt(cursor.getColumnIndex("friend"));
				for (int i = 0; i < mJaccardList.size(); i++) {
					Friend friend=mJaccardList.get(i);
					Friend friend1=mCosineList.get(i);
					if(friend.getName()==friendName){
//						位置
						mJaccardCount+=i;
					}
					if(friend1.getName()==friendName){
//						位置
						mCosineCount+=i;
					}
				}
			}while(cursor.moveToNext());
		}
		cursor.close();
		List<Integer> mData=Arrays.asList(mJaccardCount,mCosineCount);
		histogram.setBackgroundColor(getResources().getColor(R.color.bg_white));
		histogram.setData(mData);
		return histogram;
	}
}

package wang.beats.fragment;

import java.net.IDN;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import charming.views.HistorgramLine;
import wang.beats.R;
import wang.beats.activity.RecActivity;
import wang.beats.dao.Friend;
import wang.beats.dao.User;
import wang.beats.db.MyDatabaseHelper;

public class LineFragment extends Fragment {
	private ArrayList<Friend> mJaccardList;
	private ArrayList<Friend> mCosineList;
	private ArrayList<Friend> mMixList;
	ArrayList<List<Integer>> mdata;
	private User mUser;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		HistorgramLine histogramLine = new HistorgramLine(getActivity());
		SQLiteDatabase db = MyDatabaseHelper.getDatabase(getActivity());
		mJaccardList = ((RecActivity) getActivity()).getJaccardList();
		mCosineList = ((RecActivity) getActivity()).getCosineList();
		mMixList = ((RecActivity) getActivity()).getMisList();
		mUser = ((RecActivity) getActivity()).getUser();
		ArrayList<List<Integer>> mdata = new ArrayList<List<Integer>>();
		;
		ArrayList<Integer> int1 = new ArrayList<Integer>();
		ArrayList<Integer> int2 = new ArrayList<Integer>();
		ArrayList<Integer> int3 = new ArrayList<Integer>();
		Cursor cursor = db.query("FriendData", null, "people=?", new String[] { mUser.getName() + "" }, null, null,
				null);
		if (cursor.moveToFirst()) {
			do {
				int friendName = cursor.getInt(cursor.getColumnIndex("friend"));
				for (int i = 0; i < mJaccardList.size(); i++) {
					Friend friend = mJaccardList.get(i);
					Friend friend1 = mCosineList.get(i);
					Friend friend2 = mMixList.get(i);
					if (friend.getName() == friendName) {
						// 位置
						int1.add(i);
					}
					if (friend1.getName() == friendName) {
						// 位置
						int2.add(i);
					}
					if (friend2.getName() == friendName) {
						// 位置
						int3.add(i);
					}
				}
			} while (cursor.moveToNext());
		}
		cursor.close();
		mdata.add(int1);
		mdata.add(int2);
		mdata.add(int3);
		histogramLine.setBackgroundColor(getResources().getColor(R.color.bg_white));
		histogramLine.setData(mdata);
		return histogramLine;
	}

}

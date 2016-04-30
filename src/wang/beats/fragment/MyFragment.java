package wang.beats.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MyFragment extends Fragment {
	private MyFragment(){};
	public static MyFragment getInstance(String text){
		MyFragment myFragment = new MyFragment();
		Bundle bundle = new Bundle();
		bundle.putString("tag", text);
		myFragment.setArguments(bundle);
		return myFragment;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		TextView textView = null ;
		Bundle bundle = getArguments();
		if(bundle!=null){
			textView = new TextView(getActivity());
			textView.setText(bundle.getString("tag"));
			textView.setGravity(Gravity.CENTER);
		}
		Log.v("jay", "onCreateView");
		return textView;
	}
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		Log.v("jay", "onStart");
		super.onStart();
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		Log.v("jay", "onResume");
		super.onResume();
		
//		Toast.makeText(getActivity(), "1", Toast.LENGTH_SHORT).show();
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		Log.v("jay", "onPause");
		super.onPause();
	}
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		Log.v("jay", "onStop");
		super.onStop();
	}
}

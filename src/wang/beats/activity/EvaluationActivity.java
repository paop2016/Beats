package wang.beats.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import charming.utils.DensityUtils;
import charming.utils.ScreenUtils;
import charming.views.LineChart;
import wang.beats.R;
import wang.beats.db.MyDatabaseHelper;
import wang.beats.views.TitleBuilder;

public class EvaluationActivity extends Activity{
	LineChart mLC;
	List<Integer> mDatas;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evaluation);
		
		ScreenUtils.tranStutasBar(this, R.id.status_view);
		mDatas=new ArrayList<Integer>();
		new TitleBuilder(this).setImgLeft(R.drawable.abc_ic_ab_back_material).setLeftListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		}).setTextCenter("算法评估");
		
		SQLiteDatabase db=MyDatabaseHelper.getDatabase(this);
		Cursor cursor=db.query("ConsequenceData", null, "people=?", new String[]{"99"}, null, null, null);
		if(cursor.moveToFirst()){
			mDatas.add(cursor.getInt(cursor.getColumnIndex("jcount")));
			mDatas.add(cursor.getInt(cursor.getColumnIndex("ccount")));
			mDatas.add(cursor.getInt(cursor.getColumnIndex("mcount")));
		}
		cursor.close();
		mLC=(LineChart) findViewById(R.id.lc_id);
		mLC.setItem(Arrays.asList("Jaccard","Cosine","Mix")).setData(mDatas).setYvalue(100).setYCount(10);
	}
}
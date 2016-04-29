package wang.beats.views;

import android.app.Activity;
import android.opengl.Visibility;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import wang.beats.R;

public class TitleBuilder {
	TextView tv_left;
	TextView tv_center;
	TextView tv_right;
	ImageView iv_left;
	public TitleBuilder(Activity activity) {
		// TODO Auto-generated constructor stub
		tv_left=(TextView) activity.findViewById(R.id.tv_title_left);
		tv_center=(TextView) activity.findViewById(R.id.tv_title_center);
		tv_right=(TextView) activity.findViewById(R.id.tv_title_right);
		iv_left=(ImageView) activity.findViewById(R.id.iv_img_left);
	}
	public TitleBuilder setTextLeft(String str){
		tv_left.setText(str);
		tv_left.setVisibility(View.VISIBLE);
		return this;
	}
	public TitleBuilder setImgLeft(int res){
		iv_left.setImageResource(res);
		iv_left.setVisibility(View.VISIBLE);
		return this;
	}
	public TitleBuilder setTextCenter(String str){
		tv_center.setText(str);
		tv_center.setVisibility(View.VISIBLE);
		return this;
	}
	public TitleBuilder setTextRight(String str){
		tv_right.setText(str);
		tv_right.setVisibility(View.VISIBLE);
		return this;
	}
	public TitleBuilder setLeftListener(OnClickListener listener){
		if(tv_left.getVisibility()==View.VISIBLE)
			tv_left.setOnClickListener(listener);
		else if(iv_left.getVisibility()==View.VISIBLE)
			iv_left.setOnClickListener(listener);
		return this;
	}
	public TitleBuilder setCenterListener(OnClickListener listener){
		tv_center.setOnClickListener(listener);
		return this;
	}
	public TitleBuilder setRightListener(OnClickListener listener){
		tv_right.setOnClickListener(listener);
		return this;
	}
}

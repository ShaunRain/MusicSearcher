package com.Rain.musicsearch;

import java.util.List;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewPagerActivity extends Activity{
	private ViewPager viewPager;//页卡内容  
    private ImageView imageView;// 动画图片  
    private TextView textView1,textView2,textView3;  
    private List<View> views;// Tab页面列表  
    private int offset = 0;// 动画图片偏移量  
    private int currIndex = 0;// 当前页卡编号  
    private int bmpW;// 动画图片宽度  
    private View view1,view2,view3;//各个页卡  
    
    
}

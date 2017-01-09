package com.atm.chatonline.chat.util;

/**
 * 这个是定义多功能的窗口，点击会出现新建群和搜索群的功能
 * */

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.atm.chatonline.chat.adapter.MoreChooseAdapter;
import com.atm.chatonline.chat.info.MoreChoose;
import com.example.studentsystem01.R;



public class MtitlePopupWindow extends PopupWindow{
	
	private String tag="MtitlePopuWindow";
	
	private Context context;
	
	private MoreChooseAdapter moreChooseAdapter;
	
	private List<MoreChoose> list = new ArrayList<MoreChoose>();
	
	private OnPopupWindowClickListener listener;
	
	private ListView listView;
	
	private int width = 0;
	
	public MtitlePopupWindow(Context context){
		super(context);
		this.context=context;
		initView();
	}
	
	public void initView(){
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View popupView = inflater.inflate(R.layout.pop_menulist, null);
		setContentView(popupView);
		
		setWidth(300);
		setHeight(LayoutParams.WRAP_CONTENT);
		
//		setAnimationStyle(R.style.AnimationFade);
		
		this.setFocusable(true);
		this.setBackgroundDrawable(new BitmapDrawable()); 
	    this.setOutsideTouchable(true);
	    
	    listView = (ListView)popupView.findViewById(R.id.more_list);
	    MoreChoose createGroup = new MoreChoose("新建群",R.drawable.pic_head);
		MoreChoose searchGroup = new MoreChoose("搜索群",R.drawable.pic_head);
		list.add(createGroup);
		list.add(searchGroup);
	    moreChooseAdapter = new MoreChooseAdapter(context,R.layout.pop_menuitem,list);
	    listView.setAdapter(moreChooseAdapter);
	    listView.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> parent, View view,int position,long id){
				MtitlePopupWindow.this.dismiss();
				Log.i(tag, "listView********");
				if(listener != null){
					listener.onPopupWindowItemClick(position);
					Log.i(tag, "position:"+position);
				}
			}

			
		});
	    
	    
	}
	
	public void setOnPopupWindowClickListener(OnPopupWindowClickListener listener){
		this.listener = listener;
	}
	
	public interface OnPopupWindowClickListener{
		/**
		 * 当点击PopupWindow的ListView 的item的时候调用此方法，用回调方法的好处就是降低耦合性
		 * @param position 位置
		 */
		void onPopupWindowItemClick(int position);
	}
	
}

package com.atm.chatonline.chat.adapter;

import java.io.FileNotFoundException;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atm.chatonline.bbs.commom.CircleImageView;
import com.atm.chatonline.chat.info.ChatMessage;
import com.atm.chatonline.chat.util.Config;
import com.atm.chatonline.chat.util.ExpressionUtil;
import com.atm.chatonline.chat.util.FileUtil;
import com.atm.chatonline.chat.util.TimeUtil;
import com.example.studentsystem01.R;




public class ChatAdapter extends BaseAdapter{

	protected static final String tag = "ChattingAdapter";
	private Context context;
	private List<ChatMessage> chatMessages;
	private String lastTime="";
	

	public ChatAdapter(Context context, List<ChatMessage> messages) {
		super();
		this.context = context;
		this.chatMessages = messages;
	}

	public int getCount() {
		return chatMessages.size();
	}

	public Object getItem(int position) {
		return chatMessages.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		ChatMessage message = chatMessages.get(position);
		int direction=message.getDirection();
		int type=message.getType();
		if (convertView == null
				|| (holder = (ViewHolder) convertView.getTag()).flag != message
						.getDirection()) {
			holder = new ViewHolder();
			if (direction == Config.MESSAGE_FROM) {
				if(type==Config.MESSAGE_TEXT){
				convertView = LayoutInflater.from(context).inflate(
						R.layout.chatfrom_list_view, null);
				}else if(type==Config.MESSAGE_IMG){
					convertView = LayoutInflater.from(context).inflate(R.layout.chatfrom_list_img, null);
				}
			} else if(direction == Config.MESSAGE_TO){
				if(type==Config.MESSAGE_TEXT){
				convertView = LayoutInflater.from(context).inflate(
						R.layout.chatto_list_view, null);
				}else if(type==Config.MESSAGE_IMG){
					convertView = LayoutInflater.from(context).inflate(R.layout.chatto_list_img, null);
				}
			}
			
			
			convertView.setTag(holder);
			holder.head=(CircleImageView)convertView.findViewById(R.id.chat_head_pic);
			holder.time=(TextView)convertView
					.findViewById(R.id.chat_time);//用它显示内容
			
			holder.text = (TextView) convertView
					.findViewById(R.id.chat_content);//用它显示时间
			
			holder.img = (ImageView)convertView.findViewById(R.id.chat_img);//聊天发送图片
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		
		String time=message.getTime();
		
		
//		Log.i(TAG, "time="+time);
		if(time!=null && !"".equals(time)){
			String content=message.getContent();
			if(message.equals(chatMessages.get(0))){
				Log.i(tag, "chatMessage是第一条，我的内容是:"+content);
				String relativeTime=TimeUtil.compareTime(time,time,1);
				holder.time.setText(relativeTime);
				Log.i(tag, "我要显示时间:");
				Log.i(tag, "chatMessage是第一条，我永远走的是1");
				Log.i(tag, "relativeTime="+relativeTime+" 时间");
			}else{
					lastTime=chatMessages.get(chatMessages.size()-2).getTime();
					Log.i(tag, "chatMessage不是第一条,我的内容是:"+content);
					String relativeTime=TimeUtil.compareTime(time,lastTime,message.getShowTime());
					
					Log.i(tag, "relativeTime="+relativeTime+" 时间");
					holder.time.setText(relativeTime);
					if(message.getShowTime()==0){
					if(relativeTime.equals("")){
						message.setShowTime(-1);
						Log.i(tag, "chatMessage不是第一条，我永远走的是-1");
					}
					else{
						message.setShowTime(1);
						Log.i(tag, "chatMessage不是第一条，我永远走的是1");
					}
					}else{
						Log.i(tag, "我的内容是:"+content+"我骄傲，不用重新走，我的showTime是:"+message.getShowTime());
					}
					
				
			}
		}
		Log.i(tag, "ChatAdapter-----我在变化");
		Log.i(tag, "我要显示内容:"+message.getContent());
		if(direction==Config.MESSAGE_FROM){
		holder.head.setImageBitmap(FileUtil.getBitmap(FileUtil.APP_PATH+"/friend"+"/"+message.getSelfID()+"/"+message.getFriendID()+".jpg"));
		Log.i(tag, "获取"+message.getFriendID()+"的头像");
		}else{
			holder.head.setImageBitmap(FileUtil.getBitmap(Environment.getExternalStorageDirectory()+"/ATM/userhead/"+message.getSelfID()+".jpg"));
			Log.i(tag, "获取"+message.getSelfID()+"的头像");
		}
		
		//判断message 的 type ，从而决定是显示文字还是图片
		if(type==Config.MESSAGE_TEXT){
		String str = message.getContent();														//消息具体内容
		Log.i(tag, "str:::"+str);
		String zhengze = "\\#(exp\\d{2})";											//正则表达式，用来判断消息内是否有表情
		SpannableString spannableString = ExpressionUtil.getExpressionString(context, str, zhengze);//在方法里面就开始把整串文字和正则表达式比对
		
		holder.text.setText(spannableString);
		}else if(type==Config.MESSAGE_IMG){
			String filePath = message.getContent();
			try{
				Bitmap bitmap = getSmallPic(filePath,100);
				holder.img.setLayoutParams(new LinearLayout.LayoutParams(180,240));
				holder.img.setScaleType(ScaleType.FIT_XY);
				holder.img.setImageBitmap(bitmap);
			}catch(FileNotFoundException e){
				
			}
		}
		return convertView;
	}
	
	private Bitmap getSmallPic(String filePath,int width)throws FileNotFoundException{
		BitmapFactory.Options options = new BitmapFactory.Options();
		//但如果使用一个BitmapFactory.Options对象，并把该对象的inJustDecodeBounds属性设置为true，
		//decodeResource()方法就不会生成Bitmap对象，而仅仅是读取该图片的尺寸和类型信息：
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		int be = (int)(options.outWidth/(float)width);
		options.inSampleSize = be;
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
		return bitmap;
	}

	// 优化listview的Adapter
	static class ViewHolder {
		CircleImageView head;
		
		TextView time;
		TextView text;
		ImageView img;
		int flag;
	}
	

}

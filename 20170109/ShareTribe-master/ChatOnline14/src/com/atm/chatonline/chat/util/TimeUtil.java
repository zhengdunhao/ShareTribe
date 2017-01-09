package com.atm.chatonline.chat.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.util.Log;

public class TimeUtil {
	/**
	 * 获取绝对时间(系统当前时间),时间格式为"yyMMddHHmmss"
	 * 
	 * @return
	 */
	private static String tag = "TimeUtil";

	public static String getAbsoluteTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}
	
	public static String getAbsoluteTime2(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		return sdf.format(new Date());
	}
	
	/**
	 * date本条信息的时间，lastTime为上一条的信息，showTime是ChatMessage类的一个整型属性。
	 * 其中showTime=0表示每一次更新信息时，本条信息要与上一条信息进行比较
	 * showTime=-1表示本条信息与上一条信息时间相隔不到3分钟，所以它在以后更新UI里面，从此不用显示时间，
	 * showTime=1表示本条信息与上一条信息时间相隔超过3分钟，在界面里直接显示本条信息的绝对时间，从此也是显示自己ChatMessage里存入的时间
	 * 
	 * */

	public static String compareTime(String date,String lastTime,int showTime){
		String time="";
		try {
			SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date dt1=sdf.parse(date);
			Date dt3=sdf.parse(lastTime);
			
			Calendar cl=Calendar.getInstance();
			cl.setTime(dt1);
			int year1=cl.get(Calendar.YEAR);
			int month1=cl.get(Calendar.MONTH);
			int day1=cl.get(Calendar.DAY_OF_MONTH);
			int hour1=cl.get(Calendar.HOUR_OF_DAY);
			int minute1=cl.get(Calendar.MINUTE);
			int second1=cl.get(Calendar.SECOND);
			
			cl.setTime(dt3);
			int year3=cl.get(Calendar.YEAR);
			int month3=cl.get(Calendar.MONTH);
			int day3=cl.get(Calendar.DAY_OF_MONTH);
			int hour3=cl.get(Calendar.HOUR_OF_DAY);
			int minute3=cl.get(Calendar.MINUTE);
			int second3=cl.get(Calendar.SECOND);
			
			Log.i(tag, "本条主短信(发或收)的时间是："+year1+"年"+month1+"月"+day1+"日"+hour1+"小时"+minute1+"分"+second1+"秒");
			Log.i(tag, "上一条主短信的时间是："+year3+"年"+month3+"月"+day3+"日"+hour3+"小时"+minute3+"分"+second3+"秒");
			if(showTime==0){
				if(year1==year3){
					if(month1==month3){
						if(day1==day3){
							if(hour1==hour3){
								if((minute1 - minute3) < 3){
									return time="";
								}else{
									return time=date;
								}
							}
						}
					}
				}
			}else if(showTime==1){
				return time=date;
			}else if(showTime==-1){
				return time="";
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			Log.i(tag, "你妹，时间报错了，getRelativeTime（）是走进来了，但时间格式可能不对啊");
			e.printStackTrace();
		}
		return time;
	}

	/**
	 * 获取相对时间(将给点的时间变换成相对于系统当前时间的差值)，格式为“XX分钟前”
	 * 
	 * @return
	 */
	

	private static String formatTime(int hour, int minute) {
		String time = "";
		if (hour < 10) {
			time += "0" + hour + ":";
		} else {
			time += hour + ":";
		}

		if (minute < 10) {
			time += "0" + minute;
		} else {
			time += minute;
		}
		System.out.println("format(hour, minute)=" + time);
		return time;
	}
}

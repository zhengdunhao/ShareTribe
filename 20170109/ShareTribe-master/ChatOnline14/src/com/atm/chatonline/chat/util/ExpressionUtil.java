package com.atm.chatonline.chat.util;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;

import com.example.studentsystem01.R;


public class ExpressionUtil {
	/**
	 * 瀵箂panableString杩涜姝ｅ垯鍒ゆ柇锛屽鏋滅鍚堣姹傦紝鍒欎互琛ㄦ儏鍥剧墖浠ｆ浛
	 * @param context
	 * @param spannableString
	 * @param patten
	 * @param start
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws NumberFormatException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	
	private static String tag="ExpressionUtil";
    public static void dealExpression(Context context,SpannableString spannableString, Pattern patten, int start) throws SecurityException, NoSuchFieldException, NumberFormatException, IllegalArgumentException, IllegalAccessException {
    	Matcher matcher = patten.matcher(spannableString);
    	Log.i(tag, "dealExpression");
        while (matcher.find()) {
        	Log.i(tag, "dealExpression33");
            String key = matcher.group();
            if (matcher.start() < start) {
                continue;
            }
            key=key.substring(1);//去掉井号，得到的字符串匹配图片文字
            Log.i(tag, "key:"+key);
            Field field = R.drawable.class.getDeclaredField(key);
			int resId = Integer.parseInt(field.get(null).toString());		//通过上面匹配得到的字符串来生成图片资源id
			Log.i(tag,"resId:"+resId);
            if (resId != 0) {
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);	
                ImageSpan imageSpan = new ImageSpan(bitmap);				//通过图片资源id来得到bitmap，用一个ImageSpan来包装
                int end = matcher.start() + key.length()+1;					//计算该图片名字的长度，也就是要替换的字符串的长度
                spannableString.setSpan(imageSpan, matcher.start(), end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);	//将该图片替换字符串中规定的位置中
                if (end < spannableString.length()) {						//如果整个字符串还未验证完，则继续。。
                    dealExpression(context,spannableString,  patten, end);
                }
                break;
            }
        }
        Log.i(tag, "dealExpression2");
    }
    
    /**
     * 寰楀埌涓?釜SpanableString瀵硅薄锛岄?杩囦紶鍏ョ殑瀛楃涓?骞惰繘琛屾鍒欏垽鏂?     * @param context
     * @param str
     * @return
     */
    public static SpannableString getExpressionString(Context context,String str,String zhengze){
    	Log.i(tag, "str:"+str+",zhengze:"+zhengze);
    	SpannableString spannableString = new SpannableString(str);
    	Pattern sinaPatten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);		//通过传入的正则表达式来生成一个pattern
        try {
            dealExpression(context,spannableString, sinaPatten, 0);
        } catch (Exception e) {
            Log.e("dealExpression", e.getMessage());
        }
        return spannableString;
    }
	

}
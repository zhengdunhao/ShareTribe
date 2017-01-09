package com.atm.chatonline.bbs.commom;
/*
 * 该类用于 按钮触发后的倒计时状态
 * 2015.7.26,atm--小二
 */
import android.os.CountDownTimer;
import android.widget.Button;


public class CountDownUtil {
		private TimeCount time;
		private Button but = null;
		
		//定义一个带参的构造函数，该参数为Button类型
		public CountDownUtil(Button but){
			this.but = but;
		}
		
		public void action(){
			time = new TimeCount(60000,1000);//总时长为60秒，计时的时间间隔为1秒
			time.start();//开始计时
			
		}

		/*定义一个倒计时的内部类*/
		class TimeCount extends CountDownTimer{
			//重写三个方法
			public TimeCount(long millisInFuture, long countDownInterval) {
				super(millisInFuture, countDownInterval);
				// TODO Auto-generated constructor stub
				//参数一次为总时长，和计时的时间间隔
			}

			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
				//计时过程显示
				but.setClickable(false);//计时过程按钮无法再触发
				but.setText(millisUntilFinished/1000+"秒");
				
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				//计时完毕时触发
				but.setText("重新验证");
				but.setClickable(true);
			}
			
		}
}

package aiwac.admin.com.healthrobot.sport;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;


// 使用说明
// 1. AiwacSportApi aiwacSportApi = new AiwacSportApi();
// 2. aiwacSportApi.aiwacSportType(type);  其中type 不同的位会有不同的含义，具体看 aiwacSportType 的逻辑



public class AiwacSportApi {
	 public  boolean flag=false;
	public Socket socketA33 = null;
	OutputStream os ;
	BufferedReader br;  //read from socket
	String content  = null ;

	String A33Ip = "127.0.0.1";
	int socketPort = 9999;


	String aiwacState = "1";
	int stateFlag = 0;  //0£º×´Ì¬»¹Ã»ºÃ    1£º×´Ì¬¿ÉÒÔ¿©£¬¿ÉÒÔ¸üÐÂÀ²


	boolean LinkStatus = true;  // 当前连接状态,


	Handler mainHandle, myhandle;

	public AiwacSportApi()
	{
		Log.i("A33Socket", "启动  link  ，并检测");
		this.startAiwacSport();
		this.linsentingLinkStatus();
	}


	public void stopAiwacSport(){
		if(this.socketA33!=null){
			try {
				this.socketA33.close();
				Log.i("A33Socket", "this.socketA33.close()");
				this.socketA33 = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else
		{
			Log.i("A33Socket", "未执行close, socketA33==null");
		}

	}

	// 检测当前连接状态 ，否并 重连
	public void linsentingLinkStatus()
	{
		new Thread()
		{
			public void run()
			{
				try {
					sleep(10000);
					Log.i("A33Socket", "开始检测  连接情况");

				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				while (true)
				{

					try {
						sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					try{
						socketA33.sendUrgentData(0xFF);
					}catch(Exception ex){
						//连接断开
						Log.i("A33Socket", "检测到A33Socket连接已断开，启动重连");
						LinkStatus = false;
					}


					if (LinkStatus == false)   //启动重连
					{
						try {

							socketA33.close();

							socketA33 = null;
							socketA33 = new Socket(A33Ip, socketPort);

							if (socketA33 == null)
							{
								Log.i("A33Socket", " 启动重连 link error!");
								continue;
							}
							else
							{
								Log.i("A33Socket","启动重连 link ok!");
								LinkStatus = true ;
							}
							os = socketA33.getOutputStream();
							br = new BufferedReader(new InputStreamReader(socketA33.getInputStream(), "utf-8"));
							//content = br.readLine()

						} catch (UnknownHostException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					else
					{
						Log.i("A33Socket", " 连接健在!");
					}

				}

			}
		}.start();


	}


//	// 健康机器人用的发送接口
//	public void sendOrder2AIWACRobot(int type)
//    {
//       // boolean ret = false;
//        this.flag = false;  //socket  连接标志
//
//        this.startAiwacSport();
//
//        // 等待子线程执行
//        while (!this.flag)
//            ;
//        this.aiwacSportType(type);
//		try {
//			Thread.sleep(100);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//
//
//		this.stopAiwacSport();
//
////        return ret;
//    }



	public boolean startAiwacSport()
	{

		new Thread()
		{
			public void run()
			{
				A33Ip = "127.0.0.1";
				Log.i("A33Socket", "enter run()!");
				try {
					socketA33 = null;
					socketA33 = new Socket(A33Ip, socketPort);

					if (socketA33 == null)
					{
						Log.i("A33Socket", "link error!");
					}
					else
					{
						Log.i("A33Socket","link ok!");
						flag=true;
					}

					os = socketA33.getOutputStream();
					br = new BufferedReader(new InputStreamReader(socketA33.getInputStream(), "utf-8"));
					//content = br.readLine()


					Looper.prepare();
					myhandle = new Handler(){
						public void handleMessage(Message msg) {
							// TODO Auto-generated method stub
							if(msg.what == 333)
							{
								try {
									Log.i("A33Socket", "发送前："+ msg.obj.toString());
									os.write((msg.obj.toString()).getBytes("utf-8"));
									Log.i("A33Socket", "发送后："+ msg.obj.toString());
								} catch (UnsupportedEncodingException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									Log.i("A33Socket", "发送失败");


								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									Log.i("A33Socket", "发送失败");
								}

//								Log.i("A33Socket", "发送一次");


							}
						}

					};
					Looper.loop();


					while(true)
					{

						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						Log.i("A33Socket", "检测接受情况");
						if((content = br.readLine())!=null)
						{
							;
						}

					}



				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


			}
		}.start();

		return  flag;
	}


	private void SportClass(String sportCode)
	{
		Message ms = new Message();
		ms.what = 333;

		content = sportCode;
		Log.i("A33Socket", "sportCode "+content);
		ms.obj = content;
		myhandle.sendMessage(ms);
	}

	public void aiwacSportType(int type)
	{
		int sportTemp = 0;
		Log.i("A33Socket", "type :"+type);

		//ÔË¶¯Ê¶±ð
		sportTemp = type & 15;
		Log.i("A33Socket", "sportTemp:"+sportTemp);
		switch (sportTemp)
		{
			case 1:
				this.SportClass("a");  // 前
				Log.i("A33Socket", "SportClass:"+"a");
				break;
			case 2:
				this.SportClass("b");  // 后
				Log.i("A33Socket", "SportClass:"+"b");
				break;
			case 4:
				this.SportClass("c");  // 左
				Log.i("A33Socketg", "SportClass:"+"c");
				break;
			case 8:
				this.SportClass("d");  //右
				Log.i("A33Socket", "SportClass:"+"d");
				break;
			case 9:
				this.SportClass("e");  // 前+右
				Log.i("A33Socket", "SportClass:"+"e");
				break;
			case 5:
				this.SportClass("f"); // 前+左
				Log.i("A33Socket", "SportClass:"+"f");
				break;
			case 10:
				this.SportClass("g"); //后+右
				Log.i("A33Socket", "SportClass:"+"g");
				break;
			case 6:
				this.SportClass("h"); //后+左
				Log.i("A33Socket", "h");
				break;
			case 0:
				this.SportClass("i"); // 停止
				Log.i("A33Socket", "SportClass:"+"i");
				break;
			default:
				break;
		}


		sportTemp = type & 16;
		Log.i("A33Socket", "灯1 sportTemp:"+sportTemp);
		//¼ì²â  µÆ1µÄÇé¿ö
		if ( (sportTemp & 16) == 16 )
		{
			this.SportClass("j"); // 灯1  开
		}
		else if( (sportTemp & 16) == 0 )
		{
			this.SportClass("k"); // 灯1  关
		}

		sportTemp = type & 32;
		Log.i("A33Socket", "灯2  sportTemp:"+sportTemp);
		//¼ì²â  µÆ2µÄÇé¿ö
		if ( (sportTemp & 32) == 32 )
		{
			this.SportClass("l"); // 灯2  开
		}
		else if ( (sportTemp & 32) == 0 )
		{
			this.SportClass("m"); //  灯2  关
		}


		sportTemp = type & 64;
		Log.i("A33Socket", "未实现 sportTemp:"+sportTemp);
		//  暂时未实现
		if ( (sportTemp & 64) ==64 )
		{
			this.SportClass("n"); // free
		}


	}
	public String getAiwacState()
	{
		Message ms = new Message();
		ms.what = 333;
		content = "k";   //ÇëÇóµÄ¾ßÌåµÃÔÙÉè¼ÆÏÂ
		ms.obj = content;
		myhandle.sendMessage(ms);

		while (true)
		{
			if (stateFlag  == 1)
			{
				break;
			}

		}
		return aiwacState;
	}
}

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

public class AiwacSportApi {

	Socket socketA33 = null;
	OutputStream os ;
	BufferedReader br;  //read from socket
	String content  = null ;
	String A33Ip = "";
	String aiwacState = "1";
	int stateFlag = 0;  //0£º×´Ì¬»¹Ã»ºÃ    1£º×´Ì¬¿ÉÒÔ¿©£¬¿ÉÒÔ¸üÐÂÀ²
	Handler mainHandle, myhandle;

	public void startAiwacSport()
	{
		new Thread()
		{
			public void run()
			{
				A33Ip = "127.0.0.1";
				Log.i("tagg", "enter run()!");
				try {
					socketA33 = new Socket(A33Ip, 8888);
					Log.i("tagg", "socket  ok!");
					if (socketA33 == null)
					{
						Log.i("tagg", "link error!");
					}
					else
					{
						Log.i("tagg", "link ok!");
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
									os.write((msg.obj.toString()).getBytes("utf-8"));
								} catch (UnsupportedEncodingException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								Log.i("tagg", "·¢ËÍÒ»´Î");


							}
						}

					};
					Looper.loop();


					while(true)
					{
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
	}


	private void SportClass(String sportCode)
	{
		Message ms = new Message();
		ms.what = 333;

		content = sportCode;
		Log.i("tagg", "·¢ËÍ "+content);
		ms.obj = content;
		myhandle.sendMessage(ms);
	}

	public void aiwacSportType(int type)
	{
		int sportTemp = 0;
		Log.i("tagg", "·¢ËÍ type :"+type);

		//ÔË¶¯Ê¶±ð
		sportTemp = type & 15;
		Log.i("tagg", "·¢ËÍ sportTemp:"+sportTemp);
		switch (sportTemp)
		{
			case 1:
				this.SportClass("a");  //Ç°
				Log.i("tagg", "Ç°");
				break;
			case 2:
				this.SportClass("b");  //ºó
				Log.i("tagg", "ºó");
				break;
			case 4:
				this.SportClass("c");  // ×ó
				Log.i("tagg", "×ó");
				break;
			case 8:
				this.SportClass("d");  //ÓÒ
				Log.i("tagg", "ÓÒ");
				break;
			case 9:
				this.SportClass("e");  // Ç° + ÓÒ
				Log.i("tagg", "Ç° + ÓÒ");
				break;
			case 5:
				this.SportClass("f"); // Ç° + ×ó
				Log.i("tagg", "Ç° + ×ó");
				break;
			case 10:
				this.SportClass("g"); // ºó + ÓÒ
				Log.i("tagg", "ºó + ÓÒ");
				break;
			case 6:
				this.SportClass("h"); // ºó + ×ó
				Log.i("tagg", "ºó + ×ó");
				break;
			case 0:
				this.SportClass("i"); // Í£
				Log.i("tagg", "Í£");
				break;
			default:
				break;
		}


		sportTemp = type & 16;
		Log.i("tagg", "¼ì²â  µÆ1µÄÇé¿ö  sportTemp:"+sportTemp);
		//¼ì²â  µÆ1µÄÇé¿ö
		if ( (sportTemp & 16) == 16 )
		{
			this.SportClass("j"); // ¿ªµÆ1
		}
		else if( (sportTemp & 16) == 0 )
		{
			this.SportClass("k"); // ¹ØµÆ1
		}

		sportTemp = type & 32;
		Log.i("tagg", "¼ì²â  µÆ2µÄÇé¿ö  sportTemp:"+sportTemp);
		//¼ì²â  µÆ2µÄÇé¿ö
		if ( (sportTemp & 32) == 32 )
		{
			this.SportClass("l"); // ¿ªµÆ2
		}
		else if ( (sportTemp & 32) == 0 )
		{
			this.SportClass("m"); // ¹ØµÆ2
		}


		sportTemp = type & 64;
		Log.i("tagg", "¼ì²â  ×ÔÓÉ»î¶¯ sportTemp:"+sportTemp);
		//ÊÇ·ñ×ÔÓÉ»î¶¯
		if ( (sportTemp & 64) ==64 )
		{
			this.SportClass("n"); // ×ÔÓÉ»î¶¯
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

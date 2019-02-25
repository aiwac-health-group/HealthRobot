package aiwac.admin.com.healthrobot.utils;

import android.os.Environment;

import java.io.File;
import java.io.IOException;

public class FileUtil {
	
	public static File updateDir = null;
	public static File updateFile = null;
	public static final String Application = "AiwacAndroid";
	
	public static boolean isCreateFileSucess;
	public static void createFile(String app_name) {
		
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			isCreateFileSucess = true;
			
			updateDir = new File(Environment.getExternalStorageDirectory()+ "/" +Application +"/");
			updateFile = new File(updateDir + "/" + app_name + ".apk");

			if (!updateDir.exists()) {
				updateDir.mkdirs();
			}
			//updateFile.createNewFile();
			if (!updateFile.exists()) {
				try {
					updateFile.createNewFile();
				} catch (IOException e) {
					isCreateFileSucess = false;
					e.printStackTrace();
				}
			}

		}else{
			isCreateFileSucess = false;
		}
	}
}
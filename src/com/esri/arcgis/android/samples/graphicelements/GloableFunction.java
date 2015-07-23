package com.esri.arcgis.android.samples.graphicelements;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;

import textwatcher.ValueNameDomain;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class GloableFunction {
	public static int Map_SELECT_CODE=0;
	public static int  User_SELECT_CODE=1;
	public static int ImPort_Point_CODE=2;
	public static String VERSION_KEY="VERSION_KEY";
	public static ArrayList<ValueNameDomain> UserList;
	public static boolean isFirstRun(Context context) {
		PackageInfo info;
		try {
			info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			int currentVersion = info.versionCode;  
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);  
			int lastVersion = prefs.getInt(GloableFunction.VERSION_KEY, 0);  
			if (currentVersion > lastVersion) {  
			     //如果当前版本大于上次版本，该版本属于第一次启动 
			     //将当前版本写入preference中，则下次启动的时候，据此判断，不再为首次启动  
			     prefs.edit().putInt(GloableFunction.VERSION_KEY,currentVersion).commit();  
			     return true;
			}  
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} 
		return false;
	}
	 public static String convertToSexagesimal(String numStr){  
	        double num = Double.parseDouble(numStr);  
	        int du=(int)Math.floor(Math.abs(num));    //获取整数部分  
	        double temp=getdPoint(Math.abs(num))*60;  
	        int fen=(int)Math.floor(temp); //获取整数部分  
	        double miao=getdPoint(temp)*60;  
	        if(num<0)  
	        return "-"+du+"/1,"+fen+"/1,"+miao+"/1";  
	      
	        return du+"/1,"+fen+"/1,"+miao+"/1";  
	  
	    }  
	    //获取小数部分  
	    private static double getdPoint(double num){  
	        double d = num;  
	        int fInt = (int) d;  
	        BigDecimal b1 = new BigDecimal(Double.toString(d));  
	        BigDecimal b2 = new BigDecimal(Integer.toString(fInt));  
	        double dPoint = b1.subtract(b2).floatValue();  
	        return dPoint;  
	    }  
	 public static void ShowMessage(Context contxt,String message)
	    {
	    	Toast.makeText(contxt, message, Toast.LENGTH_SHORT).show();
	    }
}

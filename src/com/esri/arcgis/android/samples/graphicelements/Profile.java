package com.esri.arcgis.android.samples.graphicelements;

import android.content.Context;
import android.content.SharedPreferences;

public class Profile {
	 public SharedPreferences preferences; 
	 public SharedPreferences.Editor editor;
	 Context context;
	 public Profile(Context parContext)
	 {
		 context=parContext;
		 preferences = context.getSharedPreferences("ArcGis",Context.MODE_WORLD_READABLE); 
		 editor = preferences.edit(); 
	 }
	 //原生的获取；
	 public int getInt(String key)
	 {
		 int num = preferences.getInt(key, 0);
		 return num;
	 }
	 //原生的获取；
	 public String getString(String key)
	 {
		 String str=preferences.getString(key, "");
		 return str;		 
	 }
	 public int getInt(int key)
	 {
		return getInt(context.getResources().getString(key));
	 }
	 public String getString(int key)
	 {
		return getString(context.getResources().getString(key));
	 }	
	 public void Save(int key,String value)
	 {
		 Save(context.getResources().getString(key),value);
	 }
	 public void Save(int key,int value)
	 {
		 Save(context.getResources().getString(key),value);
	 }
	 public void Save(String key,int num)
	 {
         editor.putInt(key,num); 
         editor.commit(); 
	 }
	 public void Save(String key,String value)
	 {
         editor.putString(key,value); 
         editor.commit(); 
	 }
	 
}

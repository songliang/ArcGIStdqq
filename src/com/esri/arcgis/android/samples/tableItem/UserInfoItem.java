package com.esri.arcgis.android.samples.tableItem;
import java.io.Serializable;

import android.annotation.SuppressLint;
import com.esri.arcgis.android.samples.graphicelements.R.id;
import com.j256.ormlite.field.DatabaseField; 

public class UserInfoItem implements Serializable,IExportFunction {  
	public UserInfoItem() {
		super();
	}
	public UserInfoItem(String name, String iDNumber) {
		super();
		Name = name;
		IDNumber = iDNumber;
	}
	public UserInfoItem(int iD, String name, String iDNumber) {
		super();
		ID = iD;
		Name = name;
		IDNumber = iDNumber;
	}
	private static final long serialVersionUID = -568326366991817105L; 
	@DatabaseField(allowGeneratedIdInsert=true,generatedId=true)  
	   private int ID;  
	   @DatabaseField  
	   private String Name;
	   @DatabaseField  
	   private String IDNumber;
	public int getID() {
		return ID;
	}
	public void setId(int iD) {
		ID = iD;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getIDNumber() {
		return IDNumber;
	}
	public void setIDNumber(String iDNumber) {
		IDNumber = iDNumber;
	}
	@Override
	public String getCvsTitle() {
		// TODO Auto-generated method stub
		//return null;
		return "编号,姓名,身份证号";
	}
	@Override
	public String getCvsData() {
		// TODO Auto-generated method stub
		//return null;
		return String.format("%d,%s,%s",ID,Name,IDNumber);
	}
}

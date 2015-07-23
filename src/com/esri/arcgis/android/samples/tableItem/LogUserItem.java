package com.esri.arcgis.android.samples.tableItem;
import java.io.Serializable;

import com.esri.arcgis.android.samples.graphicelements.R.id;
import com.j256.ormlite.field.DatabaseField; 

public class LogUserItem implements Serializable,IExportFunction {  
	public LogUserItem(int iD, String name, String password) {
		super();
		ID = iD;
		Name = name;
		Password = password;
	}
	public LogUserItem() {
		super();
	}
	public LogUserItem(String name, String password) {
		super();
		Name = name;
		Password = password;
	}
	private static final long serialVersionUID = -568326366991817104L;
	@DatabaseField(allowGeneratedIdInsert=true,generatedId=true)  
	   private int ID;  
	   @DatabaseField  
	   private String Name;
	   @DatabaseField  
	   private String Password;
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
		public String getPassword() {
			return Password;
		}
		public void setPassword(String password) {
			Password = password;
		}
		@Override
		public String getCvsTitle() {
			// TODO Auto-generated method stub
			//return null;
			return "±‡∫≈,–’√˚,√‹¬Î";
		}
		@Override
		public String getCvsData() {
			// TODO Auto-generated method stub
			//return null;
			return String.format("%d,%s,%s",ID,Name,Password);
		} 
}

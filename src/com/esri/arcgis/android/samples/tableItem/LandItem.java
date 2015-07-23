package com.esri.arcgis.android.samples.tableItem;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;

public class LandItem implements Serializable,IExportFunction {  
	 /**
	 * 
	 */
	private static final long serialVersionUID =-5683263669923174550L;
	public LandItem() {
		super();
	}
	 public LandItem(int id) {
			super();
			this.ID=id;
	}
	public LandItem(int iD, String name, String iDNumber, String housID,
				String groundID, String groundSize, String other) {
			super();
			ID = iD;
			Name = name;
			IDNumber = iDNumber;
			this.housID = housID;
			this.groundID = groundID;
			this.groundSize = groundSize;
			this.other = other;
	}
	 @DatabaseField(allowGeneratedIdInsert=true,generatedId=true)  
	  private int ID;  
	   @DatabaseField  
	   private String Name;
	   @DatabaseField  
	   private String IDNumber;
	 
	@DatabaseField  
	   private String housID;
	   @DatabaseField  
	   private String groundID;
	   @DatabaseField  
	   private String groundSize;
	   @DatabaseField  
	   private String other;
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
	public String getHousID() {
		return housID;
	}
	public void setHousID(String housID) {
		this.housID = housID;
	}
	public String getGroundID() {
		return groundID;
	}
	public void setGroundID(String groundID) {
		this.groundID = groundID;
	}
	public String getGroundSize() {
		return groundSize;
	}
	public void setGroundSize(String groundSize) {
		this.groundSize = groundSize;
	}
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
	@Override
	public String getCvsTitle() {
		// TODO Auto-generated method stub
		return "序号,姓名,身份证号,户名,地块号,合同面积,备注";
	}
	@Override
	public String getCvsData() {
		// TODO Auto-generated method stub
		return String.format("%d,%s,%s,%s,%s,%s,%s,%s",ID,Name,IDNumber,housID,groundID,groundSize,other);
	}
}

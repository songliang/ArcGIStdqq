package com.esri.arcgis.android.samples.tableItem;
import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField; 
public class PointFullItem implements Serializable,IExportFunction {	
	public PointFullItem(int id, String name, String PersonID,String housID, String groundID,
			String groundSize, String other, double lat, double lng,
			String imgPath) {
		super();
		this.ID = id;
		this.name = name;
		this.PersonID=PersonID;
		this.housID = housID;
		this.groundID = groundID;
		this.groundSize = groundSize;
		this.other = other;
		this.lat = lat;
		this.lng = lng;
		this.imgPath = imgPath;
	}	
	public PointFullItem() {
		super();
	}
	private static final long serialVersionUID = -5683263669918171030L;  
	/*  <item >要素ID</item>
       <item >姓名</item>
       <item >户号</item>
       <item >地块号</item>
       <item >面积</item>
       <item >备注</item>
       <item >经度</item>
       <item >纬度</item>　
       <item >关联相片编号</item>
       <item >删除</item>
    */
   @DatabaseField(generatedId=true)  
   private int ID;  
   @DatabaseField  
   private String name;
   @DatabaseField  
   private String PersonID;
   @DatabaseField  
   private String housID;
   @DatabaseField  
   private String groundID;
   @DatabaseField  
   private String groundSize;
   @DatabaseField  
   private String other;
   @DatabaseField  
   private double lat;
   @DatabaseField  
   private double lng;
   private String code;
   @DatabaseField
   private String imgPath;   
   public String getPersonID() {
	return PersonID;
}
public void setPersonID(String personID) {
	PersonID = personID;
}

   public int getId() {
	return ID;
	}
	public void setId(int id) {
		this.ID = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	@Override
	public String getCvsTitle() {
		// TODO Auto-generated method stub
		//return null;
		return "要素编号,姓名,身份证号,户名,地块号,合同面积,备注,经度,纬度,关联相片编号";
	}
	@Override
	public String getCvsData() {
		// TODO Auto-generated method stub
		//return null;
		return String.format("%d,%s,%s,%s,%s,%s,%s,%f,%f,%s",ID,name,PersonID,housID,groundID,groundSize,other,lng,lat,imgPath);
	} 
     
}  


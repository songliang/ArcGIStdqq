package com.esri.arcgis.android.samples.tableItem;
import java.io.Serializable;

import com.esri.arcgis.android.samples.graphicelements.R.string;
import com.j256.ormlite.field.DatabaseField; 
public class JieZhiPointItem implements Serializable,IExportFunction {  
	 public JieZhiPointItem() {
		super();
	}
	 public JieZhiPointItem(int id) {
			super();
			this.ID=id;
	}
	 public JieZhiPointItem(String other, double lat, double lng, int rtkID) {
			super();
			this.other = other;
			this.lat = lat;
			this.lng = lng;
			this.rtkID = rtkID;
	}
	public JieZhiPointItem(int id, String other, double lat, double lng, int rtkID) {
		super();
		this.ID = id;
		this.other = other;
		this.lat = lat;
		this.lng = lng;
		this.rtkID = rtkID;
	}
	private static final long serialVersionUID = -5683263669918171030L;  
	/* 
 		<item >要素ID</item>
        <item >经度</item>
        <item >纬度</item>
        <item >关联RTK测点号</item>
        <item >备注</item>      
    */	
   @DatabaseField(allowGeneratedIdInsert=true,generatedId=true)  
   private int ID;  
   @DatabaseField  
   private String other;
   @DatabaseField  
   private double lat;
   @DatabaseField  
   private double lng;
   @DatabaseField
   private int	rtkID;
   public int getId() {
	   return ID;
   }
	public void setId(int id) {
		this.ID = id;
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
	public int getRtkID() {
		return rtkID;
	}
	public void setRtkID(int rtkID) {
		this.rtkID = rtkID;
	}
	@Override
	public String getCvsTitle() {
		// TODO Auto-generated method stub
		//return null;
		return "要素编号,经度,纬度,关联RTK测点号,备注";
	}
	@Override
	public String getCvsData() {
		// TODO Auto-generated method stub
		//return null;int id, String other, double lat, double lng, int rtkID
		return String.format("%d,%f,%f,%d,%s,",ID,lat,lng,rtkID,other);
	}
}  


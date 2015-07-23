package com.esri.arcgis.android.samples.tableItem;
import java.io.Serializable;
import com.j256.ormlite.field.DatabaseField; 
public class JieZhiLineItem  implements Serializable ,IExportFunction {  
	 public JieZhiLineItem() {
		super();
	}
	 public JieZhiLineItem(JieZhiPointItem startid, JieZhiPointItem endid) {
			super();
			this.startid = startid;
			this.endid = endid;
	}
	public JieZhiLineItem(int id, JieZhiPointItem startid, JieZhiPointItem endid) {
		super();
		this.ID = id;
		this.startid = startid;
		this.endid = endid;
	}
	private static final long serialVersionUID = -5683263669923171030L;  
	/* 
 		<item >要素ID</item>
        <item >经度</item>
        <item >纬度</item>
        <item >关联RTK测点号</item>
        <item >备注</item>      
    */
   @DatabaseField(allowGeneratedIdInsert=true,generatedId=true)  
   private int ID;
   @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
   private JieZhiPointItem startid;
   @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
   private JieZhiPointItem endid; 
   public int getId() {
	return ID;
	}
	public void setId(int id) {
		this.ID = id;
	}
	public JieZhiPointItem getStartid() {
		return startid;
	}
	public void setStartid(JieZhiPointItem startisd) {
		this.startid = startisd;
	}
	public JieZhiPointItem getEndid() {
		return endid;
	}
	public void setEndid(JieZhiPointItem edndid) {
		this.endid = edndid;
	}
	@Override
	public String getCvsTitle() {
		// TODO Auto-generated method stub
		return "要素编号,起点要素编号,终点要素编号"; 
		//return null;
	}
	@Override
	public String getCvsData() {
		// TODO Auto-generated method stub
		return String.format("%d,%d,%d", ID,startid.getId(),endid.getId());
		//return null;
	}
}
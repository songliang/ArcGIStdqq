package com.esri.arcgis.android.samples.DataControl;
import java.sql.SQLException;
import android.content.Context;
import com.esri.arcgis.android.samples.BasControl.BaseDao;
import com.esri.arcgis.android.samples.tableItem.LogUserItem;
import com.esri.arcgis.android.samples.tableItem.PointFullItem;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;

public class PointFull extends BaseDao<PointFullItem, Integer> {
	public PointFull(Context context) {  
		  super(context);  
		 }
	@Override  
	 public Dao<PointFullItem, Integer> getDao() throws SQLException {  
	  return getHelper().getDao(PointFullItem.class);  
	}
	@Override
	public void ClearTable() throws SQLException {
		// TODO Auto-generated method stub
		TableUtils.clearTable(getHelper().getConnectionSource(), PointFullItem.class);
	}
	@Override
	public String FileName() {
		// TODO Auto-generated method stub
		return "µÿøÈ Ù–‘µ„";
	}
}
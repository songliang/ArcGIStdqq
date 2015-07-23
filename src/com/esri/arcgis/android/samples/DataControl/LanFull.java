package com.esri.arcgis.android.samples.DataControl;

import java.sql.SQLException;

import android.content.Context;

import com.esri.arcgis.android.samples.BasControl.BaseDao;
import com.esri.arcgis.android.samples.tableItem.JieZhiPointItem;
import com.esri.arcgis.android.samples.tableItem.LandItem;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;

public class LanFull  extends BaseDao<LandItem, Integer> {
	public LanFull(Context context) {  
		  super(context);  
		 }
	 	@Override  
		 public Dao<LandItem, Integer> getDao() throws SQLException {  
		  return getHelper().getDao(LandItem.class);  
		}
		@Override
		public void ClearTable() throws SQLException {
			TableUtils.clearTable(getHelper().getConnectionSource(), LandItem.class);
			// TODO Auto-generated method stub
		}
		@Override
		public String FileName() {
			// TODO Auto-generated method stub
			return "详细人员信息表";
		}
}

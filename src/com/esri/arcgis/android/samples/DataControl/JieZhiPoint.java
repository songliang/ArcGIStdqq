package com.esri.arcgis.android.samples.DataControl;

import java.sql.SQLException;

import com.esri.arcgis.android.samples.BasControl.BaseDao;
import com.esri.arcgis.android.samples.tableItem.JieZhiPointItem;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;

import android.content.Context;

public class JieZhiPoint extends BaseDao<JieZhiPointItem, Integer> {
	public JieZhiPoint(Context context) {  
		  super(context);  
		 }
	 	@Override  
		 public Dao<JieZhiPointItem, Integer> getDao() throws SQLException {  
		  return getHelper().getDao(JieZhiPointItem.class);  
		}
		@Override
		public void ClearTable() throws SQLException {
			TableUtils.clearTable(getHelper().getConnectionSource(), JieZhiPointItem.class);
			// TODO Auto-generated method stub
			
		}
		@Override
		public String FileName() {
			// TODO Auto-generated method stub
			return "ΩÁ÷∑µ„±Ì";
		}
}
